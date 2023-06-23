package com.nure.vasyliev.prodef.ui.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentSessionsBinding
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.ui.createPomodoro.CreateSessionDialog
import com.nure.vasyliev.prodef.utils.formatFromServer
import com.nure.vasyliev.prodef.utils.getNavResult
import com.nure.vasyliev.prodef.utils.setNavResult

class SessionsFragment : Fragment() {

    private lateinit var binding: FragmentSessionsBinding

    private lateinit var sessionViewModel: SessionViewModel

    private lateinit var sessionAdapter: SessionRecyclerViewAdapter

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, _, _ ->
            val success = getNavResult<Boolean>(R.id.sessionsFragment, CreateSessionDialog.RESULT)
            if (success == true) {
                sessionViewModel.getAllPomodoros()
                setNavResult(R.id.sessionsFragment, CreateSessionDialog.RESULT, null)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val pomodoroRepository = PomodoroRepository()

        val sessionViewModelFactory = SessionViewModelFactory(
            sharedPrefsRepository,
            pomodoroRepository
        )

        sessionViewModel =
            ViewModelProvider(this, sessionViewModelFactory)[SessionViewModel::class.java]

        sessionAdapter = SessionRecyclerViewAdapter()

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSessions.adapter = sessionAdapter

        binding.fabCreatePomodoro.setOnClickListener {
            val toCreatePomodoroDialog = SessionsFragmentDirections.toCreatePomodoroDialog()
            findNavController().navigate(toCreatePomodoroDialog)
            onPause()
        }

        binding.layoutRefresh.setOnRefreshListener {
            sessionViewModel.getAllPomodoros()
        }

        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onStop() {
        super.onStop()
        findNavController().removeOnDestinationChangedListener(destinationChangedListener)
    }

    private fun setupObservers() {
        sessionViewModel.pomodoros.observe(viewLifecycleOwner) { pomodoros ->
            val sortedPomodoros = pomodoros.sortedBy { pomodoro ->
                val date = formatFromServer.parse(pomodoro.createdAt)
                date
            }.reversed()
            sessionAdapter.updateList(sortedPomodoros)
        }
        sessionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.layoutRefresh.isRefreshing = isLoading
        }
    }
}