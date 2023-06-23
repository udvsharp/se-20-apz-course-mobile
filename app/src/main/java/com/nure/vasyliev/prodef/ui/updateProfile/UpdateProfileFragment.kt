package com.nure.vasyliev.prodef.ui.updateProfile

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
import com.nure.vasyliev.prodef.databinding.FragmentUpdateProfileBinding
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository

class UpdateProfileFragment : Fragment() {

    private lateinit var binding: FragmentUpdateProfileBinding

    private lateinit var updateProfileViewModel: UpdateProfileViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val userRepository = UserRepository()

        val updateProfileViewModelFactory = UpdateProfileViewModelFactory(
            sharedPrefsRepository,
            userRepository
        )

        updateProfileViewModel =
            ViewModelProvider(this, updateProfileViewModelFactory)[UpdateProfileViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle(R.string.title_update_profile)

        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.doOnTextChanged { _, _, _, _ ->
            binding.etNameContainer.error = null
        }

        binding.etUsername.doOnTextChanged { _, _, _, _ ->
            binding.etUsernameContainer.error = null
        }

        binding.etEmail.doOnTextChanged { _, _, _, _ ->
            binding.etEmailContainer.error = null
        }

        binding.btnUpdateProfile.setOnClickListener {
            val name = binding.etName.text.toString()
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            updateProfileViewModel.updateUser(name, username, email)
        }

        setupObservers()
    }

    private fun setupObservers() {
        updateProfileViewModel.user.observe(viewLifecycleOwner) {
            it?.let { user ->
                binding.etName.setText(user.name)
                binding.etUsername.setText(user.username)
                binding.etEmail.setText(user.email)
            }
        }
        updateProfileViewModel.error.observe(viewLifecycleOwner) { error ->
            when(error) {
                UpdateUserError.NameError -> {
                    binding.etNameContainer.error = requireContext().getString(R.string.name_error)
                }
                UpdateUserError.UsernameError -> {
                    binding.etUsernameContainer.error = requireContext().getString(R.string.username_error)
                }
                UpdateUserError.EmailError -> {
                    binding.etEmailContainer.error = requireContext().getString(R.string.email_error)
                }
            }
        }
        updateProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnUpdateProfile.isEnabled = !isLoading
        }
        updateProfileViewModel.iSuccess.observe(viewLifecycleOwner) {
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