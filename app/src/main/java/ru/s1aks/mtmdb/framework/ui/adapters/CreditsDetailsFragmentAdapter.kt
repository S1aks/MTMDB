package ru.s1aks.mtmdb.framework.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.s1aks.mtmdb.databinding.FragmentDetailsCastRecyclerItemBinding
import ru.s1aks.mtmdb.framework.ui.details_fragment.DetailsFragment
import ru.s1aks.mtmdb.model.entities.Cast
import ru.s1aks.mtmdb.model.repository.BASE_IMAGE_URL

class CreditsDetailsFragmentAdapter(
    private var itemClickListener: DetailsFragment.OnItemViewClickListener?
) : RecyclerView.Adapter<CreditsDetailsFragmentAdapter.CreditsViewHolder>() {
    private var data: List<Cast> = listOf()

    fun setData(data: List<Cast>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CreditsViewHolder(
        FragmentDetailsCastRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: CreditsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class CreditsViewHolder(private val binding: FragmentDetailsCastRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Cast) = with(binding) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                actorName.text = data.name
                profilePath.load(BASE_IMAGE_URL + data.profile_path)
                root.setOnClickListener { itemClickListener?.onItemViewClick(data) }
            }
        }
    }
}