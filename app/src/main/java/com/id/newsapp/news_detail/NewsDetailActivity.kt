package com.id.newsapp.news_detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.id.newsapp.R
import com.id.newsapp.base.BaseActivity
import com.id.newsapp.news_by_category.ShowNewsByCategoryActivity
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : BaseActivity() {
    private var targetUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        try{
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_news_detail)
            setToolbarTitle()
            getPassingDataFromIntent()
            showNewsToUi(targetUrl)
        } catch (e: Exception) {
            showErrorDialog(e)
        }
    }

    private fun setToolbarTitle() {
        initActionBar(toolbar, getString(R.string.news_detail_title))
    }

    private fun getPassingDataFromIntent() {
        val bundle = intent.extras
        if (bundle != null) {
            targetUrl = bundle.getString(ShowNewsByCategoryActivity.TARGET_URL_KEY, "")
        }
    }

    private fun showNewsToUi(targetUrl: String) {
        webView.webViewClient = getWebViewClient()
        webView.settings.defaultTextEncodingName = "utf-8"
        webView.loadUrl(targetUrl)
    }

    private fun getWebViewClient(): WebViewClient{
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showErrorDialog(error.toString())
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                showErrorDialog(errorResponse.toString())
            }
        }
    }
}