package com.android.boilerplate.aide.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.boilerplate.R
import com.android.boilerplate.aide.utils.NotificationUtils
import com.android.boilerplate.model.data.local.preference.Preferences

/**
 * @author Abdul Rahman
 */
class PeriodicWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val preferences = Preferences(applicationContext)
        return if (preferences.getBoolean(Preferences.KEY_NOTIFICATION)) {
            NotificationUtils.sendNotification(
                applicationContext,
                applicationContext.getString(R.string.notification),
                applicationContext.getString(R.string.notification_text)
            )
            Result.success()
        } else {
            PeriodicWorkerUtils.cancelPeriodicWorker(applicationContext)
            Result.failure()
        }
    }
}