package com.example.submisiakhir.ui.eventlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submisiakhir.R
import com.example.submisiakhir.data.remote.response.ListEventsItem
import com.example.submisiakhir.databinding.FragmentUpcomingBinding
import com.example.submisiakhir.ui.utils.adapter.CardAdapter
import com.example.submisiakhir.ui.utils.viewmodel.EventViewModel
import com.example.submisiakhir.ui.detail.DetailActivity

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isUpcoming = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingViewModel = ViewModelProvider(this)[EventViewModel::class.java]

        val layoutManager = LinearLayoutManager(context)
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)

        binding.btnUpcoming.setOnClickListener {
            activeUpcoming(true)
            upcomingViewModel.active.observe(viewLifecycleOwner) { events ->
                setListEvent(events)
            }
            binding.title.text = getString(R.string.upcoming_event)
        }

        binding.btnFinished.setOnClickListener {
            activeUpcoming(false)
            upcomingViewModel.completed.observe(viewLifecycleOwner) { events ->
                setListEvent(events)
            }
            binding.title.text = getString(R.string.finished_event)
        }

        upcomingViewModel.active.observe(viewLifecycleOwner) { events ->
            setListEvent(events)
        }
        activeUpcoming(isUpcoming)

        upcomingViewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        upcomingViewModel.isLoadingCompleted.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Error")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun setListEvent(data: List<ListEventsItem> ) {
        val adapter = CardAdapter()
        adapter.submitList(data)
        binding.rvEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : CardAdapter.OnItemClickBack {
            override fun onItemClicked(event: ListEventsItem) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, event.id)
                startActivity(intent)
            }
        })
    }

    private fun activeUpcoming(isUpcoming: Boolean) {
        if (isUpcoming) {
            binding.btnUpcoming.isEnabled = false
            binding.btnFinished.isEnabled = true
        } else {
            binding.btnFinished.isEnabled = false
            binding.btnUpcoming.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}