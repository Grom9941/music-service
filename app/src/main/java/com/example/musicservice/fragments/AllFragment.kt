package com.example.musicservice.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.musicservice.R
import com.example.musicservice.adapters.PageAdapter
import kotlinx.android.synthetic.main.music_service.*

/**
 * A simple [Fragment] subclass.
 */
class AllFragment : Fragment() {

    private var flag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_music, container, false)

        Toast.makeText(this.context, "music", Toast.LENGTH_SHORT).show()

        activity!!.view_pager.visibility = View.VISIBLE
        activity!!.fragment_container.visibility = View.GONE

        if (!flag) {
            val fragmentAdapter = PageAdapter(activity!!.supportFragmentManager)
            activity!!.view_pager.adapter = fragmentAdapter
            activity!!.tab_layout.setupWithViewPager(view_pager)
            flag = true
        }
        return view
    }
}
