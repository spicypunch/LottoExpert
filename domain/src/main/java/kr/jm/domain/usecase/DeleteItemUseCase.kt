package kr.jm.domain.usecase

import kr.jm.domain.model.ItemEntity
import kr.jm.domain.repository.Repository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(itemEntity: ItemEntity): Result<Unit> {
        return try {
            repository.deleteItem(itemEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}