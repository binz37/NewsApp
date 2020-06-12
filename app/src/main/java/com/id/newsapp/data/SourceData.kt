package com.id.newsapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SourceData : Serializable {
    @SerializedName("id")
    var id : String =""
    @SerializedName("name")
    var name : String = ""
}