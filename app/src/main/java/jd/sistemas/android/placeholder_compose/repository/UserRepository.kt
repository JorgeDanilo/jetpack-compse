package jd.sistemas.android.placeholder_compose.repository

import jd.sistemas.android.placeholder_compose.data.remote.UserServiceApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserServiceApi
) {
    suspend fun getUser() = api.getUsers()
    suspend fun getUser(userId: Int) = api.getUser(userId)
}