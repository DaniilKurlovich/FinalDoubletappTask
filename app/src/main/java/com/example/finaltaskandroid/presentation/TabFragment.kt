package com.example.finaltaskandroid.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.finaltaskandroid.R
import com.google.android.material.tabs.TabLayout
import java.lang.Exception


class TabFragment: Fragment() {
    private lateinit var collectionPagerAdapter: CollectionTabAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        collectionPagerAdapter = CollectionTabAdapter(childFragmentManager)
        viewPager.adapter = collectionPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

}


class CollectionTabAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    val fragmentBadList = HabitsListFragment.newInstance("Good")
    val fragmentGoodList = HabitsListFragment.newInstance("Bad")

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return fragmentBadList
            1 -> return fragmentGoodList
            else -> throw Exception("Position more than $position>2 does not implemented")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "Good"
            1 -> return "Bad"
            else -> throw Exception("Position more than $position>2 does not implemented")
        }
    }
}