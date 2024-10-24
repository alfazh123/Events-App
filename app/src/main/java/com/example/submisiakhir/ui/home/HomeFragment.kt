package com.example.submisiakhir.ui.home

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
import com.example.submisiakhir.data.remote.response.ListEventsItem
import com.example.submisiakhir.databinding.FragmentHomeBinding
import com.example.submisiakhir.ui.utils.adapter.CarouselAdapter
import com.example.submisiakhir.ui.utils.viewmodel.EventViewModel
import com.example.submisiakhir.ui.utils.adapter.ListEventAdapter
import com.example.submisiakhir.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel =
            ViewModelProvider(this).get(EventViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)

        binding.rvEventActive.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEventActive.addItemDecoration(itemDecoration)

        binding.rvEventComplete.layoutManager = LinearLayoutManager(context)
        val itemDecorationComplete = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvEventComplete.addItemDecoration(itemDecorationComplete)

        homeViewModel.active.observe(viewLifecycleOwner) { events ->
            val activeEventLimit = events.take(5)
            setActiveEvents(activeEventLimit)
        }

        homeViewModel.completed.observe(viewLifecycleOwner) { events ->
            val completeEventLimit = events.take(5)
            setCompleteEvents(completeEventLimit)
        }

        homeViewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbarActive.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.isLoadingCompleted.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbarComplete.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
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

    private fun setActiveEvents(data: List<ListEventsItem>) {
        val adapter = CarouselAdapter()
        adapter.submitList(data)
        binding.rvEventActive.adapter = adapter
        binding.rvEventActive.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapter.setOnItemClickCallback(object : CarouselAdapter.OnItemClickback {
            override fun onItemClicked(event: ListEventsItem) {
                moveToDetail(event.id)
            }
        })
    }

    private fun setCompleteEvents(data: List<ListEventsItem>) {
        val adapter = ListEventAdapter()
        adapter.submitList(data)
        binding.rvEventComplete.adapter = adapter

        adapter.setOnClickCallback(object : ListEventAdapter.OnItemClickback {
            override fun onItemClicked(data: ListEventsItem) {
                moveToDetail(data.id)
            }

        })
    }

    private fun moveToDetail(id: Int) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}