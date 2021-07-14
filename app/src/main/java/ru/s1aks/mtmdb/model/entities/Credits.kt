package ru.s1aks.mtmdb.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Credits(
    val id: Int,
    val cast: List<Cast>?
) : Parcelable
