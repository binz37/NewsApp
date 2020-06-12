package com.id.newsapp.news_by_category

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.newsapp.R
import com.id.newsapp.adapter.NewsAdapter
import com.id.newsapp.api_consume.APIClient
import com.id.newsapp.api_consume.NewsManager
import com.id.newsapp.base.BaseActivity
import com.id.newsapp.base.EndlessOnScrollListener
import com.id.newsapp.common.Common
import com.id.newsapp.data.ArticleData
import com.id.newsapp.data.ResponseData
import com.id.newsapp.handler.IArticleListener
import com.id.newsapp.home.HomeCategoryListActivity
import com.id.newsapp.news_detail.NewsDetailActivity
import kotlinx.android.synthetic.main.activity_show_news_by_category.*
import kotlinx.android.synthetic.main.home_category_list_activity.recyclerView
import kotlinx.android.synthetic.main.home_category_list_activity.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.MessageFormat

class ShowNewsByCategoryActivity : BaseActivity() {
    companion object {
        const val TARGET_URL_KEY = "targetURLKey"
        const val STATE_SEARCH = "onSearching"
        const val STATE_NORMAL = "normal"
    }

    private var dataAdapter: MutableList<ArticleData> = ArrayList()
    private lateinit var adapter: NewsAdapter
    private var categoryId = ""
    private var currentState = ""
    private var searchKeyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_show_news_by_category)
            getPassingDataFromIntent()
            initComponent()
            getDataByCategoryFromServer(categoryId, 1)
        } catch (e: Exception) {
            showErrorDialog(e)
        }
    }

    private fun setToolbarTitle() {
        val message = MessageFormat.format(
            getString(R.string.category_id_list_title),
            categoryId
        )
        initActionBar(toolbar, message)
    }

    private fun getPassingDataFromIntent() {
        val bundle = intent.extras
        if (bundle != null) {
            categoryId = bundle.getString(HomeCategoryListActivity.CATEGORY_ID_KEY, "")
        }
    }

    private fun initComponent() {
        setToolbarTitle()
        setTextOnKeyListener()
        initAdapter()
        setTextChangeListener()
        initButtonHandler()
    }

    private fun initAdapter() {
        adapter = NewsAdapter(applicationContext, dataAdapter, setAdapterListener())
        val linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(scrollData())
        dataAdapter.clear()
    }

    private fun setTextOnKeyListener() {
        txtSearch.setOnKeyListener { _: View?, keyCode: Int, event: KeyEvent ->
            try {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_ENTER -> {
                            val keyword = txtSearch.text.toString()
                            if (keyword.isNotEmpty()) {
                                searchKeyword = keyword
                                dataAdapter.clear()
                                pageNumber = 1
                                searchArticleByKeywordFromServer(keyword, pageNumber)
                            } else {
                                searchKeyword = ""
                                dataAdapter.clear()
                                loadArticleByCategoryFromBeginning()
                            }
                            hideSoftInputKeyboard()
                        }
                        else -> if (txtSearch.text.toString().trim() != "") {
                            btnClearSearch.visibility = View.VISIBLE
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                showErrorDialog(e)
            }
            false
        }
    }

    private fun setTextChangeListener() {
        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btnClearSearch.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {
                val key = s.toString().trim()
                if (key != "") {
                    btnClearSearch.visibility = View.VISIBLE
                } else {
                    btnClearSearch.visibility = View.GONE
                    txtSearch.clearFocus()
                    hideSoftInputKeyboard()
                }
            }
        })
    }

    private fun initButtonHandler() {
        btnClearSearch.setOnClickListener {
            try {
                txtSearch.setText("")
                searchKeyword = ""
                loadArticleByCategoryFromBeginning()
            } catch (e: Exception) {
                showErrorDialog(e)
            }
        }
    }

    private fun scrollData(): EndlessOnScrollListener {
        return object : EndlessOnScrollListener() {
            override fun onLoadMore() {
                pageNumber += 1
                if (currentState == STATE_NORMAL) {
                    getDataByCategoryFromServer(categoryId, pageNumber)
                } else {
                    searchArticleByKeywordFromServer(searchKeyword, pageNumber)
                }
            }
        }
    }

    private fun setAdapterListener(): IArticleListener {
        return object : IArticleListener {
            override fun onItemClicked(position: Int, view: View) {
                try {
                    val selected = dataAdapter[position]
                    showDetailNews(selected.url)
                } catch (e: Exception) {
                    showErrorDialog(e)
                }
            }
        }
    }

    private fun showDetailNews(targetURL: String) {
        val intent = Intent(applicationContext, NewsDetailActivity::class.java)
        intent.putExtra(TARGET_URL_KEY, targetURL)
        startActivity(intent)
    }

    private fun setProgressBar(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun loadArticleByCategoryFromBeginning() {
        getDataByCategoryFromServer(categoryId, 1)
        pageNumber = 1
    }

    private fun searchArticleByKeywordFromServer(keyword: String, pageNumber: Int) {
        setProgressBar(true)
        val callBackAPI: NewsManager = APIClient.getClient().create(NewsManager::class.java)
        val call = callBackAPI.searchAnyArticle(
            keyword,
            10,
            pageNumber,
            Common.NEWS_API_KEY
        )

        call.enqueue(object : Callback<ResponseData> {
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                setProgressBar(false)
                showErrorDialog(t.message!!)
            }

            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                setProgressBar(false)
                currentState = STATE_SEARCH
                if (response.body()!!.status == "ok") {
                    val articleList = response.body()!!.articles
                    dataAdapter.addAll(articleList)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private var pageNumber = 1

    private fun getDataByCategoryFromServer(categoryId: String, pageNumber: Int) {
        setProgressBar(true)
        val callBackAPI: NewsManager = APIClient.getClient().create(NewsManager::class.java)
        val call = callBackAPI.getArticleListByCategoryId(
            categoryId,
            10,
            pageNumber,
            Common.NEWS_API_KEY
        )

        call.enqueue(object : Callback<ResponseData> {
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                setProgressBar(false)
                showErrorDialog(t.message!!)
            }

            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                setProgressBar(false)
                currentState = STATE_NORMAL
                if (response.body()!!.status == "ok") {
                    val articleList = response.body()!!.articles
                    dataAdapter.addAll(articleList)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}