package com.nure.vasyliev.prodef.ui.createPomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentCreateSessionBinding
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.utils.setNavResult

class CreateSessionDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateSessionBinding

    private lateinit var createSessionViewModel: CreateSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val pomodoroRepository = PomodoroRepository()

        val createSessionViewModelFactory = CreateSessionViewModelFactory(
            sharedPrefsRepository,
            pomodoroRepository
        )

        createSessionViewModel = ViewModelProvider(
            this,
            createSessionViewModelFactory
        )[CreateSessionViewModel::class.java]

        binding = FragmentCreateSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etTaskName.doOnTextChanged { _, _, _, _ ->
            binding.etNameContainer.error = null
        }

        binding.etDuration.doOnTextChanged { _, _, _, _ ->
            binding.etDurationContainer.error = null
        }

        binding.btnCreateSession.setOnClickListener {
            val taskName = binding.etTaskName.text.toString()
            val durationMins = binding.etDuration.text.toString()
            createSessionViewModel.createPomodoro(taskName, durationMins)
        }

        setupObservers()
    }

    private fun setupObservers() {
        createSessionViewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                setNavResult(R.id.sessionsFragment, RESULT, it)
                findNavController().navigateUp()
            }
        }
        createSessionViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        createSessionViewModel.taskNameError.observe(viewLifecycleOwner) {
            binding.etNameContainer.error = requireContext().getString(R.string.task_name_error)
        }
        createSessionViewModel.durationError.observe(viewLifecycleOwner) {
            binding.etDurationContainer.error = requireContext().getString(R.string.duration_error)
        }
    }

    companion object {
        const val RESULT = "is_session_created"
    }
}