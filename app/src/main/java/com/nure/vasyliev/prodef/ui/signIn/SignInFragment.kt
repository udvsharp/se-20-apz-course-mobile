package com.nure.vasyliev.prodef.ui.signIn

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
import com.nure.vasyliev.prodef.databinding.FragmentSignInBinding
import com.nure.vasyliev.prodef.rest.repositories.AuthRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private lateinit var signInhViewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val authRepository = AuthRepository()

        val signInViewModelFactory = SignInViewModelFactory(
            sharedPrefsRepository,
            authRepository
        )

        signInhViewModel = ViewModelProvider(this, signInViewModelFactory)[SignInViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignUp.setOnClickListener {
            val toSignUpFragment = SignInFragmentDirections.toSignUpFragment()
            findNavController().navigate(toSignUpFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            signInhViewModel.signIn(email, password)
        }

        setupObservers()
    }

    private fun setupObservers() {
        signInhViewModel.token.observe(viewLifecycleOwner) {
            val toPomodoroFragment = SignInFragmentDirections.toPomodoroFragment()
            findNavController().navigate(toPomodoroFragment)
        }
        signInhViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.sign_in_error),
                Toast.LENGTH_SHORT
            ).show()
        }
        signInhViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.etEmailContainer.isEnabled = !isLoading
            binding.etPasswordContainer.isEnabled = !isLoading
            binding.btnSignIn.isEnabled = !isLoading
        }
    }
}