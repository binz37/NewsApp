package com.id.newsapp.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessOnScrollListener : RecyclerView.OnScrollListener() {
    private var totalPrevious = 0
    private var isLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItem = recyclerView.childCount
        val totalItem = recyclerView.layoutManager?.itemCount
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (isLoading) {
            if (totalItem!! > totalPrevious) {
                isLoading = false
                totalPrevious = totalItem
            }
        }
        val visibleThreshold = 5

        if (!isLoading && ((totalItem!! - visibleItem) <= (firstVisibleItem + visibleThreshold))) {
            onLoadMore();
            isLoading = true;
        }
    }

    abstract fun onLoadMore()
}