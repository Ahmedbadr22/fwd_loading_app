package com.ahmedbadr.fwd_loading.app.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.ahmedbadr.fwd_loading.R
import com.ahmedbadr.fwd_loading.presentation.activity.DetailActivity

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(
    messageBody: String,
    downloadingStatus: String,
    downloadingFileName: String,
    applicationContext: Context
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        putExtra(DetailActivity.DOWNLOAD_FILENAME, downloadingFileName)
        putExtra(DetailActivity.DOWNLOAD_STATUS, downloadingStatus)
    }

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = Notification.Builder(
        applicationContext,
        applicationContext.getString(R.string.downloading_notification_channel_id)
    ).apply {
        setSmallIcon(R.drawable.ic_launcher_foreground)
        setContentTitle(applicationContext.getString(R.string.downloading_finish))
        setContentText(messageBody)
        setContentIntent(pendingIntent)
        setAutoCancel(true)
    }

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}