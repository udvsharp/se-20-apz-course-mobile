package com.nure.vasyliev.prodef.ui.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.databinding.FragmentPomodoroBinding

class PomodoroFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val pomodoroViewModel =
                ViewModelProvider(this).get(PomodoroViewModel::class.java)

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        return binding.root
    }
}