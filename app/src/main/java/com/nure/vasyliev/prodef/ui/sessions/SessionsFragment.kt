package com.nure.vasyliev.prodef.ui.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.databinding.FragmentSessionsBinding

class SessionsFragment : Fragment() {

    private lateinit var binding: FragmentSessionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }
}