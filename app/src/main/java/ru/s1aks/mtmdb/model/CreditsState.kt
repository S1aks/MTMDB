package ru.s1aks.mtmdb.model

import ru.s1aks.mtmdb.model.entities.Cast

sealed class CreditsState {
    data class Success(val creditsData: List<Cast>) : CreditsState()
    data class Error(val error: Throwable) : CreditsState()
}