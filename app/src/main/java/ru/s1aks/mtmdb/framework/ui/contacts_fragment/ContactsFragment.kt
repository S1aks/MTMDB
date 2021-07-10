package ru.s1aks.mtmdb.framework.ui.contacts_fragment

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.databinding.FragmentContactsBinding


class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding

    private val permissionResult = registerForActivityResult(RequestPermission()) { result ->
        if (result) {
            getContacts()
        } else {
            Toast.makeText(context,
                getString(R.string.read_contacts_toast_text),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun requestPermission() {
        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) -> {
                    getContacts()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContacts?.let { cursor ->
                for (i in 1..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        addView(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun addView(textToShow: String) {
        binding.containerForContacts.addView(AppCompatTextView(requireContext()).apply {
            text = textToShow
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.overview_text_size))
        })
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}