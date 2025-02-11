package kr.jm.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.repository.Repository
import javax.inject.Inject

class SaveLottoItemUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        name: String,
        startNum: String,
        endNum: String,
        lottoNumbers: List<Pair<Int, Int>>
    ): Result<Unit> {
        return try {
            if (repository.getItem(startNum, endNum).firstOrNull() != null) {
                return Result.failure(Exception("이미 저장한 회차입니다"))
            }

            val itemEntity = ItemEntity(
                name = name,
                startNum = startNum,
                endNum = endNum,
                lottoNumbers = lottoNumbers
            )
            repository.insertItem(itemEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}