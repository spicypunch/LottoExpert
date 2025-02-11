package kr.jm.feature_record

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.usecase.DeleteItemUseCase
import kr.jm.domain.usecase.GetAllItemsUseCase
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
) : ViewModel() {
    private var _state = mutableStateOf(RecordScreenState())
    val state: State<RecordScreenState> = _state

    init {
        onEvent(RecordScreenEvent.LoadItems)
    }

    fun onEvent(event: RecordScreenEvent) {
        when (event) {
            is RecordScreenEvent.ShowMessage -> showMessage(event.message)
            RecordScreenEvent.ClearMessage -> clearMessage()
            is RecordScreenEvent.DeleteItem -> deleteItem(event.item)
            RecordScreenEvent.LoadItems -> getAllItems()
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                message = message
            )
        }
    }

    private fun clearMessage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                message = ""
            )
        }
    }

    private fun getAllItems() {
        viewModelScope.launch {
            getAllItemsUseCase().collect { result ->
                result.fold(
                    onSuccess = {
                        _state.value = _state.value.copy(
                            items = it
                        )
                    },
                    onFailure = {
                        Log.e("RecordViewModel", "getAllItems: ${it.message}")
                    }
                )
            }
        }
    }

    private fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            val result = deleteItemUseCase(itemEntity)
            result.fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        items = _state.value.items.filter { it != itemEntity }
                    )
                },
                onFailure = {
                    Log.e("RecordViewModel", "deleteItem: ${it.message}")
                }
            )
        }
    }
}