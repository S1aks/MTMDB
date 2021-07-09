package ru.s1aks.mtmdb.framework.ui.details_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.databinding.FragmentDetailsBinding
import ru.s1aks.mtmdb.framework.AppSettings
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.entities.History
import ru.s1aks.mtmdb.model.repository.BASE_IMAGE_URL
import ru.s1aks.mtmdb.model.repository.RemoteDataSource
import ru.s1aks.mtmdb.model.repository.RepositoryImpl
import ru.s1aks.mtmdb.utils.showSnackBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DetailsFragment : Fragment(), CoroutineScope by MainScope() {
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
        arguments?.getInt(BUNDLE_EXTRA, DEF_INT_VAL)?.let {
            movieId = it
            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                renderData(appState)
            })
            viewModel.getMovieFromRemoteSource(it)
        }
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Loading ->
                loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                loadingLayout.visibility = View.GONE
                movieTitle.text = appState.moviesData.first().title
                movieDate.text = appState.moviesData.first().release_date
                moviePoster.load(BASE_IMAGE_URL
                        + appState.moviesData[0].poster_path)
                movieOverview.text = appState.moviesData.first().overview
                launch(Dispatchers.IO) {
                    try {
                        viewModel.saveToHistory(History(
                            appState.moviesData.first().id,
                            appState.moviesData.first().title,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppSettings.DATE_TIME_FORMAT_PATTERN))
                        ))
                    } catch (exception: Exception) {
                        Log.d("ERROR", "Error load from DB:" + exception.localizedMessage)
                    }

                }
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
        const val DEF_INT_VAL = 0

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}