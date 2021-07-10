package ru.s1aks.mtmdb.framework.ui.settings_fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class SettingsViewModel: ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {
    val settingsLiveData by lazy { MutableLiveData<Boolean>() }
}

