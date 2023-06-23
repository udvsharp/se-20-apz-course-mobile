package com.nure.vasyliev.prodef.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.utils.MILLIS_IN_SECOND
import com.nure.vasyliev.prodef.utils.toMmSsFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class PomodoroService : Service() {

    private lateinit var pomodoroSharedPrefs: PomodoroSharedPrefs
    private lateinit var notificationManager: NotificationManager

    private var countDownTimer: CountDownTimer? = null

    private val _isStarted = MutableStateFlow(true)
    private val _isForegroundStarted = MutableStateFlow(false)
    private val _millis = MutableStateFlow(0L)

    private val supervisor = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + supervisor)

    override fun onCreate() {
        super.onCreate()

        pomodoroSharedPrefs = PomodoroSharedPrefs(applicationContext)
        notificationManager = getSystemService(NotificationManager::class.java)

        registerReceiver(stopReceiver, IntentFilter(SEND_STOP))

        createNotificationChannel(notificationManager)
        val notification = createNotification(0, "")
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification(0, "")
        startForeground(NOTIFICATION_ID, notification)
        _isForegroundStarted.value = true
        val seconds = intent?.getLongExtra(SECONDS, 0L) ?: 0L
        val taskName = intent?.getStringExtra(TASK_NAME) ?: ""
        val millis = seconds * MILLIS_IN_SECOND

        val isStarted = pomodoroSharedPrefs.getIsStarted()

        if (isStarted) {
            val startTime = pomodoroSharedPrefs.getStartMillis()
            val maxMillis = pomodoroSharedPrefs.getMaxMillis()
            val oldTaskName = pomodoroSharedPrefs.getTaskName()
            setTimer(oldTaskName, maxMillis - (Date().time - startTime))
        } else {
            pomodoroSharedPrefs.putIsStarted(true)
            pomodoroSharedPrefs.putTaskName(taskName)
            pomodoroSharedPrefs.putMaxMillis(millis)
            pomodoroSharedPrefs.putStartMillis(Date().time)
            setTimer(taskName, millis)
        }

        coroutineScope.launch {
            _isStarted.emit(true)
        }

        countDownTimer?.start()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setTimer(taskName: String, millis: Long) {
        countDownTimer = object : CountDownTimer(millis, MILLIS_IN_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _millis.value = millisUntilFinished
                notificationManager.notify(
                    NOTIFICATION_ID,
                    createNotification(millisUntilFinished, taskName)
                )
            }

            override fun onFinish() {
                refreshPrefsData()

                stopPomodoro()
            }
        }
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

    private fun createNotification(millis: Long, taskName: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle(taskName)
            .setContentText(millis.toMmSsFormat())
            .build()
    }

    private fun refreshPrefsData() {
        pomodoroSharedPrefs.putIsStarted(false)
        pomodoroSharedPrefs.putTaskName("")
        pomodoroSharedPrefs.putMaxMillis(0L)
        pomodoroSharedPrefs.putStartMillis(0L)
    }

    private fun stopPomodoro() {
        refreshPrefsData()

        coroutineScope.launch {
            _isStarted.emit(false)
        }
        _isForegroundStarted.value = false

        stopForeground(STOP_FOREGROUND_DETACH)
        notificationManager.cancel(NOTIFICATION_ID)

        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()

        stopForeground(STOP_FOREGROUND_DETACH)
        notificationManager.cancel(NOTIFICATION_ID)

        unregisterReceiver(stopReceiver)
    }

    private val stopReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            countDownTimer?.cancel()

            stopPomodoro()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return PBinder()
    }

    inner class PBinder : Binder() {
        val isStarted: StateFlow<Boolean> = _isStarted.asStateFlow()
        val millis: StateFlow<Long> = _millis.asStateFlow()
        val isForegroundStarted: StateFlow<Boolean> = _isForegroundStarted.asStateFlow()
    }

    companion object {
        private const val CHANNEL_NAME = "pomodoro_channel"
        private const val CHANNEL_ID = "pomodoro_channel_id"
        private const val NOTIFICATION_ID = 121

        private const val SECONDS = "seconds"
        private const val TASK_NAME = "task_name"

        const val SEND_STOP = "send_stop"

        fun startIntent(context: Context, seconds: Long, taskName: String): Intent {
            return Intent(context, PomodoroService::class.java).apply {
                putExtra(SECONDS, seconds)
                putExtra(TASK_NAME, taskName)
            }
        }

        fun bindIntent(context: Context): Intent {
            return Intent(context, PomodoroService::class.java)
        }
    }
}