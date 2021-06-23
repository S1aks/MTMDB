package ru.s1aks.mtmdb.framework.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.s1aks.mtmdb.databinding.FragmentMainNewRecyclerItemBinding
import ru.s1aks.mtmdb.framework.ui.main_fragment.MainFragment
import ru.s1aks.mtmdb.model.entities.Movie

class MainFragmentAdapterNew(private var itemClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapterNew.MainViewHolder>() {
    private var moviesData: List<Movie> = listOf()
    private lateinit var binding: FragmentMainNewRecyclerItemBinding

    fun setMovies(data: List<Movie>) {
        moviesData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        binding = FragmentMainNewRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MainViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(moviesData[position])
    }

    override fun getItemCount(): Int {
        return moviesData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie) = with(binding) {
            moviePoster.setImageResource(movie.imageId)
            movieTitle.text = movie.title
            movieDate.text = movie.date
            root.setOnClickListener { itemClickListener?.onItemViewClick(movie) }
        }
    }
}