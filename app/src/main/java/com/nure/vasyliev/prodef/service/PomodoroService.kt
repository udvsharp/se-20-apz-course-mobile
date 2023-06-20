package com.nure.vasyliev.prodef.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.utils.MILLIS_IN_SECOND
import com.nure.vasyliev.prodef.utils.toMmSsFormat

class PomodoroService : Service() {

    private lateinit var pomodoroSharedPrefs: PomodoroSharedPrefs

    private var countDownTimer: CountDownTimer? = null

    private val millisIntent = Intent(SEND_MILLIS)
    private val startedIntent = Intent(SEND_STARTED)

    override fun onCreate() {
        super.onCreate()

        pomodoroSharedPrefs = PomodoroSharedPrefs(applicationContext)

        val notificationManager = getSystemService(NotificationManager::class.java)
        createNotificationChannel(notificationManager)
        startForeground(NOTIFICATION_ID, createNotification(0))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val seconds = intent?.getLongExtra(SECONDS, 0L) ?: 0L
        val millis = seconds * MILLIS_IN_SECOND

        pomodoroSharedPrefs.putMaxMillis(millis)

        val notificationManager = getSystemService(NotificationManager::class.java)

        countDownTimer = object : CountDownTimer(millis, MILLIS_IN_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                millisIntent.putExtra(MILLIS, millisUntilFinished)
                sendBroadcast(millisIntent)
                notificationManager.notify(NOTIFICATION_ID, createNotification(millisUntilFinished))
                pomodoroSharedPrefs.putMillis(millisUntilFinished)
            }

            override fun onFinish() {
                startedIntent.putExtra(IS_STARTED, false)
                sendBroadcast(startedIntent)

                stopForeground(STOP_FOREGROUND_DETACH)

                stopSelf()
            }
        }

        startedIntent.putExtra(IS_STARTED, true)
        sendBroadcast(startedIntent)

        countDownTimer?.start()

        registerReceiver(stopReceiver, IntentFilter(SEND_STOP))

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()

        pomodoroSharedPrefs.putMaxMillis(0L)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID)

        unregisterReceiver(stopReceiver)
        super.onDestroy()
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(millis: Long): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle(millis.toMmSsFormat())
            .setContentText(millis.toMmSsFormat())
            .build()
    }

    private val stopReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            countDownTimer?.cancel()

            startedIntent.putExtra(IS_STARTED, false)
            sendBroadcast(startedIntent)

            millisIntent.putExtra(MILLIS, 0L)
            sendBroadcast(millisIntent)

            stopForeground(STOP_FOREGROUND_DETACH)

            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val CHANNEL_NAME = "pomodoro_channel"
        private const val CHANNEL_ID = "pomodoro_channel_id"
        private const val NOTIFICATION_ID = 121

        private const val SECONDS = "seconds"

        const val SEND_MILLIS = "send_millis"
        const val MILLIS = "millis"

        const val SEND_STARTED = "send_started"
        const val IS_STARTED = "isStarted"

        const val SEND_STOP = "send_stop"

        fun newIntent(context: Context, seconds: Long): Intent {
            return Intent(context, PomodoroService::class.java).apply {
                putExtra(SECONDS, seconds)
            }
        }
    }
}