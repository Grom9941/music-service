package com.example.musicservice

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.musicservice.fragments.AllFragment
import com.example.musicservice.fragments.MusicFragment
import com.example.musicservice.fragments.PlaylistFragment
import com.example.musicservice.fragments.RecommendedFragment
import kotlinx.android.synthetic.main.music_service.*

class MusicService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_service)

        val musicFragment = MusicFragment()
        val playlistFragment = PlaylistFragment()
        val recommendedFragment = RecommendedFragment()
        val allFragment = AllFragment()

        tab_layout.visibility = View.GONE
        view_pager.visibility = View.GONE
        fragment_container.visibility = View.VISIBLE
        makeCurrentFragment(musicFragment)

        navigation.setOnNavigationItemSelectedListener {
            view_pager.visibility = View.GONE
            fragment_container.visibility = View.VISIBLE
            when (it.itemId) {
                R.id.nav_music -> makeCurrentFragment(musicFragment)
                R.id.nav_playlist -> makeCurrentFragment(playlistFragment)
                R.id.nav_recommended -> makeCurrentFragment(recommendedFragment)
                R.id.nav_all_music -> {
                    makeCurrentFragment(allFragment)
                    /*
                    view_pager.visibility = View.VISIBLE
                    fragment_container.visibility = View.GONE
                    val fragmentAdapter = PageAdapter(supportFragmentManager)
                    view_pager.adapter = fragmentAdapter
                    tab_layout.setupWithViewPager(view_pager)
                    */
                }
            }
            true
        }


    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            supportFragmentManager.executePendingTransactions()
            commit()
        }


    companion object {
        lateinit var imageList: List<Bitmap>
    }
}
