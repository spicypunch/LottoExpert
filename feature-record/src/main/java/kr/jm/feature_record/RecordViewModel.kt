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
import kr.jm.domain.util.Result
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
                when (result) {
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            items = result.data
                        )
                    }
                    is Result.Error -> {
                        Log.e("RecordViewModel", "getAllItems: ${result.exception.message}")
                    }
                }
            }
        }
    }

    private fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            when (val result = deleteItemUseCase(itemEntity)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        message = "삭제되었습니다"
                    )
                }

                is Result.Error -> {
                    Log.e("RecordViewModel", "deleteItem: ${result.exception.message}")
                }
            }
        }
    }
}