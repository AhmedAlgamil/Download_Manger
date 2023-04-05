package com.udacity

import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionScene
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var scene: MotionScene
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val extras: Bundle? = intent.extras
        var status: String = ""
        var fileName: String = ""
        if (savedInstanceState == null) {
            if (extras == null) {
                status = ""
                fileName = ""
            } else {
                status = extras.getString("status")!!
                fileName = extras.getString("filename")!!
            }
        } else {
            status = savedInstanceState.getSerializable("status").toString()
            fileName = savedInstanceState.getSerializable("filename").toString()
        }
        textView_status.text = status
        textView_file_name.text = fileName

        btn_back.setOnClickListener {
            this.finish()
        }

    }


}
