package iti.intake40.workmanager_notification_demo.util

import android.R.id.message
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.view.activites.SubscribeActivity


private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(msg: String, appContext: Context) {
    val contentIntent = Intent(appContext, SubscribeActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val image = BitmapFactory.decodeResource(
        appContext.resources,
        R.drawable.heart
    )

    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val builder = NotificationCompat.Builder(appContext, msg)

        .setSmallIcon(R.drawable.heart)
        .setContentTitle("New Update")
        .setContentText(msg)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(image)
        .addAction(
            R.drawable.heart,
           "Show Details",
            contentPendingIntent
        )
        .setSound(soundUri)
        .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}


