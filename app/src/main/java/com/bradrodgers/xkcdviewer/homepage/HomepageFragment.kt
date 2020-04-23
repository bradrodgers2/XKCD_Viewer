package com.bradrodgers.xkcdviewer.homepage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import coil.size.Scale
import com.bradrodgers.xkcdviewer.R
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

        viewModel.comicInfo.observe(viewLifecycleOwner, Observer {
            Timber.d("specific comic response: $it")
            binding.altText.text = it.alt
            binding.comicTitle.text = it.title


            val dateString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val date = LocalDate.of(it.year, it.month, it.day)
                "Published: " + date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            } else {
                "Published: " + it.month.toString() + " " + it.day.toString() + ", " + it.year.toString()
            }

            binding.publishDate.text = dateString

            Glide.with(this).load(it.img).into(binding.comicImageView)

            if(comicNumberMax == 0) comicNumberMax = it.num
        })

        binding.randomBtn.setOnClickListener {
            val rand = (1..comicNumberMax).random()
            viewModel.setComicNumber(rand)
        }

        viewModel.setComicNumber(0)

        return view
    }
}
