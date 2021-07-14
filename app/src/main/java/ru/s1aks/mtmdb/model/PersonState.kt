package ru.s1aks.mtmdb.model

import ru.s1aks.mtmdb.model.entities.Person

sealed class PersonState {
    data class Success(val personData: Person) : PersonState()
    data class Error(val error: Throwable) : PersonState()
    object Loading : PersonState()
}