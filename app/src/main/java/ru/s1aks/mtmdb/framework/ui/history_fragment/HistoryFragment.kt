package ru.s1aks.mtmdb.framework.ui.history_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.databinding.FragmentHistoryBinding
import ru.s1aks.mtmdb.framework.ui.adapters.HistoryFragmentAdapter
import ru.s1aks.mtmdb.framework.ui.details_fragment.DetailsFragment
import ru.s1aks.mtmdb.model.repository.RemoteDataSource
import ru.s1aks.mtmdb.model.repository.RepositoryImpl

class HistoryFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel {
        parametersOf(RepositoryImpl(RemoteDataSource()))
    }
    private var adapter: HistoryFragmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        RecyclerView.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, {
            adapter = HistoryFragmentAdapter(object : OnItemViewClickListener {
                override fun onItemViewClick(movie_id: Int) {
                    val fragmentManager = activity?.supportFragmentManager
                    fragmentManager?.let { manager ->
                        val bundle = Bundle().apply {
                            putInt(DetailsFragment.BUNDLE_EXTRA, movie_id)
                        }
                        manager.beginTransaction()
                            .replace(R.id.container, DetailsFragment.newInstance(bundle))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
            }
            ).apply {
                setData(it)
            }
            RecyclerView.adapter = adapter
        })
        launch(Dispatchers.IO) {
            viewModel.getAllHistory()
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie_id: Int)
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}