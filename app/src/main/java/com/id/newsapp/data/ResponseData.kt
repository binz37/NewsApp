package com.id.newsapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseData: Serializable {
    @SerializedName("status")
    var status : String = ""
    @SerializedName("totalResults")
    var totalResults : Int = 0
    @SerializedName("articles")
    var articles : MutableList<ArticleData> = ArrayList()
}