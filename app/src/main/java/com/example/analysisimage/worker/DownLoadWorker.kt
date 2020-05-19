package com.example.analysisimage.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class DownLoadWorker(appContext: Context,workerParams: WorkerParameters): Worker(appContext,workerParams) {

    override fun doWork(): Result {
        inputData.getString("url")
        val resultData = workDataOf("result" to "结果")
        setProgressAsync(workDataOf("progress" to 100))
        return Result.success(resultData)
    }

}