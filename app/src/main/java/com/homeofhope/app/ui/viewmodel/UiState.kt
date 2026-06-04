package com.homeofhope.app.ui.viewmodel

sealed class UiState {
    object Idle    : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}
