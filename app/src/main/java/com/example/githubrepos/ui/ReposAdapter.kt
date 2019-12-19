package com.example.githubrepos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubrepos.R
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.util.formatServerDate
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repo>() {

        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RepoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repo,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RepoViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Repo>) {

        differ.submitList(list)
    }

    fun addItems(list: List<Repo>) {

        val newList = mutableListOf<Repo>()
        newList.addAll(differ.currentList)
        newList.addAll(list)
        differ.submitList(newList)
    }

    class RepoViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Repo) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            titleTextView.text = item.title

            Glide.with(imageView.context).load(item.owner?.avatarUrl).into(imageView)

            forkTextView.text = item.forksCount?.toString()

            describtionTextView.text = item.description

            dateTextView.text = item.creationDate?.let { formatServerDate(it) }

        }
    }
    

    interface Interaction {
        fun onItemSelected(position: Int, item: Repo)
    }
}

