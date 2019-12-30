package com.example.kotlinmvvm.view.news.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT 表示在ViewPager中 除了要显示的Fragment会执行onResume之外其他的只执行到onStart
 */
class FragmentManagerdapter(
    private val fragmentList: List<Fragment>,
    private val titleList: List<String>,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}