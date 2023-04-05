package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.udacity.Constatns.Companion.NOTIFICATION_ID
import com.udacity.Constatns.Companion.NOTIFICATION_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {


    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager
    private lateinit var fileName: String
    lateinit var loadingButton: LoadingButton
    lateinit var c: Cursor
    lateinit var query: DownloadManager.Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel("$NOTIFICATION_ID", NOTIFICATION_NAME)

        rb_glide_library.setOnClickListener {
            fileName = "glideMaster.zip"
            isSelected = true
        }

        rb_loading_app_library.setOnClickListener {
            fileName = "LoadingAppMaster.zip"
            isSelected = true
        }

        rb_retrofit_library.setOnClickListener {
            fileName = "RetrofitMaster.zip"
            isSelected = true
        }

        loadingButton = findViewById(R.id.custom_button)

        loadingButton.setOnClickListener {
            isDownloaded = false
            if (!isSelected) {
                Toast.makeText(
                    applicationContext,
                    "You must Select one of these libraries",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (rb_glide_library.isChecked) {
                    downloadFile(fileName, "from github ", URL)
                } else if (rb_loading_app_library.isChecked) {
                    downloadFile(fileName, "from github ", URL2)
                } else if (rb_retrofit_library.isChecked) {
                    downloadFile(fileName, "from github ", URL3)
                }
            }

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                notificationManager = getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.completedNotification(
                    getString(R.string.downloaded),
                    getString(R.string.app_name),
                    fileName,
                    applicationContext
                )
                loadingButton.isCompleted()
            }

        }
    }


    private fun downloadFile(fileName: String, desc: String, url: String) {
        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(desc)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)

        notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.sendNotification(
            "downloading.......",
            getString(R.string.app_name),
            applicationContext
        )
    }

    companion object {
        private const val URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val URL2 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL3 =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channelId"

        public var downloadID: Long = 0
        public var isSelected: Boolean = false
        public var isDownloaded = false
    }


    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )
                // TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.downloading_notification_channel_description)

            val notificationManager = application.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)

        }

        // TODO: Step 1.6 END create channel
    }


}
