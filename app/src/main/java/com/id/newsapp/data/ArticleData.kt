package com.id.newsapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ArticleData : Serializable {
    @SerializedName("source")
    var source: SourceData? = null

    @SerializedName("author")
    var author: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("url")
    var url: String = ""

    @SerializedName("urlToImage")
    var urlToImage: String = ""

    @SerializedName("publishedAt")
    var publishedAt: String = ""

    @SerializedName("content")
    var content: String = ""

}