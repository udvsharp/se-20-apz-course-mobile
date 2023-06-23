package com.nure.vasyliev.prodef.ui.updatePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentUpdatePasswordBinding
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository

class UpdatePasswordFragment : Fragment() {

    private lateinit var binding: FragmentUpdatePasswordBinding

    private lateinit var updatePasswordViewModel: UpdatePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val userRepository = UserRepository()

        val updatePasswordViewModelFactory = UpdatePasswordViewModelFactory(
            sharedPrefsRepository,
            userRepository
        )

        updatePasswordViewModel =
            ViewModelProvider(this, updatePasswordViewModelFactory)[UpdatePasswordViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle(R.string.title_update_password)

        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            binding.etPasswordContainer.error = null
        }

        binding.btnUpdatePassword.setOnClickListener {
            val password = binding.etPassword.text.toString()
            updatePasswordViewModel.updatePassword(password)
        }

        setupObservers()
    }

    private fun setupObservers() {
        updatePasswordViewModel.error.observe(viewLifecycleOwner) {
            binding.etPasswordContainer.error = requireContext().getString(R.string.password_error)
        }
        updatePasswordViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnUpdatePassword.isEnabled = !isLoading
        }
        updatePasswordViewModel.iSuccess.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.something_went_wrong,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}