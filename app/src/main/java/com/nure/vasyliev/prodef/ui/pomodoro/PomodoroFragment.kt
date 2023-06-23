package com.nure.vasyliev.prodef.ui.pomodoro

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentPomodoroBinding
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.service.PomodoroService
import com.nure.vasyliev.prodef.service.PomodoroSharedPrefs
import com.nure.vasyliev.prodef.utils.MILLIS_IN_MINUTE
import com.nure.vasyliev.prodef.utils.MILLIS_IN_SECOND
import com.nure.vasyliev.prodef.utils.SECONDS_IN_MINUTE
import com.nure.vasyliev.prodef.utils.toMmSsFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PomodoroFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroBinding

    private lateinit var pomodoroViewModel: PomodoroViewModel

    private lateinit var appContext: Context

    private var isBounded: Boolean = false

    private val supervisor = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + supervisor)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val pomodoroRepository = PomodoroRepository()
        val pomodoroSharedPrefs = PomodoroSharedPrefs(requireContext())

        val pomodoroViewModelFactory = PomodoroViewModelFactory(
            sharedPrefsRepository,
            pomodoroRepository,
            pomodoroSharedPrefs
        )

        pomodoroViewModel =
            ViewModelProvider(this, pomodoroViewModelFactory)[PomodoroViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setTitle(R.string.title_pomodoro)

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        appContext = requireContext().applicationContext

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivPlayController.isEnabled = false
        binding.layoutTask.layoutPeriod.isVisible = false

        setupObservers()
    }

    private fun setupObservers() {
        pomodoroViewModel.millis.observe(viewLifecycleOwner) { millis ->
            binding.tvTimer.text = millis.toMmSsFormat()
            binding.cpiTimerProgress.progress = (millis / MILLIS_IN_SECOND).toInt()
        }
        pomodoroViewModel.isStarted.observe(viewLifecycleOwner) { isStarted ->
            if (isStarted) {
                isBounded = appContext.bindService(
                    Intent(appContext, PomodoroService::class.java),
                    pomodoroConnection,
                    Context.BIND_AUTO_CREATE
                )
                binding.ivPlayController.setImageResource(R.drawable.ic_stop)
                binding.ivPlayController.setOnClickListener {
                    val intent = Intent(PomodoroService.SEND_STOP)
                    appContext.sendBroadcast(intent)
                    coroutineScope.launch {
                        delay(500)
                        pomodoroViewModel.getLastValidPomodoro()
                    }
                }
            } else {
                binding.ivPlayController.setImageResource(R.drawable.ic_play)
                binding.ivPlayController.setOnClickListener {
                    startService()
                }
            }
        }
        pomodoroViewModel.validPomodoro.observe(viewLifecycleOwner) { pomodoro ->
            if (pomodoro != null) {
                binding.layoutTask.layoutPomodoro.isVisible = true
                binding.layoutTask.tvTaskName.text = pomodoro.task
                binding.layoutTask.tvDuration.text =
                    context?.getString(R.string.rv_item_duration, pomodoro.durationMins.toString())
                binding.layoutTask.tvPlanning.text = context?.getString(R.string.planned)
                binding.cpiTimerProgress.max = pomodoro.durationMins * SECONDS_IN_MINUTE.toInt()
                if (pomodoroViewModel.isStarted.value != true) {
                    binding.tvTimer.text = (pomodoro.durationMins * MILLIS_IN_MINUTE).toMmSsFormat()
                    binding.cpiTimerProgress.progress =
                        pomodoro.durationMins * SECONDS_IN_MINUTE.toInt()
                }
            } else {
                binding.ivPlayController.setOnClickListener {  }
            }
        }
        pomodoroViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.ivPlayController.isEnabled = !isLoading
        }
    }

    private fun startService() {
        val seconds = pomodoroViewModel.maxSeconds.value ?: 0L
        val taskName = pomodoroViewModel.validPomodoro.value?.task ?: ""
        val pomodoroId = pomodoroViewModel.validPomodoro.value?.id ?: ""
        val intent = PomodoroService.startIntent(requireContext(), seconds, taskName, pomodoroId)
        isBounded = appContext.bindService(
            intent,
            pomodoroConnection,
            Context.BIND_AUTO_CREATE
        )
        ContextCompat.startForegroundService(appContext, intent)
    }

    override fun onStop() {
        super.onStop()
        if (isBounded)
            appContext.unbindService(pomodoroConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        supervisor.cancelChildren()
    }

    private var pomodoroConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            val binder = (iBinder as PomodoroService.PBinder)
            coroutineScope.launch {
                binder.isStarted.collect {
                    withContext(Dispatchers.Main) {
                        pomodoroViewModel.updateStarted(it)
                    }
                }
            }
            coroutineScope.launch {
                binder.millis.collect {
                    withContext(Dispatchers.Main) {
                        pomodoroViewModel.updateMillis(it)
                    }
                }
            }
            coroutineScope.launch {
                binder.isForegroundStarted.collect {
                    withContext(Dispatchers.Main) {
                        if (!it && pomodoroViewModel.isStarted.value == true) {
                            ContextCompat.startForegroundService(
                                appContext,
                                PomodoroService.bindIntent(appContext)
                            )
                        }
                    }
                }
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {}
    }
}