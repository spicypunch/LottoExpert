package kr.jm.lottoexpert

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.jm.lottoexpert.repository.RepositoryImpl
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    val progressBarStatus = mutableStateOf(false)

    var lottoNumbers = mutableStateOf(emptyList<Int>())

    private val list: MutableList<Int> = mutableListOf()

    fun getLottoNum() {
        viewModelScope.launch {
            progressBarStatus.value = true
            for (i in 1108..1109) {
                val result = repository.getLottoNum(i.toString())
                list.addAll(
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
            lottoNumbers.value = list
            progressBarStatus.value = false
        }
    }
}
