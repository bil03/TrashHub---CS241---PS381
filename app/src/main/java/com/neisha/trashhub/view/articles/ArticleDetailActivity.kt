package com.neisha.trashhub.view.articles

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.neisha.trashhub.R
import com.neisha.trashhub.databinding.ActivityDetailArticlesBinding
import com.neisha.trashhub.viewmodel.ViewModelFactory

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticlesBinding
    private val articleDetailViewModel: ArticleDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getStringExtra("ARTICLE_ID") ?: return

        articleDetailViewModel.article.observe(this) { article ->
            binding.articleTitle.text = article.title
            binding.articleAuthorDate.text = getString(R.string.article_author_date, article.author, article.date)
            binding.articleContent.text = article.content
            Glide.with(this)
                .load(article.image)
                .into(binding.articleImage)
        }

        articleDetailViewModel.errorMessage.observe(this) { errorMessage ->
            // Handle error message here
        }

        articleDetailViewModel.fetchArticleDetail(articleId)
    }
}
