package ru.s1aks.mtmdb.framework.ui.details_fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.repository.Repository

class DetailsViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun loadData(id: Int) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            val data = repository.getMovieFromServer(id)
            liveDataToObserve.postValue(AppState.Success(listOf(data), listOf<Movie>()))
        }.start()
    }
}