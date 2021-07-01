package ru.s1aks.mtmdb.framework.ui.main_fragment

import android.util.Log
import androidx.lifecycle.*
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData() = liveDataToObserve
    fun getData() = getNewDataFromLocalSource()

    private fun getNewDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            try {
                liveDataToObserve.postValue(
                    AppState.Success(
                        repository.getNewMoviesFromServer(),
                        repository.getTopMoviesFromServer()
                    ))
            } catch (ex: Exception) {
                liveDataToObserve.postValue(
                    AppState.Error(
                        error = ex
                    ))
            }

        }.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onViewStart() {
        Log.i("LifecycleEvent", "onStart")
    }
}