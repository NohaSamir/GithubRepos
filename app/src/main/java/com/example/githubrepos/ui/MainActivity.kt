package com.example.githubrepos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.example.githubrepos.R
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ReposAdapter.Interaction {


    private val viewModel: ReposViewModel by lazy {
        ViewModelProviders.of(this)
            .get(ReposViewModel::class.java)
    }

    private val mAdapter = ReposAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.adapter = mAdapter

        viewModel.repos.observe(this, Observer<PagedList<Repo>> {
            mAdapter.submitList(it)
        })

        viewModel.networkErrors.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onItemSelected(position: Int, item: Repo) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(browserIntent)
    }
}
