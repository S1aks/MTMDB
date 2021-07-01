package ru.s1aks.mtmdb.framework.ui.details_fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.s1aks.mtmdb.databinding.FragmentDetailsBinding
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.entities.Movie


class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let {
            movieTitle.text = it.title
            movieDate.text = it.release_date
            val imgUri: Uri =
                Uri.parse("https://www.themoviedb.org/t/p/w600_and_h900_bestv2" + it.poster_path)
            moviePoster.setImageURI(imgUri)
            viewModel.liveDataToObserve.observe(viewLifecycleOwner, { appState ->
                when (appState) {
                    is AppState.Error -> {
                        //...
                        loadingLayout.visibility = View.GONE
                    }
                    AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
                    is AppState.Success -> {
                        loadingLayout.visibility = View.GONE
                        movieOverview.text = it.overview
                    }
                }
            })
            viewModel.loadData(id)
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "movie"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}