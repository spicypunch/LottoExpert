package kr.jm.lottoexpert.ui.screen.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    val progressBarStatus = mutableStateOf(false)

    var lottoNumbers = mutableStateOf(emptyList<Pair<Int, Int>>())

    var recommendNumbers = mutableStateOf(emptyList<Int>())

    private val getLottoList: MutableList<Int> = mutableListOf()
    private var mostFrequentNumbers: List<Pair<Int, Int>> = mutableListOf()

    fun getLottoNum(startDrawerNumber: Int, endDrawerNumber: Int) {
        viewModelScope.launch {
            progressBarStatus.value = true
            getLottoList.clear()
            for (i in startDrawerNumber..endDrawerNumber) {
                val result = repository.getLottoNum(i.toString())
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
                .take(20)

            lottoNumbers.value = mostFrequentNumbers
            progressBarStatus.value = false
        }
    }

    fun recommendLottoNumbers() {
        viewModelScope.launch {
            progressBarStatus.value = true
            val lottoNumbers: List<Int> = mostFrequentNumbers.map { it.first }
            val randomNumbers: List<Int> = lottoNumbers.shuffled().take(6)
            recommendNumbers.value = randomNumbers
            progressBarStatus.value = false
        }
    }
}
