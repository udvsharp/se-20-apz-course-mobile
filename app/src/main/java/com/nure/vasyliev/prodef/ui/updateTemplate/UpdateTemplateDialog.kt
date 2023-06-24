package com.nure.vasyliev.prodef.ui.updateTemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentUpdateTemplateBinding
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository
import com.nure.vasyliev.prodef.ui.createTemplate.CreateTemplateDialog
import com.nure.vasyliev.prodef.utils.setNavResult

class UpdateTemplateDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUpdateTemplateBinding

    private lateinit var updateTemplateViewModel: UpdateTemplateViewModel

    private val args: UpdateTemplateDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val templateRepository = TemplateRepository()

        val updateTemplateViewModelFactory = UpdateTemplateViewModelFactory(
            templateRepository
        )

        updateTemplateViewModel = ViewModelProvider(
            this,
            updateTemplateViewModelFactory
        )[UpdateTemplateViewModel::class.java]

        binding = FragmentUpdateTemplateBinding.inflate(inflater, container, false)
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
            updateTemplateViewModel.updateTemplate(args.templateId, taskName, durationMins)
        }

        setupObservers()
    }

    private fun setupObservers() {
        updateTemplateViewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                setNavResult(R.id.fromTemplateDialog, CreateTemplateDialog.RESULT, it)
                findNavController().navigateUp()
            }
        }
        updateTemplateViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        updateTemplateViewModel.taskNameError.observe(viewLifecycleOwner) {
            binding.etNameContainer.error = requireContext().getString(R.string.task_name_error)
        }
        updateTemplateViewModel.durationError.observe(viewLifecycleOwner) {
            binding.etDurationContainer.error = requireContext().getString(R.string.duration_error)
        }
    }
}