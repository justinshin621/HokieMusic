package com.example.hw4


import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject
import retrofit2.Call


class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload user interaction history.

        val json = JSONObject()
        json.accumulate("date",inputData.getString("date"))
        json.accumulate("userID",inputData.getString("username"))
        json.accumulate("event",inputData.getString("event"))


        Log.d(playing.TAG, "params:"+json.toString()+ " url "+playing.URL)
        return   uploadLog(json, playing.URL)
    }

    fun uploadLog(json: JSONObject, url: String): Result {
        Log.d(playing.TAG, "uploadLog() "+url)
        var call : Call<String> =
            TrackerRetrofitService.create(url).postLog(json)

        var response = call.execute()

        if(response.isSuccessful){
            return Result.success()

        }
        else{
            if (response.code() in (500..599)) {
                // try again if there is a server error
                return Result.retry()
            }
            Log.d(playing.TAG, "response is:  "+response)
            return Result.failure()
        }

    }
}