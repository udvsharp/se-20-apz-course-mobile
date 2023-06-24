package com.nure.vasyliev.prodef.ui.createTemplate

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
import com.nure.vasyliev.prodef.databinding.FragmentCreateTemplateBinding
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository
import com.nure.vasyliev.prodef.utils.setNavResult

class CreateTemplateDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateTemplateBinding

    private lateinit var createTemplateViewModel: CreateTemplateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val templateRepository = TemplateRepository()

        val createTemplateViewModelFactory = CreateTemplateViewModelFactory(
            sharedPrefsRepository,
            templateRepository
        )

        createTemplateViewModel = ViewModelProvider(
            this,
            createTemplateViewModelFactory
        )[CreateTemplateViewModel::class.java]

        binding = FragmentCreateTemplateBinding.inflate(inflater, container, false)
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

        binding.btnCreateTemplate.setOnClickListener {
            val taskName = binding.etTaskName.text.toString()
            val durationMins = binding.etDuration.text.toString()
            createTemplateViewModel.createTemplate(taskName, durationMins)
        }

        setupObservers()
    }

    private fun setupObservers() {
        createTemplateViewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                setNavResult(R.id.fromTemplateDialog, RESULT, it)
                findNavController().navigateUp()
            }
        }
        createTemplateViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        createTemplateViewModel.taskNameError.observe(viewLifecycleOwner) {
            binding.etNameContainer.error = requireContext().getString(R.string.task_name_error)
        }
        createTemplateViewModel.durationError.observe(viewLifecycleOwner) {
            binding.etDurationContainer.error = requireContext().getString(R.string.duration_error)
        }
    }

    companion object {
        const val RESULT = "is_template_created"
    }
}