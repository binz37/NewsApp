package com.id.newsapp.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.id.newsapp.R
import com.id.newsapp.home.data.NewsCategoryData
import com.id.newsapp.home.handler.ICategory
import kotlinx.android.synthetic.main.card_news_category.view.*
import java.lang.Exception

class NewsCategoryAdapter (
    private val context: Context,
    private val dataList: List<NewsCategoryData>,
    private val listener: ICategory?
) : RecyclerView.Adapter<NewsCategoryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_news_category, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val category = dataList[position]
        holder.view.tvTitle.text = category.title
        holder.view.imgCategory.setImageResource(category.resourceId)
        holder.view.articleParent.tag = category

        holder.view.articleParent.setOnClickListener {
            try {
                listener!!.onCategoryClicked(position)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}