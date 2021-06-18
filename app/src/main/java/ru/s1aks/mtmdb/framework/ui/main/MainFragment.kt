package ru.s1aks.mtmdb.framework.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.databinding.MainFragmentBinding
import ru.s1aks.mtmdb.framework.ui.adapters.MainFragmentAdapter
import ru.s1aks.mtmdb.model.AppState
import ru.s1aks.mtmdb.model.entities.Movie

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()

    private var adapter: MainFragmentAdapter? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getMyData()
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                adapter = MainFragmentAdapter(object : OnItemViewClickListener {
                    override fun onItemViewClick(movie: Movie) {
//                        val manager = activity?.supportFragmentManager
//                        manager?.let { manager ->
//                            val bundle = Bundle().apply {
//                                putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
//                            }
//                            manager.beginTransaction()
//                                .add(R.id.container, DetailsFragment.newInstance(bundle))
//                                .addToBackStack("")
//                                .commitAllowingStateLoss()
//                        }
                    }
                }
                ).apply {
                    setMovies(appState.moviesData)
                }
                mainFragmentRecyclerView.adapter = adapter
            }
            is AppState.Loading -> {
                mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainFragmentLayout, appState.error.localizedMessage!!, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getMyData() }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }
}