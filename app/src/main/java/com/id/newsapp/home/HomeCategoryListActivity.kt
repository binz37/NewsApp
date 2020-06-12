package com.id.newsapp.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.newsapp.R
import com.id.newsapp.base.BaseActivity
import com.id.newsapp.home.data.NewsCategoryData
import com.id.newsapp.home.handler.ICategory
import com.id.newsapp.news_by_category.ShowNewsByCategoryActivity
import kotlinx.android.synthetic.main.home_category_list_activity.*


class HomeCategoryListActivity : BaseActivity() {
    companion object {
        const val CATEGORY_ID_KEY = "categoryIdKey"
    }

    private var dataAdapter: MutableList<NewsCategoryData> = ArrayList()
    private lateinit var adapter: NewsCategoryAdapter
    private var isPressedTwice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.home_category_list_activity)

            initComponent()
            setMenuList()
        } catch (e: Exception) {
            showErrorDialog(e)
        }
    }

    override fun onBackPressed() {
        handleBackPressed()
    }

    private fun handleBackPressed() {
        if (isPressedTwice) {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
            finishAffinity()
        }

        isPressedTwice = true
        Toast.makeText(applicationContext, getString(R.string.press_twice_to_exit), Toast.LENGTH_LONG).show()
        Handler().postDelayed({ isPressedTwice = false }, 2000)
    }

    private fun initAdapter() {
        adapter = NewsCategoryAdapter(applicationContext, dataAdapter, setAdapterListener())
    }

    private fun setAdapterListener(): ICategory {
        return object : ICategory {
            override fun onCategoryClicked(position: Int) {
                try {
                    val selected = dataAdapter[position]
                    showNewsyCategoryActivity(selected.title)
                } catch (e: Exception) {
                    showErrorDialog(e)
                }
            }
        }
    }

    private fun showNewsyCategoryActivity(category: String) {
        val intent = Intent(applicationContext, ShowNewsByCategoryActivity::class.java)
        intent.putExtra(CATEGORY_ID_KEY, category)
        startActivity(intent)
    }

    private fun initComponent() {
        initActionBar(toolbar, resources.getString(R.string.category_list_title))
        initAdapter()
        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    private fun setMenuList() {
        val menuList = getMenuList()
        dataAdapter.clear()
        dataAdapter.addAll(menuList)
        adapter.notifyDataSetChanged()
    }

    private fun getMenuList(): List<NewsCategoryData>{
        val list: MutableList<NewsCategoryData> = ArrayList()
        val business = NewsCategoryData()
        business.title = "Business"
        business.resourceId = R.drawable.ic_bussiness
        list.add(business)

        val entertainment = NewsCategoryData()
        entertainment.title = "Entertainment"
        entertainment.resourceId = R.drawable.ic_entertainment
        list.add(entertainment)

        val general = NewsCategoryData()
        general.title = "General"
        general.resourceId = R.drawable.ic_general
        list.add(general)

        val health = NewsCategoryData()
        health.title = "Health"
        health.resourceId = R.drawable.ic_health
        list.add(health)

        val science = NewsCategoryData()
        science.title = "Science"
        science.resourceId = R.drawable.ic_science
        list.add(science)

        val sports = NewsCategoryData()
        sports.title = "Sports"
        sports.resourceId = R.drawable.ic_sport
        list.add(sports)

        val technology = NewsCategoryData()
        technology.title = "Technology"
        technology.resourceId = R.drawable.ic_technology
        list.add(technology)

        return list
    }
}