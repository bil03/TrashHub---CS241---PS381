package com.neisha.trashhub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neisha.trashhub.data.pref.response.ArticleResultsItem
import com.neisha.trashhub.databinding.ArticlesItemBinding

class ArticleAdapter(
    private var currentArticles: List<ArticleResultsItem>,
    private val onItemClick: (ArticleResultsItem) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ArticlesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleResultsItem) {
            binding.articleTitle.text = article.title
            binding.articleDate.text = article.date
            binding.articleDescription.text = if (article.content.length > 100) {
                "${article.content.substring(0, 100)}..."
            } else {
                article.content
            }
            Glide.with(binding.root.context)
                .load(article.image)
                .into(binding.articleImage)
            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ArticlesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(currentArticles[position])
    }

    override fun getItemCount(): Int = currentArticles.size

    fun updateArticles(newArticles: List<ArticleResultsItem>) {
        val diffCallback = ArticlesDiffCallback(currentArticles, newArticles)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        currentArticles = newArticles
        diffResult.dispatchUpdatesTo(this)
    }

    class ArticlesDiffCallback(
        private val oldList: List<ArticleResultsItem>,
        private val newList: List<ArticleResultsItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
