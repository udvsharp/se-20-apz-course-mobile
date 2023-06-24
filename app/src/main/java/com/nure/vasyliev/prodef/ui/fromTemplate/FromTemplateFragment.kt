package com.nure.vasyliev.prodef.ui.fromTemplate

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nure.vasyliev.prodef.R
import com.nure.vasyliev.prodef.databinding.FragmentFromTemplateBinding
import com.nure.vasyliev.prodef.model.template.Template
import com.nure.vasyliev.prodef.rest.repositories.PomodoroRepository
import com.nure.vasyliev.prodef.rest.repositories.SharedPrefsRepository
import com.nure.vasyliev.prodef.rest.repositories.TemplateRepository
import com.nure.vasyliev.prodef.ui.createPomodoro.CreateSessionDialog
import com.nure.vasyliev.prodef.ui.createTemplate.CreateTemplateDialog
import com.nure.vasyliev.prodef.utils.getNavResult
import com.nure.vasyliev.prodef.utils.setNavResult

class FromTemplateFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFromTemplateBinding

    private lateinit var fromTemplateViewModel: FromTemplateViewModel

    private val templateAdapter by lazy {
        FromTemplateAdapter(
            ::onItemClickListener,
            ::onItemEditClickListener
        )
    }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, _, _ ->
            try {
                val success = getNavResult<Boolean>(R.id.fromTemplateDialog, CreateTemplateDialog.RESULT)
                if (success == true) {
                    fromTemplateViewModel.getAllTemplates()
                    setNavResult(R.id.fromTemplateDialog, CreateTemplateDialog.RESULT, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefsRepository = SharedPrefsRepository(requireContext())
        val pomodoroRepository = PomodoroRepository()
        val templateRepository = TemplateRepository()

        val fromTemplateViewModelProvider = FromTemplateViewModelProvider(
            sharedPrefsRepository,
            templateRepository,
            pomodoroRepository
        )

        fromTemplateViewModel =
            ViewModelProvider(this, fromTemplateViewModelProvider)[FromTemplateViewModel::class.java]

        binding = FragmentFromTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTemplates.adapter = templateAdapter

        binding.btnCreateTemplate.setOnClickListener {
            val toCreateTemplateDialog = FromTemplateFragmentDirections.toCreateTemplateDialog()
            findNavController().navigate(toCreateTemplateDialog)
            onPause()
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

                val iconMargin = 40
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
                    .setTitle(requireContext().getString(R.string.delete_template))
                    .setMessage(requireContext().getString(R.string.delete_template_confirmation))
                    .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                        val template = templateAdapter.currentList[position]
                        fromTemplateViewModel.deleteTemplate(template.id)
                        templateAdapter.removeItem(position)
                    }
                    .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                        templateAdapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        templateAdapter.notifyItemChanged(position)
                    }.create().show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTemplates)
    }

    private fun onItemClickListener(template: Template) {
        fromTemplateViewModel.createPomodoroFromTemplate(template.id)
        setNavResult(R.id.sessionsFragment, CreateSessionDialog.RESULT, true)
        findNavController().popBackStack(R.id.sessionsFragment, false)
    }

    private fun onItemEditClickListener(template: Template) {
        val templateId  = template.id
        val toUpdateTemplateDialog = FromTemplateFragmentDirections.toUpdateTemplateDialog(templateId)
        findNavController().navigate(toUpdateTemplateDialog)
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
        fromTemplateViewModel.templates.observe(viewLifecycleOwner) { templates ->
            templateAdapter.submitList(templates)
        }
    }
}