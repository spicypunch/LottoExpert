package kr.jm.feature_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.domain.usecase.GetLottoNumberUseCase
import kr.jm.domain.usecase.SaveLottoItemUseCase
import kr.jm.domain.util.Result
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getLottoNumberUseCase: GetLottoNumberUseCase,
    private val saveLottoItemUseCase: SaveLottoItemUseCase,
) : ViewModel() {
    private var _state = mutableStateOf(SearchScreenState())
    val state: State<SearchScreenState> = _state

    fun onEvent(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.ShowMessage -> showMessage(event.message)
            SearchScreenEvent.ClearMessage -> clearMessage()
            is SearchScreenEvent.GetLottoNum -> getLottoNumbers(event.startNumber, event.endNumber)
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

    private fun getLottoNumbers(startNumber: Int, endNumber: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = getLottoNumberUseCase(startNumber, endNumber)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        lottoNumbers = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        message = "값을 가져오는데 실패하였습니다",
                        isLoading = false
                    )
                }
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
            when (val result = saveLottoItemUseCase(name, startNum, endNum, lottoNumbers)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        message = "저장되었습니다"
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        message = result.exception.message ?: "저장에 실패했습니다"
                    )
                }
            }
        }
    }
}
