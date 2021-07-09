package ru.s1aks.mtmdb.framework.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.s1aks.mtmdb.R
import ru.s1aks.mtmdb.framework.ui.main_fragment.MainFragment
import ru.s1aks.mtmdb.framework.ui.settings_fragment.SettingsFragment
import ru.s1aks.mtmdb.framework.AppSettings
import ru.s1aks.mtmdb.framework.ui.history_fragment.HistoryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        savedInstanceState ?: run {
            supportFragmentManager.apply {
                beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitAllowingStateLoss()
            }
        }
        initSettings()
    }

    private fun initSettings() {
        AppSettings.adultShow = getPreferences(Context.MODE_PRIVATE)
            ?.getBoolean(AppSettings.ADULT_PREF_KEY, false) ?: false
    }

    private fun saveSettings() {
        getPreferences(Context.MODE_PRIVATE)?.edit()
            ?.putBoolean(AppSettings.ADULT_PREF_KEY, AppSettings.adultShow)
            ?.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSettings -> {
                openFragment(SettingsFragment.newInstance())
                true
            }
            R.id.actionHistory -> {
                openFragment(HistoryFragment.newInstance())
                true
            }
            R.id.actionFavorites -> {
                Toast.makeText(this, "Favorites Clicked", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onPause() {
        saveSettings()
        super.onPause()
    }
}