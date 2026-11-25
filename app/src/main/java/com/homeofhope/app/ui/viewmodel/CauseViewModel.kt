package com.homeofhope.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeofhope.app.data.model.Cause
import com.homeofhope.app.data.repository.CauseRepository
import kotlinx.coroutines.launch

data class CauseUiState(
    val causes: List<Cause> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CauseViewModel(
    private val repository: CauseRepository
) : ViewModel() {

    var uiState by mutableStateOf(CauseUiState(isLoading = true))
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                uiState = CauseUiState(
                    causes = repository.getCauses(),
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unable to load causes."
                )
            }
        }
    }

    fun findCause(causeId: Int): Cause? {
        return uiState.causes.firstOrNull { it.id == causeId }
    }
}
