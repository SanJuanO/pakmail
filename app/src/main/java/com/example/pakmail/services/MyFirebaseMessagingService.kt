package com.example.pakmail

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService: FirebaseMessagingService() {
        val FCM_PARAM = "picture"
        private val CHANNEL_NAME = "FCM"
        private val CHANNEL_DESC = "Firebase Cloud Messaging"
        private var numMessages = 0

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onMessageReceived(remoteMessage: RemoteMessage) {
                super.onMessageReceived(remoteMessage)
                val notification = remoteMessage.notification
                val data = remoteMessage.data
                Log.d("FROM", remoteMessage.from)
                sendNotification(notification, data)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun sendNotification(notification: RemoteMessage.Notification?, data: Map<String, String>) {
val guia =  notification!!.title.toString()
                val folio = guia.substring(29,39)

                val bundle = Bundle()
                bundle.putString(FCM_PARAM, data[FCM_PARAM])
                val intent = Intent(this, Detalle::class.java)
                intent.putExtras(bundle)
                intent.putExtra("folio", folio)
                intent.putExtra("escaneo", "false")

                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val notificationBuilder = NotificationCompat.Builder(this,"Pakmail")
                        .setContentTitle(notification!!.title)
                        .setContentText(notification.body)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                        .setContentIntent(pendingIntent)
                        .setContentInfo("Pakmail")
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.id.icon))
                        .setColor(getColor(R.color.white))
                        .setLights(Color.RED, 1000, 300)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setNumber(++numMessages)
                        .setSmallIcon(R.drawable.ic_popup_reminder)
                try {
                        val picture = data[FCM_PARAM]
                        if (picture != null && "" != picture) {
                                val url = URL(picture)
                                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                                notificationBuilder.setStyle(
                                        NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.body)
                                )
                        }
                } catch (e: IOException) {
                        e.printStackTrace()
                }
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel("Pakmail", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                        )
                        channel.description = CHANNEL_DESC
                        channel.setShowBadge(true)
                        channel.canShowBadge()
                        channel.enableLights(true)
                        channel.lightColor = Color.RED
                        channel.enableVibration(true)
                        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                        assert(notificationManager != null)
                        notificationManager.createNotificationChannel(channel)
                }
                assert(notificationManager != null)
                notificationManager.notify(0, notificationBuilder.build())
        }
}