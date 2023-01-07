package com.ahmedbadr.fwd_loading.presentation.activity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ahmedbadr.fwd_loading.R
import com.ahmedbadr.fwd_loading.data.models.ButtonState
import com.ahmedbadr.fwd_loading.databinding.ActivityMainBinding
import com.ahmedbadr.fwd_loading.presentation.viewModel.MainViewModel
import com.ahmedbadr.fwd_loading.presentation.viewModel.MainViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val mainViewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        registerReceiver(
            mainViewModel.receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        binding.contentMain.apply {
            viewModel = mainViewModel
            customButton.setOnClickListener {
                customButton.loadingButtonState = ButtonState.Loading
                mainViewModel.download()
            }

            mainViewModel.downloadFinished.observe(this@MainActivity) { isFinished ->
                if (isFinished) customButton.loadingButtonState = ButtonState.Completed
            }
        }

        createChannel(
            getString(R.string.downloading_notification_channel_id),
            getString(R.string.downloading_notification_channel_name)
        )
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = getString(R.string.downloading_notification_channel_name)
            }

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

}
