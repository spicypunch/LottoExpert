package kr.jm.feature_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.domain.usecase.GetLottoNumberUseCase
import kr.jm.domain.usecase.SaveLottoItemUseCase
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
            SearchScreenEvent.CloseDialog -> closeDialog()
            is SearchScreenEvent.GetLottoNum -> getLottoNumbers(event.startNumber, event.endNumber)
            is SearchScreenEvent.InsertItem -> insertItem(
                event.name,
                event.startNum,
                event.endNum,
                event.lottoNumbers
            )

            is SearchScreenEvent.UpdateStartNum -> updateStartNum(event.startNum)
            is SearchScreenEvent.UpdateEndNum -> updateEndNum(event.endNum)
            is SearchScreenEvent.ValidateInput -> validateInput(event.startNum, event.endNum)
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

    private fun closeDialog() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                showDialog = false
            )
        }
    }

    private fun getLottoNumbers(startNumber: Int, endNumber: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = getLottoNumberUseCase(startNumber, endNumber)
            result.fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        lottoNumbers = it,
                        isLoading = false,
                        showDialog = true
                    )
                },
                onFailure = {
                    _state.value = _state.value.copy(
                        message = "값을 가져오는데 실패하였습니다",
                        isLoading = false
                    )
                }
            )
        }
    }

    private fun insertItem(
        name: String,
        startNum: String,
        endNum: String,
        lottoNumbers: List<Pair<Int, Int>>
    ) {
        viewModelScope.launch {
            val result = saveLottoItemUseCase(name, startNum, endNum, lottoNumbers)
            result.fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        message = "저장되었습니다"
                    )
                },
                onFailure = {
                    _state.value = _state.value.copy(
                        message = it.message ?: "저장에 실패했습니다"
                    )
                }
            )
        }
    }

    private fun validateInput(startNum: String, endNum: String) {
        when {
            startNum.isBlank() || endNum.isBlank() -> {
                showMessage("빈칸을 채워주세요")
            }
            startNum.toIntOrNull() == null || endNum.toIntOrNull() == null -> {
                showMessage("유효한 숫자를 입력해주세요")
            }
            startNum.toInt() > endNum.toInt() -> {
                showMessage("시작 회차가 끝 회차보다 큽니다")
            }
            else -> {
                onEvent(SearchScreenEvent.GetLottoNum(startNum.toInt(), endNum.toInt()))
            }
        }

    }
}
