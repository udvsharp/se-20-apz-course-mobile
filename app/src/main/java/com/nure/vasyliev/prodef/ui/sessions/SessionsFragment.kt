package com.nure.vasyliev.prodef.ui.sessions

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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

    private val sessionAdapter by lazy {
        SessionRecyclerViewAdapter()
    }

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

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setTitle(R.string.title_sessions)

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

        setupSwipe()
        setupObservers()
    }

    private fun setupSwipe() {
        val itemTouchCallback = object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val background = ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.background_delete
                    )
                )
                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!

                val iconMargin = 60
                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )

                background.draw(c)
                icon.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.7f
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                AlertDialog.Builder(requireContext())
                    .setTitle(requireContext().getString(R.string.delete_session))
                    .setMessage(requireContext().getString(R.string.delete_session_confirmation))
                    .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                        val pomodoro = sessionAdapter.currentList[position]
                        sessionViewModel.deletePomodoro(pomodoro.id)
                        sessionAdapter.removeItem(position)
                    }
                    .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                        sessionAdapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        sessionAdapter.notifyItemChanged(position)
                    }.create().show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvSessions)
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
            sessionAdapter.submitList(sortedPomodoros) {
                if (pomodoros.isNotEmpty()) {
                    binding.rvSessions.scrollToPosition(0)
                }
            }
        }
        sessionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.layoutRefresh.isRefreshing = isLoading
        }
    }
}