package com.github.damontecres.wholphin.services

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.github.damontecres.wholphin.data.ServerRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.jellyfin.sdk.api.client.extensions.imageApi
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ImageType
import org.jellyfin.sdk.model.api.request.GetItemsRequest
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

/**
 * Answers Android TV global (voice) search with matching library items, so a result
 * like "Supernatural - Living Room" opens the item via `wholphin://view?itemId=...`.
 *
 * Only the system search service can query it (readPermission GLOBAL_SEARCH in the manifest).
 */
class GlobalSearchProvider : ContentProvider() {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ProviderEntryPoint {
        fun serverRepository(): ServerRepository
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor {
        val cursor = MatrixCursor(COLUMNS)
        val query =
            (selectionArgs?.firstOrNull() ?: uri.lastPathSegment)
                ?.takeUnless { it == SearchManager.SUGGEST_URI_PATH_QUERY }
                ?.trim()
        if (query.isNullOrBlank()) return cursor

        val repo =
            EntryPointAccessors
                .fromApplication(context!!.applicationContext, ProviderEntryPoint::class.java)
                .serverRepository()
        // Search runs on a binder thread with a short budget; an empty cursor is better than a slow one
        runBlocking {
            withTimeoutOrNull(4.seconds) {
                try {
                    if (repo.currentUser == null) repo.restoreLastSession()
                    if (repo.currentUser == null) return@withTimeoutOrNull
                    val api = repo.apiClient
                    val items =
                        api.itemsApi
                            .getItems(
                                GetItemsRequest(
                                    searchTerm = query,
                                    recursive = true,
                                    includeItemTypes = SEARCH_TYPES,
                                    limit = RESULT_LIMIT,
                                    enableTotalRecordCount = false,
                                ),
                            ).content.items
                    items.forEachIndexed { index, item ->
                        val subtitle =
                            when (item.type) {
                                BaseItemKind.EPISODE -> item.seriesName
                                else -> item.productionYear?.toString()
                            }
                        cursor.addRow(
                            arrayOf<Any?>(
                                index,
                                item.name,
                                subtitle,
                                api.imageApi.getItemImageUrl(item.id, ImageType.PRIMARY, maxHeight = 480),
                                "video/*",
                                item.productionYear,
                                item.runTimeTicks?.div(10_000),
                                "android.intent.action.VIEW",
                                "wholphin://view?itemId=${item.id}",
                            ),
                        )
                    }
                } catch (ex: Exception) {
                    Timber.w(ex, "Global search failed for '%s'", query)
                }
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String = SearchManager.SUGGEST_MIME_TYPE

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? = null

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0

    companion object {
        private const val RESULT_LIMIT = 10
        private val SEARCH_TYPES = listOf(BaseItemKind.MOVIE, BaseItemKind.SERIES, BaseItemKind.EPISODE)
        private val COLUMNS =
            arrayOf(
                "_id",
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE,
                SearchManager.SUGGEST_COLUMN_CONTENT_TYPE,
                SearchManager.SUGGEST_COLUMN_PRODUCTION_YEAR,
                SearchManager.SUGGEST_COLUMN_DURATION,
                SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
            )
    }
}
