package com.ahmedbadr.fwd_loading.presentation.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedbadr.fwd_loading.R
import com.ahmedbadr.fwd_loading.app.utils.sendNotification

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var downloadID: Long = 0

    val selectedRadioButton = MutableLiveData<Int>()
    val downloadFinished = MutableLiveData(false)

    @SuppressLint("StaticFieldLeak")
    private val applicationContext = getApplication<Application>().applicationContext

    fun download() {
        val url : String? = when(selectedRadioButton.value) {
            R.id.radioButtonGlide -> applicationContext.getString(R.string.glide_download_link)
            R.id.radioButtonLoadApp -> applicationContext.getString(R.string.app_download_link)
            R.id.radioButtonRetrofit -> applicationContext.getString(R.string.retrofit_download_link)
            else -> {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.please_choose_option_to_download), Toast.LENGTH_SHORT).show()
                null
            }
        }

        url?.let {
            val request = DownloadManager.Request(Uri.parse(it))
                .setTitle(applicationContext.getString(R.string.app_name))
                .setDescription(applicationContext.getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = applicationContext.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)
        }
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val message : String = when(selectedRadioButton.value) {
                R.id.radioButtonGlide -> applicationContext.getString(R.string.glide_download_message)
                R.id.radioButtonLoadApp -> applicationContext.getString(R.string.app_download_message)
                R.id.radioButtonRetrofit -> applicationContext.getString(R.string.retrofit_download_message)
                else -> ""
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Toast.makeText(applicationContext, "Android Version Should be O or higher", Toast.LENGTH_LONG).show()
                return
            }

            if (id != downloadID) {
                sendNotification("$message Downloaded Fail", "Fail", message)
                return
            }

            downloadFinished.value = true
            sendNotification("$message Downloaded Successfully", "Success", message)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(message: String, status: String, fileName: String) {
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(message, status, fileName, applicationContext)
    }
}

class MainViewModelFactory (private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}