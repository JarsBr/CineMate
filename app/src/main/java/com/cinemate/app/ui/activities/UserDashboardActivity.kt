package com.cinemate.app.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.cinemate.app.R
import com.cinemate.app.ui.fragments.main.FeedFragment
import com.cinemate.app.ui.fragments.main.MinhaListaFragment
import com.cinemate.app.ui.fragments.main.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {

    private var activeFragmentTag: String = "Feed"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        setContentView(R.layout.activity_user_dashboard)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        if (savedInstanceState == null) {
            setupInitialFragments()
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_feed -> {
                    switchFragment("Feed", FeedFragment())
                    true
                }
                R.id.nav_lista -> {
                    switchFragment("MinhaLista", MinhaListaFragment())
                    true
                }
                R.id.nav_perfil -> {
                    switchFragment("Perfil", PerfilFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun setupInitialFragments() {
        val feedFragment = FeedFragment()
        val minhaListaFragment = MinhaListaFragment()
        val perfilFragment = PerfilFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, feedFragment, "Feed")
            .setMaxLifecycle(feedFragment, Lifecycle.State.RESUMED)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, minhaListaFragment, "MinhaLista")
            .setMaxLifecycle(minhaListaFragment, Lifecycle.State.STARTED)
            .hide(minhaListaFragment)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, perfilFragment, "Perfil")
            .setMaxLifecycle(perfilFragment, Lifecycle.State.STARTED)
            .hide(perfilFragment)
            .commit()

        activeFragmentTag = "Feed"
    }

    private fun switchFragment(targetTag: String, targetFragment: Fragment) {
        if (activeFragmentTag != targetTag) {
            val fragmentManager = supportFragmentManager
            val currentFragment = fragmentManager.findFragmentByTag(activeFragmentTag)
            val newFragment = fragmentManager.findFragmentByTag(targetTag)

            fragmentManager.beginTransaction().apply {
                if (currentFragment != null) {
                    hide(currentFragment)
                    setMaxLifecycle(currentFragment, Lifecycle.State.STARTED)
                }
                if (newFragment != null) {
                    show(newFragment)
                    setMaxLifecycle(newFragment, Lifecycle.State.RESUMED)
                } else {
                    add(R.id.fragmentContainer, targetFragment, targetTag)
                        .setMaxLifecycle(targetFragment, Lifecycle.State.RESUMED)
                }
            }.commitAllowingStateLoss()

            activeFragmentTag = targetTag
        }
    }
}
