package com.nure.vasyliev.prodef.ui.pomodoro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentPomodoroBinding
import com.nure.vasyliev.prodef.service.PomodoroService
import com.nure.vasyliev.prodef.service.PomodoroSharedPrefs
import com.nure.vasyliev.prodef.utils.MILLIS_IN_SECOND
import com.nure.vasyliev.prodef.utils.toMmSsFormat

class PomodoroFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroBinding

    private lateinit var pomodoroViewModel: PomodoroViewModel

    private lateinit var appContext: Context

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val pomodoroSharedPrefs = PomodoroSharedPrefs(requireContext())

        val pomodoroViewModelFactory = PomodoroViewModelFactory(
            pomodoroSharedPrefs
        )

        pomodoroViewModel = ViewModelProvider(this, pomodoroViewModelFactory)[PomodoroViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContext = requireContext().applicationContext

        setupObservers()
    }

    private fun setupObservers() {
        pomodoroViewModel.millis.observe(viewLifecycleOwner) { millis ->
            binding.tvTimer.text = millis.toMmSsFormat()
            binding.cpiTimerProgress.progress = (millis / MILLIS_IN_SECOND).toInt()
        }
        pomodoroViewModel.isStarted.observe(viewLifecycleOwner) { isStarted ->
            if (isStarted) {
                binding.ivPlayController.setImageResource(R.drawable.ic_stop)
                binding.ivPlayController.setOnClickListener {
                    val intent = Intent(PomodoroService.SEND_STOP)
                    appContext.sendBroadcast(intent)
                }
            } else {
                binding.ivPlayController.setImageResource(R.drawable.ic_play)
                binding.ivPlayController.setOnClickListener {
                    val seconds = 10L
                    val intent = PomodoroService.newIntent(requireContext(), seconds)
                    ContextCompat.startForegroundService(appContext,intent)
                    pomodoroViewModel.updateMaxSeconds(seconds)
                    binding.cpiTimerProgress.max = seconds.toInt()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        appContext.registerReceiver(millisTimerReceiver, IntentFilter(PomodoroService.SEND_MILLIS))
        appContext.registerReceiver(startedTimerReceiver, IntentFilter(PomodoroService.SEND_STARTED))
    }

    override fun onResume() {
        super.onResume()
        val maxSeconds = pomodoroViewModel.maxSeconds.value?.toInt() ?: 0
        binding.cpiTimerProgress.max = maxSeconds
        Log.d("TAG", maxSeconds.toString())
    }

    override fun onStop() {
        super.onStop()
        appContext.unregisterReceiver(millisTimerReceiver)
        appContext.unregisterReceiver(startedTimerReceiver)
    }

    private val millisTimerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val millis = intent?.getLongExtra(PomodoroService.MILLIS, 0L)
            millis?.let {
                pomodoroViewModel.updateMillis(it)
            }
        }
    }

    private val startedTimerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isStarted = intent?.getBooleanExtra(PomodoroService.IS_STARTED, false)
            isStarted?.let {
                pomodoroViewModel.updateStarted(it)
            }
        }
    }
}