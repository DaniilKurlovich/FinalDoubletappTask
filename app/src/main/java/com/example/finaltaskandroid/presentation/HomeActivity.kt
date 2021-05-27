package com.example.finaltaskandroid.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.finaltaskandroid.R
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {
    private var mDrawer: DrawerLayout? = null
    private var nvDrawer: NavigationView? = null
    private var toolbar: Toolbar? = null
    private lateinit var tabFragment: TabFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mDrawer = findViewById(R.id.drawer_layout)
        nvDrawer = findViewById(R.id.nvView)
        setUpDrawerContent(nvDrawer!!)

        tabFragment = TabFragment()
        supportFragmentManager.beginTransaction().add(R.id.tab_container, tabFragment)
            .addToBackStack(null).commit()

    }

    fun setUpDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_fragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.tab_container, tabFragment)
                        .addToBackStack(null).commit()
                    mDrawer?.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_description_fragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.tab_container, InfoFragment())
                        .addToBackStack(null).commit()
                    mDrawer?.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> mDrawer?.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item)
    }
}