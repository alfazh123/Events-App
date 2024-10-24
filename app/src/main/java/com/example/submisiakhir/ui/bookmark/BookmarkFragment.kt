 package com.example.submisiakhir.ui.bookmark

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submisiakhir.data.ViewModelFactory
import com.example.submisiakhir.data.local.entity.FavoriteEntity
import com.example.submisiakhir.databinding.FragmentBookmarkBinding
import com.example.submisiakhir.ui.detail.DetailActivity
import com.example.submisiakhir.ui.utils.adapter.BookmarkAdapter
import com.example.submisiakhir.ui.utils.viewmodel.DetailAndBookmarkViewModel

 class BookmarkFragment : Fragment() {

     private var _binding: FragmentBookmarkBinding? = null
     private val binding get() = _binding!!

     private val bookmarkViewModel by viewModels<DetailAndBookmarkViewModel> {
         ViewModelFactory.getInstance(requireActivity())
     }


     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

         val layoutManager = LinearLayoutManager(context)
         binding.rvFavorite.layoutManager = layoutManager
         val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
         binding.rvFavorite.addItemDecoration(itemDecoration)

         bookmarkViewModel.getFavorite().observe(viewLifecycleOwner) { favorite ->
             setRecyclerView(favorite)
         }

         bookmarkViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
             isLoadState(isLoading)
         }

         bookmarkViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
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

     private fun setRecyclerView(event: List<FavoriteEntity>) {
         val adapter = BookmarkAdapter()
         adapter.submitList(event)
         binding.rvFavorite.adapter = adapter

         adapter.setOnItemClickCallback(object : BookmarkAdapter.OnItemClickBack {
             override fun onItemClicked(event: FavoriteEntity) {
                 val intent = Intent(context, DetailActivity::class.java)
                 intent.putExtra(DetailActivity.EXTRA_ID, event.id)
                 startActivity(intent)
             }
         })
     }

     private fun isLoadState(isLoading: Boolean) {
         if (isLoading) {
             binding.progressBar.visibility = View.VISIBLE
         } else {
             binding.progressBar.visibility = View.GONE
         }
     }

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
     ): View {
         // Inflate the layout for this fragment

         _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
         val root: View = binding.root

         return root
     }

}