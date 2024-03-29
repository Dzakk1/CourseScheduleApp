package com.dicoding.courseschedule.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.ID_REPEATING
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_ID
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_NAME
import com.dicoding.courseschedule.util.NOTIFICATION_ID
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = DataRepository.getInstance(context)
            val courses = repository?.getTodaySchedule()

            courses?.let {
                if (it.isNotEmpty()) showNotification(context, it)
            }
        }
    }

    //TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
    fun setDailyReminder(context: Context) {

        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dailyReminder = Intent(context, DailyReminder::class.java)
        val pending = PendingIntent.getBroadcast(context, ID_REPEATING, dailyReminder, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val settingCalendar = Calendar.getInstance()

        settingCalendar.set(Calendar.SECOND, 0)
        settingCalendar.set(Calendar.MINUTE, 0)
        settingCalendar.set(Calendar.HOUR_OF_DAY, 6)

        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            settingCalendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pending
        )
    }

    fun cancelAlarm(context: Context) {

        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dailyReminder = Intent(context, DailyReminder::class.java)
        val pending = PendingIntent.getBroadcast(context, ID_REPEATING, dailyReminder, PendingIntent.FLAG_IMMUTABLE)

        manager.cancel(pending)

    }

    private fun showNotification(context: Context, content: List<Course>) {
        //TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationStyle = NotificationCompat.InboxStyle()
        val timeString = context.resources.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
            notificationStyle.addLine(courseData)
        }


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val home = Intent(context, HomeActivity::class.java)
        val pending = PendingIntent.getActivity(context, 0, home, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setStyle(notificationStyle)
            .setContentTitle(context.getString(R.string.title_activity_list))
            .setContentText(context.getString(R.string.notification_message_format))
            .setContentIntent(pending)
            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            manager.createNotificationChannel(notificationChannel)
        }

        manager.notify(NOTIFICATION_ID, notification)
    }
}