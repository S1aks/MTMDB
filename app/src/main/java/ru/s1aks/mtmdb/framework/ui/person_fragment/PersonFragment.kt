package ru.s1aks.mtmdb.framework.ui.person_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
import java.io.IOException


class PersonFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var binding: FragmentPersonBinding
    private val viewModel: PersonViewModel by viewModel {
        parametersOf(RepositoryImpl(RemoteDataSource()))
    }
    private var personId: Int = 0
    private var searchText: String? = null
    private val markers: ArrayList<Marker> = ArrayList()
    private lateinit var map: GoogleMap
    private var birthPlace: String? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }

        val geoCoder = Geocoder(context)
        val searchText = birthPlace
        Thread {
            try {
                val addresses = geoCoder.getFromLocationName(searchText, 1)
                if (addresses.isNotEmpty()) {
                    searchText?.let { goToAddress(addresses, binding.map, it) }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun goToAddress(addresses: MutableList<Address>, view: View, searchText: String) {
        val location = LatLng(addresses[0].latitude, addresses[0].longitude)
        view.post {
            setMarker(location, searchText)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    15f
                )
            )
        }
    }

    private fun setMarker(location: LatLng, searchText: String) {
        val marker = map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )
        marker?.let { markers.add(marker) }
    }

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
                birthPlace = personState.personData.place_of_birth
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
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