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

    private var _allItems = mutableStateOf(emptyList<ItemEntity>())
    val allItems: State<List<ItemEntity>> = _allItems

    fun getAllItems() {
        viewModelScope.launch {
            try {
                repository.getAllItems().collect() { result ->
                    _allItems.value = result
                }
            } catch (e:Exception) {
                Log.e("RecordViewModel", "getAllItems: $e")
            }
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            try {
                repository.deleteItem(itemEntity)
            } catch (e: Exception) {
                Log.e("RecordViewModel", "deleteItem: $e")
            }
        }
    }
}