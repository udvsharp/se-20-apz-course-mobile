package com.nure.vasyliev.prodef.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nure.vasyliev.prodef.MainActivity
import com.nure.vasyliev.prodef.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
}