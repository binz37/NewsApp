package com.id.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.id.newsapp.R
import com.id.newsapp.data.ArticleData
import com.id.newsapp.handler.IArticleListener
import kotlinx.android.synthetic.main.card_article.view.*
import java.lang.Exception

class NewsAdapter(
    private val context: Context,
    private val dataList: List<ArticleData>,
    private val listener: IArticleListener?
) : RecyclerView.Adapter<NewsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_article, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val selectedArticle = dataList[position]
        holder.view.tvTitle.text = selectedArticle.title
        holder.view.tvDescription.text = selectedArticle.description
        holder.view.articleParent.tag = selectedArticle

        holder.view.articleParent.setOnClickListener { view ->
            try {
                listener!!.onItemClicked(position, view)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}