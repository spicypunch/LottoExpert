package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.repository.Repository
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Result<List<ItemEntity>>> {
        return repository.getAllItems()
            .map { items ->
                Result.success(items)
            }
            .catch { e ->
                emit(Result.failure(Exception(e)))
            }
    }
}