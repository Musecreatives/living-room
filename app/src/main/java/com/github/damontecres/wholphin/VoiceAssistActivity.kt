package com.github.damontecres.wholphin

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import timber.log.Timber

/**
 * Handles the remote's mic button (ACTION_VOICE_ASSIST) when Living Room is chosen as its handler:
 * shows the system speech prompt, then opens Living Room search with what was said.
 */
class VoiceAssistActivity : ComponentActivity() {
    private val recognize =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val query =
                result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()
                    ?.takeIf { it.isNotBlank() }
            if (query != null) {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .setAction(Intent.ACTION_SEARCH)
                        .putExtra(SearchManager.QUERY, query)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
                )
            }
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) return // prompt already showing after a config change
        try {
            recognize.launch(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    .putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.search)),
            )
        } catch (ex: Exception) {
            Timber.w(ex, "No speech recognizer available")
            finish()
        }
    }
}
