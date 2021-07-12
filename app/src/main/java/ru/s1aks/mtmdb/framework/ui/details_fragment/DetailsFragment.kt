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
import ru.s1aks.mtmdb.framework.ui.adapters.CreditsDetailsFragmentAdapter
import ru.s1aks.mtmdb.framework.ui.person_fragment.PersonFragment
import ru.s1aks.mtmdb.model.CreditsState
import ru.s1aks.mtmdb.model.MovieState
import ru.s1aks.mtmdb.model.entities.Cast
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
    private var adapter: CreditsDetailsFragmentAdapter? = null

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
            movieCastRecyclerView.adapter = adapter
            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                renderData(appState)
            })
            viewModel.creditsLiveData.observe(viewLifecycleOwner, { creditState ->
                initAdapter(creditState)
            })
            viewModel.getMovieFromRemoteSource(it)
            viewModel.getCredits(it)
        }
    }

    private fun initAdapter(creditState: CreditsState?) {
        when (creditState) {
            is CreditsState.Success -> {
                adapter = CreditsDetailsFragmentAdapter(object : OnItemViewClickListener {
                    override fun onItemViewClick(cast: Cast) {
                        val fragmentManager = activity?.supportFragmentManager
                        fragmentManager?.let { manager ->
                            val bundle = Bundle().apply {
                                putInt(PersonFragment.BUNDLE_EXTRA, cast.id)
                            }
                            manager.beginTransaction()
                                .replace(R.id.container, PersonFragment.newInstance(bundle))
                                .addToBackStack("")
                                .commitAllowingStateLoss()
                        }
                    }
                }).apply {
                    setData(creditState.creditsData)
                }
                binding.movieCastRecyclerView.adapter = adapter
            }
            is CreditsState.Error -> {

            }
        }
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    private fun renderData(movieState: MovieState) = with(binding) {
        when (movieState) {
            is MovieState.Loading ->
                loadingLayout.visibility = View.VISIBLE
            is MovieState.Success -> {
                loadingLayout.visibility = View.GONE
                movieTitle.text = movieState.moviesData.first().title
                movieDate.text = movieState.moviesData.first().release_date
                moviePoster.load(BASE_IMAGE_URL
                        + movieState.moviesData[0].poster_path)
                movieOverview.text = movieState.moviesData.first().overview
                launch(Dispatchers.IO) {
                    try {
                        viewModel.saveToHistory(History(
                            movieState.moviesData.first().id,
                            movieState.moviesData.first().title,
                            LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(AppSettings.DATE_TIME_FORMAT_PATTERN))
                        ))
                    } catch (exception: Exception) {
                        Log.d("ERROR", "Error load from DB:" + exception.localizedMessage)
                    }

                }
            }
            is MovieState.Error -> {
                loadingLayout.visibility = View.GONE
                movieState.error.localizedMessage?.let {
                    view?.showSnackBar(
                        it,
                        getString(R.string.reload),
                        { viewModel.getMovieFromRemoteSource(movieId) }
                    )
                }
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(cast: Cast)
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