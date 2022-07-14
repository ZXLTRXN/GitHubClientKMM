package com.example.githubclientkmm.android.utils

import android.content.Context
import android.util.Log
import com.example.githubclientkmm.data.models.Repo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import org.json.JSONException
import org.json.JSONObject

class ColorPicker @Inject constructor(@ApplicationContext private val context: Context) {
    fun addLanguageColor(repos: List<Repo>): List<Repo> {
        val colorsString: String = readFromAssets() ?: return repos
        val colors = try {
            JSONObject(colorsString)
        } catch (e: JSONException) {
            Log.e(TAG, "addLanguageColor:", e)
            return repos
        }
        return repos.map { repo ->
            val lang: String = repo.language ?: return@map repo

            val color: String = try {
                colors.getString(lang)
            } catch (e: JSONException) {
                Log.e(TAG, "addLanguageColor: can't parse color for lang $lang", e)
                null
            } ?: return@map repo

            repo.copy(
                languageColor = color
            )
        }
    }

    private fun readFromAssets(): String? {
        return try {
            context.assets.open(COLORS_FILE_NAME).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e(TAG, "readFromAssets:", e)
            null
        }
    }

    companion object {
        private const val COLORS_FILE_NAME = "github_colors.json"
        private val TAG = ColorPicker::class.java.simpleName
    }
}