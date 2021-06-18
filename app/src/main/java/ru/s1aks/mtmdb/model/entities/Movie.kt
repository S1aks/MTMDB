package ru.s1aks.mtmdb.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.s1aks.mtmdb.R

@Parcelize
class Movie(
    val imageId: Int = 0,
    val title: String = "Title",
    val date: String = "Date"
) : Parcelable

fun getMovies(): List<Movie> {
    return listOf(
        Movie(R.drawable.cruella, "Круэлла", "26 май 2021"),
        Movie(R.drawable.conjuring, "Заклятие 3: По воле дьявола", "25 май 2021"),
        Movie(R.drawable.wrath_of_man, "Гнев человеческий", "22 апр 2021"),
        Movie(R.drawable.ferry, "Ферри", "14 май 2021"),
        Movie(R.drawable.mortal_kombat, "Мортал Комбат","07 апр 2021")
    )
}