package ru.s1aks.mtmdb.framework.ui.details_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.databinding.FragmentDetailsBinding
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.repository.RemoteDataSource
import ru.s1aks.mtmdb.model.repository.RepositoryImpl
import ru.s1aks.mtmdb.utils.showSnackBar


class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModel {
        parametersOf(RepositoryImpl(RemoteDataSource()))
    }
    private var movieId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(BUNDLE_EXTRA, 0)?.let {
            movieId = it
            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                renderData(appState)
            })
            viewModel.getMovieFromRemoteSource(it)
        }
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Loading ->
                loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                loadingLayout.visibility = View.GONE
                movieTitle.text = appState.moviesData[0].title
                movieDate.text = appState.moviesData[0].release_date
                moviePoster.load("https://image.tmdb.org/t/p/original"
                        + appState.moviesData[0].poster_path)
                movieOverview.text = appState.moviesData[0].overview
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                appState.error.localizedMessage?.let {
                    view?.showSnackBar(
                        it,
                        getString(R.string.reload),
                        { viewModel.getMovieFromRemoteSource(movieId) }
                    )
                }
            }
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "movie_id"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}