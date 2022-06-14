package jd.sistemas.android.placeholder_compose.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    @SerializedName("data")
    var data: List<User>

)
