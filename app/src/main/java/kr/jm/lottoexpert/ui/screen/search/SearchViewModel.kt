package kr.jm.lottoexpert.ui.screen.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {
    private var _state = mutableStateOf(SearchScreenState())
    val state: State<SearchScreenState> = _state

    private val getLottoList: MutableList<Int> = mutableListOf()
    private var mostFrequentNumbers: List<Pair<Int, Int>> = mutableListOf()

    fun onEvent(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.ShowMessage -> showMessage(event.message)
            SearchScreenEvent.ClearMessage -> clearMessage()
            is SearchScreenEvent.GetLottoNum -> getLottoNum(event.startNumber, event.endNumber)
            is SearchScreenEvent.InsertItem -> insertItem(
                event.name,
                event.startNum,
                event.endNum,
                event.lottoNumbers
            )
            is SearchScreenEvent.UpdateStartNum -> updateStartNum(event.startNum)
            is SearchScreenEvent.UpdateEndNum -> updateEndNum(event.endNum)
        }

    }

    private fun updateStartNum(value: String) {
        _state.value = _state.value.copy(
            startNum = value
        )
    }

    private fun updateEndNum(value: String) {
        _state.value = _state.value.copy(
            endNum = value
        )
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

    private fun getLottoNum(startNumber: Int, endNumber: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            try {
                getLottoList.clear()
                val deferredResults = (startNumber..endNumber).map { drawNumber ->
                    async { repository.getLottoNum(drawNumber.toString()) }
                }
                val results = deferredResults.awaitAll()
                results.forEach { result ->
                    getLottoList.addAll(
                        listOf(
                            result.drwtNo1,
                            result.drwtNo2,
                            result.drwtNo3,
                            result.drwtNo4,
                            result.drwtNo5,
                            result.drwtNo6
                        )
                    )
                }
                mostFrequentNumbers = getLottoList.groupingBy { it }.eachCount()
                    .toList()
                    .sortedByDescending { it.second }
                _state.value = _state.value.copy(
                    lottoNumbers = mostFrequentNumbers
                )
            } catch (e: Exception) {
                Log.e("SearchViewModel", "getLottoNum: $e")
                showMessage("값을 가져오는데 실패하였습니다")
            } finally {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun insertItem(
        name: String,
        startNum: String,
        endNum: String,
        lottoNumbers: List<Pair<Int, Int>>
    ) {
        viewModelScope.launch {
            if (isItemExists(startNum, endNum)) {
                showMessage("이미 저장한 회차입니다")
            } else {
                val itemEntity = ItemEntity(
                    name = name,
                    startNum = startNum,
                    endNum = endNum,
                    lottoNumbers = lottoNumbers
                )
                try {
                    repository.insertItem(itemEntity)
                    showMessage("저장되었습니다")
                } catch (e: Exception) {
                    Log.e("SearchViewModel", "insertItem: $e")
                }
            }
        }
    }

    private suspend fun isItemExists(startNum: String, endNum: String): Boolean {
        return repository.getItem(startNum, endNum).firstOrNull() != null
    }
}
