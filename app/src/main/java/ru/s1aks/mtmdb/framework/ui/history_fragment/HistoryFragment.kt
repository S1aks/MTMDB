package ru.s1aks.mtmdb.framework.ui.history_fragment

import android.os.Bundle
import android.util.Log
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
import ru.s1aks.mtmdb.databinding.FragmentHistoryBinding
import ru.s1aks.mtmdb.framework.ui.adapters.HistoryFragmentAdapter
import ru.s1aks.mtmdb.model.repository.RemoteDataSource
import ru.s1aks.mtmdb.model.repository.RepositoryImpl

class HistoryFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel {
        parametersOf(RepositoryImpl(RemoteDataSource()))
    }
    private val adapter: HistoryFragmentAdapter by lazy { HistoryFragmentAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RecyclerView.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
        launch(Dispatchers.IO) {
            try {
                viewModel.getAllHistory()
            } catch (exception: Exception) {
                Log.d("ERROR","Error load from DB:" + exception.localizedMessage)
            }
        }
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}