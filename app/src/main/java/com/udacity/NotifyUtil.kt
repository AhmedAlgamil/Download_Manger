package com.udacity
/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.NotificationCompat

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationManagerCompat
import com.udacity.Constatns.Companion.NOTIFICATION_ID
import com.udacity.Constatns.Companion.NOTIFICATION_NAME


// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param messageBody, notification text.
 * @param context, activity context.
 */
// Notification ID.

private val REQUEST_CODE = 0
private val FLAGS = 0
fun NotificationManager.sendNotification(messageBody: String,title: String, applicationContext: Context) {
    var builder = NotificationCompat.Builder(applicationContext, "${NOTIFICATION_ID}")
        .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
        .setContentTitle(title)
        .setContentText(messageBody)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(applicationContext)) {
        // notificationId is a unique int for each notification that you must define
        notify(NOTIFICATION_ID, builder.build())
    }

}

fun NotificationManager.completedNotification(messageBody: String,title: String,fileName :String, applicationContext: Context) {
    val statusCompletedIntent = Intent(applicationContext, DetailActivity::class.java)
    statusCompletedIntent.putExtra("status","Completed")
    statusCompletedIntent.putExtra("filename",fileName)
    val statusCompletedPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        statusCompletedIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    var builder = NotificationCompat.Builder(applicationContext, "${NOTIFICATION_ID}")
        .setSmallIcon(R.drawable.ic_baseline_insert_drive_file_24)
        .setContentTitle(title)
        .setContentText(messageBody)
        .addAction(
            R.drawable.ic_baseline_insert_drive_file_24,
            applicationContext.getString(R.string.view_details),
            statusCompletedPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(applicationContext)) {
        // notificationId is a unique int for each notification that you must define
        notify(NOTIFICATION_ID, builder.build())
    }

}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}