package jd.sistemas.android.placeholder_compose.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jd.sistemas.android.placeholder_compose.State
import jd.sistemas.android.placeholder_compose.data.model.User
import jd.sistemas.android.placeholder_compose.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State<User>>(State.START())
    val state: StateFlow<State<User>> = _state

    fun fetchUser(userId: String) = viewModelScope.launch {
        _state.value = State.LOADING()
        try {
            val response = repository.getUser(userId.toInt())
            _state.value = handleResponse(response)
        } catch (e: Exception) {
            _state.value = State.ERROR("Aconteceu algum erro, tente novamente mais tarde!")
        }
    }

    private fun handleResponse(response: Response<User>): State<User> {
        if (response.isSuccessful) {
            response.body()?.let { value ->
                return State.SUCCESS(value)
            }
        }
        return State.ERROR(response.message())
    }
}