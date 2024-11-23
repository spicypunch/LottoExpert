package kr.jm.lottoexpert.ui.screen.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.repository.RepositoryImpl
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    val progressBarStatus = mutableStateOf(false)

    private var _lottoNumbers = mutableStateOf(emptyList<Pair<Int, Int>>())
    val lottoNumbers: State<List<Pair<Int, Int>>> = _lottoNumbers

//    private var _message = mutableStateOf("")
//    val message: State<String> = _message

//    private var recommendNumbers = mutableStateOf(emptyList<Int>())

    private val getLottoList: MutableList<Int> = mutableListOf()
    private var mostFrequentNumbers: List<Pair<Int, Int>> = mutableListOf()


    fun getLottoNum(startNumber: Int, endNumber: Int) {
        viewModelScope.launch {
            progressBarStatus.value = true
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
                _lottoNumbers.value = mostFrequentNumbers
            } catch (e: Exception) {
                Log.e("SearchViewModel", "getLottoNum: $e")
//                _message.value ="값을 가져오는데 실패하였습니다"
            } finally {
                progressBarStatus.value = false
            }
        }
    }

    fun insertItem(name: String,
                   startNum: String,
                   endNum: String,
                   lottoNumbers: List<Pair<Int, Int>>
    ) {
        viewModelScope.launch {
            if (isItemExists(startNum, endNum)) {
                Log.i("maru", "이미 저장 마루")
            } else {
                val itemEntity = ItemEntity(
                    name = name,
                    startNum = startNum,
                    endNum = endNum,
                    lottoNumbers = lottoNumbers
                )
                try {
                    repository.insertItem(itemEntity)
                } catch (e: Exception) {
                    Log.e("SearchViewModel", "insertItem: $e")
                }
            }
        }
    }

    private suspend fun isItemExists(startNum: String, endNum: String): Boolean {
        return repository.getItem(startNum, endNum).firstOrNull() != null
    }

//    fun recommendLottoNumbers() {
//        viewModelScope.launch {
//            progressBarStatus.value = true
//            val lottoNumbers: List<Int> = mostFrequentNumbers.map { it.first }
//            val randomNumbers: List<Int> = lottoNumbers.shuffled().take(6)
//            recommendNumbers.value = randomNumbers
//            progressBarStatus.value = false
//        }
//    }
}
