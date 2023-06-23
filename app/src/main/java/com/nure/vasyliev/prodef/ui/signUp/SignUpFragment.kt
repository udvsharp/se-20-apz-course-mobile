package com.nure.vasyliev.prodef.ui.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentSignUpBinding
import com.nure.vasyliev.prodef.rest.repositories.AuthRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private lateinit var signUphViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val authRepository = AuthRepository()

        val signUpViewModelFactory = SignUpViewModelFactory(
            sharedPrefsRepository,
            authRepository
        )

        signUphViewModel = ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle(R.string.title_sign_up)

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            signUphViewModel.register(name, username, email, password)
        }

        setupObservers()
    }

    private fun setupObservers() {
        signUphViewModel.token.observe(viewLifecycleOwner) {
            val toPomodoroFragment = SignUpFragmentDirections.toPomodoroFragment()
            findNavController().navigate(toPomodoroFragment)
        }
        signUphViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.sign_up_error),
                Toast.LENGTH_SHORT
            ).show()
        }
        signUphViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.etNameContainer.isEnabled = !isLoading
            binding.etUsernameContainer.isEnabled = !isLoading
            binding.etEmailContainer.isEnabled = !isLoading
            binding.etPasswordContainer.isEnabled = !isLoading
            binding.btnSignUp.isEnabled = !isLoading
        }
    }
}