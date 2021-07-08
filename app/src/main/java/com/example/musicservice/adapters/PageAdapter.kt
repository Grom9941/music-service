package com.example.musicservice.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.musicservice.fragments.AllAlbumFragment
import com.example.musicservice.fragments.AllMusicFragment

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> AllAlbumFragment()
            else -> AllMusicFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}