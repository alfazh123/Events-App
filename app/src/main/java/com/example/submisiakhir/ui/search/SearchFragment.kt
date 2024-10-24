package com.example.submisiakhir.ui.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submisiakhir.R
import com.example.submisiakhir.data.remote.response.ListEventsItem
import com.example.submisiakhir.databinding.FragmentSearchBinding
import com.example.submisiakhir.ui.utils.adapter.CardAdapter
import com.example.submisiakhir.ui.detail.DetailActivity
import com.example.submisiakhir.ui.setting.SettingPreference
import com.example.submisiakhir.ui.setting.SettingViewModel
import com.example.submisiakhir.ui.setting.ViewModelFactory
import com.example.submisiakhir.ui.setting.dataStore
import com.example.submisiakhir.ui.utils.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var itemViewAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewAdapter = CardAdapter()

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = itemViewAdapter
        }

        searchViewModel.isLoading.observe(viewLifecycleOwner){loading ->
            binding.progressBar2.visibility = if (loading) View.VISIBLE else View.GONE
        }

        val pref = SettingPreference.getInstance(requireActivity().dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                binding.imageViewSearch.setImageResource(R.drawable.search_24dp_white)
            }
        }

        binding.tvNoEventFound.visibility = View.GONE

        searchViewModel.results.observe(viewLifecycleOwner){ result ->

            if (result.isEmpty()) {
                binding.tvNoEventFound.visibility = View.VISIBLE
                searchViewModel.isLoading.observe(viewLifecycleOwner){loading ->
                    if (loading) {
                        binding.tvNoEventFound.visibility = View.GONE
                    }
                }
            } else {
                binding.tvNoEventFound.visibility = View.GONE
            }

            itemViewAdapter.submitList(result)

            itemViewAdapter.setOnItemClickCallback(object : CardAdapter.OnItemClickBack {
                override fun onItemClicked(event: ListEventsItem) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, event.id)
                    startActivity(intent)
                }

            })
        }

        searchViewModel.errorMessage.observe(viewLifecycleOwner){errorMessage ->
            errorMessage?.let{
                AlertDialog.Builder(requireActivity())
                    .setTitle("Error")
                    .setMessage(it)
                    .setPositiveButton("OK"){dialog,_ -> dialog.dismiss()}
                    .show()
            }
        }

        getSearchQuery()
    }

    private fun getSearchQuery() {
        with(binding) {

            searchButton.setOnClickListener{
                val text = searchBar.text.toString().trim()
                if (text.isNotEmpty()) {
                    searchViewModel.setQuery(text)
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Please input the query")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}