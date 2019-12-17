package com.example.githubrepos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.githubrepos.database.ReposDatabase
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.network.ReposApi
import com.example.githubrepos.repository.ReposRepository
import com.example.githubrepos.ui.ReposAdapter
import com.example.githubrepos.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ReposAdapter.Interaction {

    private val repository by lazy {
        ReposRepository(ReposApi.githubApis, ReposDatabase.getInstance(application))
    }
    private val viewModel: ReposViewModel by lazy {
        ViewModelProviders.of(this, ReposViewModel.Factory(repository))
            .get(ReposViewModel::class.java)
    }

    private val mAdapter = ReposAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.adapter = mAdapter

        viewModel.repos.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun onItemSelected(position: Int, item: Repo) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(browserIntent)
    }
}
