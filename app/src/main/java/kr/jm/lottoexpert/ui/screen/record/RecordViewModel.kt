package kr.jm.lottoexpert.ui.screen.record

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {
    private var _state = mutableStateOf(RecordScreenState())
    val state: State<RecordScreenState> = _state

    init {
        onEvent(RecordScreenEvent.LoadItems)
    }

    fun onEvent(event: RecordScreenEvent) {
        when (event) {
            is RecordScreenEvent.DeleteItem -> deleteItem(event.item)
            RecordScreenEvent.LoadItems -> getAllItems()
        }
    }

    private fun getAllItems() {
        viewModelScope.launch {
            try {
                repository.getAllItems().collect() { result ->
                    _state.value = _state.value.copy(
                        items = result
                    )
                }
            } catch (e:Exception) {
                Log.e("RecordViewModel", "getAllItems: $e")
            }
        }
    }

    private fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            try {
                repository.deleteItem(itemEntity)
            } catch (e: Exception) {
                Log.e("RecordViewModel", "deleteItem: $e")
            }
        }
    }
}