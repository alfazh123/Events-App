package com.example.submisiakhir.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.submisiakhir.R
import com.example.submisiakhir.data.local.entity.FavoriteEntity
import com.example.submisiakhir.data.remote.response.Event
import com.example.submisiakhir.databinding.ActivityDetailBinding
import com.example.submisiakhir.ui.utils.viewmodel.DetailAndBookmarkViewModel
import com.example.submisiakhir.data.ViewModelFactory
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private  var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val EXTRA_ID = "extra_id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EXTRA_ID, 0)

        val factory = ViewModelFactory.getInstance(this)
        val detailViewModel: DetailAndBookmarkViewModel by viewModels { factory }


        lifecycleScope.launch {
            detailViewModel.getEvent(eventId)
        }

        detailViewModel.eventDetail.observe(this) { event ->
            setDetailEvent(event)
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            isLoadState(isLoading)
        }

        detailViewModel.errorMessage.observe(this) { errorMessage ->
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        detailViewModel.isFavorite(eventId).observe(this) { favorite ->
            if (favorite != null) {
                binding.btnBookmark.setImageResource(R.drawable.star_24dp_fill)
                binding.btnBookmark.setOnClickListener {
                    detailViewModel.deleteFromFavorite(favorite)
                    binding.btnBookmark.setImageResource(R.drawable.star_24dp_border)
                }
            } else {
                binding.btnBookmark.setImageResource(R.drawable.star_24dp_border)
                binding.btnBookmark.setOnClickListener {
                    detailViewModel.eventDetail.observe(this) {event->
                        val ev = FavoriteEntity(
                            event.id,
                            event.name,
                            event.summary,
                            event.description,
                            event.imageLogo
                        )
                        detailViewModel.setEvent(ev)
                        binding.btnBookmark.setImageResource(R.drawable.star_24dp_fill)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailEvent(event: Event) {
        binding.tvTitle.text = event.name
        binding.tvSummary.text = event.summary
        binding.tvDate.text = event.beginTime
        binding.tvPenyelenggara.text = event.ownerName
        binding.tvSisaKuota.text = "${event.quota - event.registrants} Kuota tersedia"
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.imgPoster)
        binding.btnRegister.setOnClickListener {
            openBrowser(event.link)
        }
        binding.tvDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)


//        if (event.isBookmarked) {
//            binding.btnBookmark.setImageResource(R.drawable.star_24dp_fill)
//        } else {
//            binding.btnBookmark.setImageResource(R.drawable.star_24dp_border)
//        }
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun isLoadState(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}