package jd.sistemas.android.placeholder_compose.ui.list

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
class MainViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State<List<User>>>(State.START())
    val state: StateFlow<State<List<User>>> = _state

    init {
        loadUser()
    }

    private fun loadUser() = viewModelScope.launch {
        _state.value = State.LOADING()
        try {
            val response = repository.getUser()
            _state.value = handleResponse(response)
        } catch (e: Exception) {
            _state.value = State.ERROR("Aconteceu algum erro, tente novamente mais tarde!")
        }
    }

    private fun handleResponse(response: Response<List<User>>): State<List<User>> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return State.SUCCESS(values)
            }
        }
        return State.ERROR(response.message())
    }
}