package com.example.top10downloader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class AsyncTaskCoroutine<I, O> {
    var result: O? = null
    var isCancel : Boolean = false
    fun onPreExecute() {}
    abstract fun doInBackground(vararg url: I): O
    open fun onPostExecute(result: O?) {}

    fun <T> execute(vararg url: I) {
        GlobalScope.launch(Dispatchers.Main) {
            onPreExecute()
            callAsync(*url)
        }
    }
    private suspend fun callAsync(vararg url: I) {

        GlobalScope.async(Dispatchers.Default) {
            result = doInBackground(*url)
        }.await()

        GlobalScope.launch(Dispatchers.Main) {
            onPostExecute(result)
        }

    }
}