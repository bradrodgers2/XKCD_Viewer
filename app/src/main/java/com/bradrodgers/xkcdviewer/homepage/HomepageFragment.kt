package com.bradrodgers.xkcdviewer.homepage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bradrodgers.xkcdviewer.databinding.HomepageFragmentBinding
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomepageFragment : Fragment() {

    private val viewModel: HomepageViewModel by viewModel()
    private var _binding: HomepageFragmentBinding? = null
    private val binding get() = _binding!!
    private var comicNumberMax = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomepageFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.comicInfo.observe(viewLifecycleOwner, Observer { comicInfo ->
            Timber.d("specific comic response: $comicInfo")
            binding.altText.text = comicInfo.alt
            binding.comicTitle.text = comicInfo.title

            //TODO: Format this string in the repo
            val dateString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val date = LocalDate.of(comicInfo.year, comicInfo.month, comicInfo.day)
                "Published: " + date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            } else {
                "Published: " + comicInfo.month.toString() + " " + comicInfo.day.toString() + ", " + comicInfo.year.toString()
            }

            binding.publishDate.text = dateString

            Glide.with(this).load(comicInfo.img).into(binding.comicImageView)

            binding.addToFavoritesButton.setOnClickListener {
                //TODO: Make this a toggle feature rather than strictly add to
                viewModel.saveComic(comicInfo)
            }

            //TODO: Move this to the viewmodel
            if (comicNumberMax == 0) comicNumberMax = comicInfo.num
        })

        binding.randomBtn.setOnClickListener {
            val rand = (1..comicNumberMax).random()
            viewModel.setComicNumber(rand)
        }

        viewModel.errorStatement.observe(viewLifecycleOwner, Observer {
            Timber.e("error statement: $it")
            //TODO:  Implement the error dialog
        })

        //TODO: Make this a separate layout instead of just a recycler view so we can include a closing X, a header, and an empty state
        val adapter = FavoriteComicListAdapter()
        viewModel.savedComics.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            adapter.onItemClick = { comicInfo ->
                viewModel.setComicNumber(comicInfo.num)
                binding.favoriteComicRecyclerView.visibility = View.GONE
            }
        })

        binding.favoriteBtn.setOnClickListener {
            //TODO: Move this logic to the viewmodel and create an observable to toggle visibility
            if (adapter.data.isNotEmpty())
                binding.favoriteComicRecyclerView.visibility = View.VISIBLE
        }

        viewModel.setComicNumber(0)

        return view
    }
}
