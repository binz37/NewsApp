package com.id.newsapp.handler

import android.view.View

interface IArticleListener {
    fun onItemClicked(position: Int, view: View)
}