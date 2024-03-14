package com.dicoding.githubuserapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.data.response.UserDetailResponse
import com.dicoding.githubuserapp.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: MutableList<UserDetailResponse?>) : ListAdapter<UserDetailResponse, ListUserAdapter.UserViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }
    class UserViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserDetailResponse){
            binding.tvItemFullname.text = user.name
            binding.tvItemUsername.text = user.login
            "${user.publicRepos.toString()} repositories".also { binding.tvItemRepositories.text = it }
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.imgItemPhoto)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserDetailResponse>() {
            override fun areItemsTheSame(oldItem: UserDetailResponse, newItem: UserDetailResponse): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: UserDetailResponse, newItem: UserDetailResponse): Boolean {
                return oldItem.login == newItem.login && oldItem.name == newItem.name
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserDetailResponse?)
    }
}