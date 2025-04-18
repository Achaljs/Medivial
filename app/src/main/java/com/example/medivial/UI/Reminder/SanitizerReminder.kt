package com.example.medivial.UI.Reminder

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.medivial.Notification.*
import com.example.medivial.R
import com.example.medivial.Service.SanitizeService
import com.example.medivial.Utils.Constant
import com.example.medivial.Utils.Constant.sanitizerchannelid
import com.example.medivial.databinding.FragmentReminderBinding
import java.time.*
import java.util.*

class SanitizerReminder : AppCompatActivity() {
    private lateinit var binding: FragmentReminderBinding
    private  var startTime:Long=32400000
    private  var endTime:Long=79200000
    private  var start="9:00 AM"
    private  var end="10:00 PM"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotification()
        startTime=Constant.loadData(this, "Sanitizerreminder", "startM","32400000")!!.toLong()
        endTime=Constant.loadData(this, "Sanitizerreminder", "endM","79200000")!!.toLong()
        binding.interval.minValue = 1
        binding.interval.maxValue = 24
        UI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        val name = "Sanitize"
        val message = "message"
        val important = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(sanitizerchannelid, name, important)
        channel.description = message
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun UI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.backward.setOnClickListener {
            finish()
        }
        binding.textView8.text="Set Reminder To Sanitize Your Hand"
        binding.textView6.text="Hand Sanitize reminder"
        binding.textView7.text="Hand sanitize reminders help you to maintain good hygiene."
        val Interval = Constant.loadData(this, "Sanitizerreminder", "interval", "1")
        binding.interval.value = Interval!!.toInt()
        binding.start.setText(Constant.loadData(this,"Sanitizerreminder","start","09:00AM"))
        binding.end.setText(Constant.loadData(this,"Sanitizerreminder","end","10:00 PM"))
        var check = Constant.loadData(this, "Sanitizerreminder", "check", "0")
        if (check == "1") {
            binding.switchMaterial.isChecked = true
            binding.switchMaterial.thumbTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_orange))
        } else {
            binding.switchMaterial.thumbTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_grey))
        }
        binding.switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.switchMaterial.thumbTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_orange))
                check="1"
            } else {
                binding.switchMaterial.thumbTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_grey))
                check="0"
            }
        }
        binding.start.setOnClickListener{
            Constant.showTimePicker(binding.start,this,object : Constant.OnTimeSelectedListener {
                override fun onTimeSelected(timeInMillis: Long,Time:String) {
                    startTime=timeInMillis
                    start=Time
                }
            })

        }
        binding.end.setOnClickListener{
            Constant.showTimePicker(binding.end,this,object : Constant.OnTimeSelectedListener {
                override fun onTimeSelected(timeInMillis: Long,Time:String) {
                    endTime=timeInMillis
                    end=Time
                }
            })

        }
        val jobScheduler=this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        binding.save.setOnClickListener{
            if(check=="1")
            {
                Constant.savedata(this, "Sanitizerreminder", "check", "1")
                Constant.savedata(applicationContext, "Sanitizerreminder", "start", start)
                Constant.savedata(applicationContext, "Sanitizerreminder", "end",end)
                Constant.savedata(applicationContext, "Sanitizerreminder", "startM",startTime.toString())
                Constant.savedata(applicationContext, "Sanitizerreminder", "endM",endTime.toString())
                Constant.savedata(this, "Sanitizerreminder", "interval", (binding.interval.value).toString())
                Toast.makeText(this, "Sanitizer Reminder  set successfully", Toast.LENGTH_SHORT).show()
                val Interval = (binding.interval.value*3600000).toLong()
                val jobInfo= JobInfo.Builder(3, ComponentName(this, SanitizeService::class.java)).setMinimumLatency(Interval).build()
                jobScheduler.schedule(jobInfo)
            }else{
                Constant.savedata(this, "Sanitizerreminder", "check", "0")
                Toast.makeText(this, "Sanitizer Reminder removed successfully", Toast.LENGTH_SHORT).show()
                jobScheduler.cancel(3)
            }
        }
    }
}