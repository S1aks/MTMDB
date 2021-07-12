package ru.s1aks.mtmdb.framework.ui.details_fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.s1aks.mtmdb.model.CreditsState
import ru.s1aks.mtmdb.model.MovieState
import ru.s1aks.mtmdb.model.entities.Credits
import ru.s1aks.mtmdb.model.entities.History
import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.repository.*

class DetailsViewModel(
    private val repository: RepositoryImpl = RepositoryImpl(RemoteDataSource()),
) : ViewModel(), LifecycleObserver{
    val liveData: MutableLiveData<MovieState> = MutableLiveData()
    val creditsLiveData: MutableLiveData<CreditsState> = MutableLiveData()

    fun getMovieFromRemoteSource(id: Int) {
        liveData.value = MovieState.Loading
        repository.getMovieDetailsFromServer(id, callback)
    }

    fun saveToHistory(history: History) {
        repository.saveToHistory(history)
    }

    fun getCredits(id: Int) {
        repository.getCreditsFromServer(id, callbackCredits)
    }

    private val callback = object :
        Callback<Movie> {
        override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
            val serverResponse: Movie? = response.body()
            liveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    MovieState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<Movie>, t: Throwable) {
            liveData.postValue(MovieState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: Movie): MovieState {
            return if (serverResponse.overview == null) {
                MovieState.Error(Throwable(CORRUPTED_DATA))
            } else {
                MovieState.Success(listOf(serverResponse))
            }
        }
    }

    private val callbackCredits = object :
        Callback<Credits> {
        override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
            val serverResponse: Credits? = response.body()
            creditsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    CreditsState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<Credits>, t: Throwable) {
            creditsLiveData.postValue(CreditsState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: Credits): CreditsState {
            return if (serverResponse.cast == null) {
                CreditsState.Error(Throwable(CORRUPTED_DATA))
            } else {
                CreditsState.Success(serverResponse.cast)
            }
        }
    }
}
