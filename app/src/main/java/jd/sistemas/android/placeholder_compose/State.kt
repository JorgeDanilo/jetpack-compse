package jd.sistemas.android.placeholder_compose

sealed class State<T>(
    val data: T? = null,
    val message: String? = null
) {
    class START<T> : State<T>()
    class SUCCESS<T>(data: T) : State<T>(data)
    class ERROR<T>(message: String, data: T? = null) : State<T>(data, message)
    class LOADING<T> : State<T>()
}