package com.github.damontecres.wholphin.services

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
import org.jellyfin.sdk.model.api.ItemFields
import org.jellyfin.sdk.model.api.MediaType
import org.jellyfin.sdk.model.api.request.GetResumeItemsRequest
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

/**
 * Gives ShaddaiTV Home (the launcher) the signed-in user's Continue Watching list for its top shelf.
 * Readable only by apps signed with the same key (signature permission in the manifest).
 */
class HomeShelfProvider : ContentProvider() {
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
        val repo =
            EntryPointAccessors
                .fromApplication(context!!.applicationContext, ProviderEntryPoint::class.java)
                .serverRepository()
        runBlocking {
            withTimeoutOrNull(5.seconds) {
                try {
                    if (repo.currentUser == null) repo.restoreLastSession()
                    val userId = repo.currentUser?.id ?: return@withTimeoutOrNull
                    val api = repo.apiClient
                    val items =
                        api.itemsApi
                            .getResumeItems(
                                GetResumeItemsRequest(
                                    userId = userId,
                                    fields = listOf(ItemFields.OVERVIEW),
                                    limit = LIMIT,
                                    mediaTypes = listOf(MediaType.VIDEO),
                                    enableTotalRecordCount = false,
                                ),
                            ).content.items
                    items.forEach { item ->
                        val isEpisode = item.type == BaseItemKind.EPISODE
                        val showId = item.seriesId ?: item.id
                        val positionTicks = item.userData?.playbackPositionTicks ?: 0L
                        val minutesLeft = item.runTimeTicks?.let { ((it - positionTicks) / 600_000_000L).coerceAtLeast(1) }
                        cursor.addRow(
                            arrayOf<Any?>(
                                item.id.toString(),
                                showId.toString(),
                                if (isEpisode) item.seriesName else item.name,
                                if (isEpisode) "Season ${item.parentIndexNumber}, Episode ${item.indexNumber}" else item.productionYear?.toString(),
                                if (isEpisode) item.name else null,
                                item.overview,
                                minutesLeft,
                                item.userData?.playedPercentage?.toInt(),
                                positionTicks / 10_000,
                                api.imageApi.getItemImageUrl(showId, ImageType.BACKDROP, maxWidth = 1280),
                                api.imageApi.getItemImageUrl(item.id, if (isEpisode) ImageType.PRIMARY else ImageType.BACKDROP, maxWidth = 640),
                            ),
                        )
                    }
                } catch (ex: Exception) {
                    Timber.w(ex, "Home shelf query failed")
                }
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? = null

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
        private const val LIMIT = 8
        private val COLUMNS =
            arrayOf(
                "item_id",
                "show_id",
                "title",
                "episode_line",
                "episode_title",
                "overview",
                "minutes_left",
                "progress",
                "position_ms",
                "backdrop_url",
                "still_url",
            )
    }
}
