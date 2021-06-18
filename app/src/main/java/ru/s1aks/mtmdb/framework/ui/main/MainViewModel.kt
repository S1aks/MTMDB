package ru.s1aks.mtmdb.framework.ui.main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.repository.Repository
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData() = liveDataToObserve
    fun getMyData() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(2000)
            try {
                when (Random.nextInt(5)) {
                    0 -> liveDataToObserve.postValue(
                        AppState.Success(
                            repository.getWeatherFromLocalStorage()
                        ))
                    1 -> throw Exception("No connect with server!")
                    else -> throw Exception("Error download data!")
                }
            } catch (ex: Exception) {
                liveDataToObserve.postValue(
                    AppState.Error(
                    error = ex
                ))
            }

        }.start()
    }
}