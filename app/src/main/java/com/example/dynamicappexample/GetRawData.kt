package com.example.dynamicappexample

import android.util.Log
import com.example.top10downloader.AsyncTaskCoroutine
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}

class GetRawData(private val listener: OnDownloadComplete) : AsyncTaskCoroutine<String, String>() {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    // private var listener: MainActivity? = null
    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

    override fun doInBackground(vararg url: String): String {
        if (url[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL Specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(url[0]).readText()
        } catch (e: Exception) {
            var errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }

                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data ${e.message}"
                }

                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "doInBackground: Security exception : Need Permission? ${e.message}"
                }

                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Unknown Error ${e.message}"

                }

            }
            Log.e(TAG, errorMessage)
            return errorMessage

        }

    }

    //fun setOnDownloadCompleteListener(callbackobject: MainActivity){
//    listener = callbackobject
//}
    override fun onPostExecute(result: String?) {
        Log.d(TAG, "onPostExecute called")
        if (result != null) {
            listener.onDownloadComplete(result, downloadStatus)
        }
    }
}