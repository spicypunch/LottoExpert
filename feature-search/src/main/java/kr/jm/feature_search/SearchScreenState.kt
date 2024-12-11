package kr.jm.feature_search

data class SearchScreenState(
    val isLoading: Boolean = false,
    val lottoNumbers: List<Pair<Int, Int>> = emptyList(),
    val message: String = "",
    val startNum: String = "",
    val endNum: String = "",
    val showDialog: Boolean = false
)

sealed interface SearchScreenEvent {
    data class ShowMessage(val message: String) : SearchScreenEvent
    data object ClearMessage : SearchScreenEvent
    data object CloseDialog: SearchScreenEvent
    data class GetLottoNum(val startNumber: Int, val endNumber: Int) : SearchScreenEvent
    data class InsertItem(
        val name: String,
        val startNum: String,
        val endNum: String,
        val lottoNumbers: List<Pair<Int, Int>>
    ) : SearchScreenEvent
    data class UpdateStartNum(val startNum: String) : SearchScreenEvent
    data class UpdateEndNum(val endNum: String) : SearchScreenEvent
    data class ValidateInput(val startNum: String, val endNum: String) : SearchScreenEvent
}
