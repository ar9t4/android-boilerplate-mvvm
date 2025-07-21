package com.android.boilerplate.aide.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.boilerplate.R
import com.android.boilerplate.view.splash.SplashActivity

/**
 * @author Abdul Rahman
 */
object NotificationUtils {

    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(context: Context, title: String, text: String) {
        val lightColor = ContextCompat.getColor(context, R.color.green_65C078)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.packageName,
                "Default",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.apply {
                enableLights(true)
                this.lightColor = lightColor
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(context, SplashActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(context, context.packageName)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setLights(lightColor, 1000, 2000)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }
}