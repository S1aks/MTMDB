package ru.s1aks.mtmdb.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val movie_id: Int,
    val movie_title: String,
    val time: String
)
