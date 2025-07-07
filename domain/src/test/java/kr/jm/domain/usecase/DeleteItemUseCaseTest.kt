package kr.jm.domain.usecase

import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.repository.Repository
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteItemUseCaseTest {

    private val mockRepository = mock<Repository>()
    private val useCase = DeleteItemUseCase(mockRepository)

    @Test
    fun `invoke should return success when repository deletes item successfully`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )

        // When
        val result = useCase(itemEntity)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).deleteItem(itemEntity)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )
        val exception = RuntimeException("Database error")
        
        // Mock the repository to throw an exception
        // Note: We can't use whenever with suspend functions that return Unit directly
        // Instead, we test the exception handling in the actual implementation

        // When & Then
        // Since we can't easily mock suspend Unit functions to throw exceptions with mockito-kotlin
        // we'll test this scenario by creating a custom test where the repository method throws
        val testRepository = object : Repository {
            override suspend fun getLottoNum(drawNumber: String) = throw NotImplementedError()
            override suspend fun getItem(startNum: String, endNum: String) = throw NotImplementedError()
            override suspend fun getAllItems() = throw NotImplementedError()
            override suspend fun insertItem(itemEntity: ItemEntity) = throw NotImplementedError()
            override suspend fun deleteItem(itemEntity: ItemEntity) {
                throw exception
            }
        }
        
        val testUseCase = DeleteItemUseCase(testRepository)
        val result = testUseCase(itemEntity)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke should handle item with null id`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = null,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )

        // When
        val result = useCase(itemEntity)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).deleteItem(itemEntity)
    }

    @Test
    fun `invoke should handle item with empty lotto numbers`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = emptyList()
        )

        // When
        val result = useCase(itemEntity)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).deleteItem(itemEntity)
    }
}