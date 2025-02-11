package kr.jm.domain.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kr.jm.domain.repository.Repository
import javax.inject.Inject

class GetLottoNumberUseCase @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(startNumber: Int, endNumber: Int): Result<List<Pair<Int, Int>>> {
        return try {
            val lottoNumbers = mutableListOf<Int>()

            // API 병렬 호출
            val deferredResults = (startNumber..endNumber).map { drawNumber ->
                coroutineScope {
                    async { repository.getLottoNum(drawNumber.toString()) }
                }
            }

            // 결과 수집 및 처리
            val results = deferredResults.awaitAll()
            results.forEach { result ->
                lottoNumbers.addAll(
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

            // 빈도수 계산
            val frequentNumbers = lottoNumbers.groupingBy { it }
                .eachCount()
                .toList()
                .sortedByDescending { it.second }

            Result.success(frequentNumbers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}