package ru.s1aks.mtmdb.framework.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.s1aks.mtmdb.databinding.FragmentHistoryRecyclerItemBinding
import ru.s1aks.mtmdb.model.entities.History

class HistoryFragmentAdapter : RecyclerView.Adapter<HistoryFragmentAdapter.HistoryViewHolder>() {
    private var data: List<History> = listOf()

    fun setData(data: List<History>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryViewHolder(
            FragmentHistoryRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position])
        Log.d("VHOLDER", data[position].toString())
    }

    override fun getItemCount() = data.size

    inner class HistoryViewHolder(private val binding: FragmentHistoryRecyclerItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: History) = with(binding) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                movieTitle.text = data.movie_title
                time.text = data.time
                root.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "on click: ${data.movie_title}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}