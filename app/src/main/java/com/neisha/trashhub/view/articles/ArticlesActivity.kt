package com.neisha.trashhub.view.articles

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.neisha.trashhub.R
import com.neisha.trashhub.view.Profile.ProfileActivity
import com.neisha.trashhub.view.adapter.ArticleAdapter
import com.neisha.trashhub.view.main.MainActivity
import com.neisha.trashhub.viewmodel.ViewModelFactory

class ArticlesActivity : AppCompatActivity() {

    private val articlesViewModel: ArticlesViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(emptyList()) { article ->
            val intent = Intent(this, ArticleDetailActivity::class.java).apply {
                putExtra("ARTICLE_ID", article.id)
            }
            startActivity(intent)
        }
        recyclerView.adapter = articleAdapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_news -> {

                    true
                }
                R.id.navigation_account -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    articlesViewModel.searchArticles(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })

        articlesViewModel.articles.observe(this, Observer { articles ->
            articleAdapter.updateArticles(articles)
        })

        articlesViewModel.errorMessage.observe(this, Observer { errorMessage ->

        })

        articlesViewModel.fetchArticles()
    }
}
