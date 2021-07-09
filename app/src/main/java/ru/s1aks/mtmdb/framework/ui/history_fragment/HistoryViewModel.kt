package ru.s1aks.mtmdb.framework.ui.history_fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.s1aks.mtmdb.model.entities.History
import ru.s1aks.mtmdb.model.repository.RemoteDataSource
import ru.s1aks.mtmdb.model.repository.RepositoryImpl

class HistoryViewModel(
    private val repository: RepositoryImpl = RepositoryImpl(RemoteDataSource()),
) : ViewModel(), LifecycleObserver {
    val historyLiveData: MutableLiveData<List<History>> = MutableLiveData()

    fun getAllHistory() {
        historyLiveData.postValue(repository.getAllHistory())
    }
}