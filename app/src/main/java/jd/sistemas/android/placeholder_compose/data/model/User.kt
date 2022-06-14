package jd.sistemas.android.placeholder_compose.data.model

import java.io.Serializable

data class User(
    val id: Int,
    val name: String,
    val email: String
) : Serializable
