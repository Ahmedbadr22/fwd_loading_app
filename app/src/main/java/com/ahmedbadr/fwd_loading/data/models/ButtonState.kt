package com.ahmedbadr.fwd_loading.data.models


sealed class ButtonState {
    object Ideal : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}