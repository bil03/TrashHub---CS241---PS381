package com.neisha.trashhub.view.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.neisha.trashhub.R
import com.neisha.trashhub.view.Pickup.PickupActivity
import com.neisha.trashhub.view.Profile.ProfileActivity
import com.neisha.trashhub.view.TrashDetection.PredictionActivity
import com.neisha.trashhub.view.adapter.EducationAdapter
import com.neisha.trashhub.view.adapter.ImageSliderAdapter
import com.neisha.trashhub.view.articles.ArticlesActivity
import com.neisha.trashhub.view.education.DetailEducationActivity
import com.neisha.trashhub.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), EducationAdapter.EducationItemClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var educationList: RecyclerView
    private lateinit var searchBar: EditText
    private val mainViewModel: MainViewModel by viewModels()

    private val images = intArrayOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        educationList = findViewById(R.id.educationList)

        val adapter = ImageSliderAdapter(this, images)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        val educationAdapter = EducationAdapter(listOf(), this)
        educationList.layoutManager = LinearLayoutManager(this)
        educationList.adapter = educationAdapter

        mainViewModel.educationContent.observe(this) { content ->
            educationAdapter.updateData(content)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handling for bottom navigation item clicks
                    true
                }
                R.id.navigation_news -> {
                    val intent = Intent(this, ArticlesActivity::class.java)
                    startActivity(intent)
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

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.fab_menu, popupMenu.menu)

        try {
            val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldPopup.isAccessible = true
            val popup = fieldPopup.get(popupMenu)
            popup?.javaClass?.getDeclaredMethod("setForceShowIcon", Boolean::class.java)?.invoke(popup, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_trash_detection -> {
                    val intent = Intent(this, PredictionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_trash_pickup -> {
                    val intent = Intent(this, PickupActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onEducationItemClicked(educationItem: EducationAdapter.EducationItem) {
        val intent = Intent(this, DetailEducationActivity::class.java)
        intent.putExtra("educationItem", educationItem)
        startActivity(intent)
    }
}
