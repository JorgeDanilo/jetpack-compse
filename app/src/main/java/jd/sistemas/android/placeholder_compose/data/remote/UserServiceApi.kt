package jd.sistemas.android.placeholder_compose.data.remote

import jd.sistemas.android.placeholder_compose.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserServiceApi {

    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/users/{userId}")
    suspend fun getUser(
        @Path(value = "userId") userId: Int
    ): Response<User>
}
