package com.cinemate.app.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cinemate.app.R
import com.cinemate.app.ui.fragments.main.FeedFragment
import com.cinemate.app.ui.fragments.main.MinhaListaFragment
import com.cinemate.app.ui.fragments.main.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {

    private var activeFragment: Fragment? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_user_dashboard)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val feedFragment = FeedFragment()
        val minhaListaFragment = MinhaListaFragment()
        val perfilFragment = PerfilFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, perfilFragment, "Perfil")
            .hide(perfilFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, minhaListaFragment, "MinhaLista")
            .hide(minhaListaFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, feedFragment, "Feed")
            .commit()

        activeFragment = feedFragment

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_feed -> {
                    switchFragment(feedFragment)
                    true
                }
                R.id.nav_lista -> {
                    switchFragment(minhaListaFragment)
                    true
                }
                R.id.nav_perfil -> {
                    switchFragment(perfilFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(targetFragment: Fragment) {
        if (activeFragment != targetFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment!!)
                .show(targetFragment)
                .commitAllowingStateLoss()
            activeFragment = targetFragment
        }
    }
}
