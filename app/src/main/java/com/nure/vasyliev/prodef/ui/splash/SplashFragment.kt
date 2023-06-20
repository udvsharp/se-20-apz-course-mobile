package com.nure.vasyliev.prodef.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nure.vasyliev.prodef.databinding.FragmentSplashBinding
import com.nure.vasyliev.prodef.rest.TokenInterceptor
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.utils.TokenState

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val pomodoroRepository = PomodoroRepository()
        val tokenInterceptor = TokenInterceptor

        val splashViewModelFactory = SplashViewModelFactory(
            sharedPrefsRepository,
            pomodoroRepository,
            tokenInterceptor
        )

        splashViewModel = ViewModelProvider(this, splashViewModelFactory)[SplashViewModel::class.java]

        setupObservers()
    }

    private fun setupObservers() {
        splashViewModel.tokenState.observe(viewLifecycleOwner) { token ->
            when(token) {
                TokenState.WorkingJWT -> {
                    val toPomodoroFragment = SplashFragmentDirections.toPomodoroFragment()
                    findNavController().navigate(toPomodoroFragment)
                }
                TokenState.ExpiredJWT -> {
                    val toSignInFragment = SplashFragmentDirections.toSignInFragment()
                    findNavController().navigate(toSignInFragment)
                }
            }
        }
    }
}