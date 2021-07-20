package ru.s1aks.mtmdb.framework.ui.person_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.databinding.FragmentPersonBinding
import ru.s1aks.mtmdb.model.PersonState
import ru.s1aks.mtmdb.model.repository.BASE_IMAGE_URL
import ru.s1aks.mtmdb.model.repository.RemoteDataSource
import ru.s1aks.mtmdb.model.repository.RepositoryImpl
import ru.s1aks.mtmdb.utils.showSnackBar


class PersonFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var binding: FragmentPersonBinding
    private val viewModel: PersonViewModel by viewModel {
        parametersOf(RepositoryImpl(RemoteDataSource()))
    }
    private var personId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(BUNDLE_EXTRA, DEF_INT_VAL)?.let {
            personId = it
            viewModel.personLiveData.observe(viewLifecycleOwner, { personState ->
                renderData(personState)
            })
            viewModel.getPerson(it)
        }
    }

    private fun renderData(personState: PersonState) = with(binding) {
        when (personState) {
            is PersonState.Loading ->
                loadingLayout.visibility = View.VISIBLE
            is PersonState.Success -> {
                loadingLayout.visibility = View.GONE
                personName.text = personState.personData.name
                personBirthDate.text = personState.personData.birthday
                personImage.load(BASE_IMAGE_URL
                        + personState.personData.profile_path)
            }
            is PersonState.Error -> {
                loadingLayout.visibility = View.GONE
                personState.error.localizedMessage?.let {
                    view?.showSnackBar(
                        it,
                        getString(R.string.reload),
                        { viewModel.getPerson(personId) }
                    )
                }
            }
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "person_id"
        const val DEF_INT_VAL = 0

        fun newInstance(bundle: Bundle): PersonFragment {
            val fragment = PersonFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}