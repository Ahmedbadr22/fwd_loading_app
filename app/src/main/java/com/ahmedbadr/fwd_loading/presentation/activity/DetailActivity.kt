package com.ahmedbadr.fwd_loading.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahmedbadr.fwd_loading.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val DOWNLOAD_STATUS = "download_status"
        const val DOWNLOAD_FILENAME = "download_fileName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        binding.apply {
            contentDetail.apply {
                textViewFileName.text = intent.getStringExtra(DOWNLOAD_FILENAME)
                textViewStatus.text = intent.getStringExtra(DOWNLOAD_STATUS)
            }

            constraintMotionLayout.transitionToEnd()
        }

    }

}
