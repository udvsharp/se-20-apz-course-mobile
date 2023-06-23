package com.nure.vasyliev.prodef.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentProfileBinding
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.UserRepository

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val userRepository = UserRepository()

        val profileViewModelFactory = ProfileViewModelFactory(
            sharedPrefsRepository,
            userRepository
        )

        profileViewModel =
            ViewModelProvider(this, profileViewModelFactory)[ProfileViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setTitle(R.string.title_profile)

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdateProfile.setOnClickListener {
            val toUpdateProfileFragment = ProfileFragmentDirections.toUpdateProfileFragment()
            findNavController().navigate(toUpdateProfileFragment)
        }

        binding.btnUpdatePassword.setOnClickListener {
            val toUpdatePasswordFragment = ProfileFragmentDirections.toUpdatePasswordFragment()
            findNavController().navigate(toUpdatePasswordFragment)
        }

        setupObservers()
    }

    private fun setupObservers() {
        profileViewModel.user.observe(viewLifecycleOwner) {
            it?.let { user ->
                binding.name.text = user.name
                binding.username.text = user.username
                binding.email.text = user.email
            }
        }
        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnUpdateProfile.isEnabled = !isLoading
        }
    }
}