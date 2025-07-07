package kr.jm.domain.usecase

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.repository.Repository
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetAllItemsUseCaseTest {

    private val mockRepository = mock<Repository>()
    private val useCase = GetAllItemsUseCase(mockRepository)

    @Test
    fun `invoke should return success result with items when repository returns valid data`() = runTest {
        // Given
        val mockItems = listOf(
            ItemEntity(
                id = 1,
                name = "Test Item 1",
                startNum = "1",
                endNum = "10",
                lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
            ),
            ItemEntity(
                id = 2,
                name = "Test Item 2",
                startNum = "11",
                endNum = "20",
                lottoNumbers = listOf(4 to 3, 5 to 2, 6 to 1)
            )
        )
        
        whenever(mockRepository.getAllItems()).thenReturn(flowOf(mockItems))

        // When
        val resultFlow = useCase()
        val results = resultFlow.toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(mockItems, results[0].getOrNull())
    }

    @Test
    fun `invoke should return success result with empty list when repository returns empty data`() = runTest {
        // Given
        val emptyList = emptyList<ItemEntity>()
        whenever(mockRepository.getAllItems()).thenReturn(flowOf(emptyList))

        // When
        val resultFlow = useCase()
        val results = resultFlow.toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(emptyList, results[0].getOrNull())
    }

    @Test
    fun `invoke should return failure result when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        whenever(mockRepository.getAllItems()).thenThrow(exception)

        // When
        val resultFlow = useCase()
        val results = resultFlow.toList()

        // Then
        assertEquals(1, results.size)
        assertTrue(results[0].isFailure)
        assertNotNull(results[0].exceptionOrNull())
        assertEquals("Database error", results[0].exceptionOrNull()?.cause?.message)
    }

    @Test
    fun `invoke should handle multiple emissions from repository`() = runTest {
        // Given
        val firstItems = listOf(
            ItemEntity(
                id = 1,
                name = "First Item",
                startNum = "1",
                endNum = "5",
                lottoNumbers = listOf(1 to 3, 2 to 2, 3 to 1)
            )
        )
        val secondItems = listOf(
            ItemEntity(
                id = 1,
                name = "First Item",
                startNum = "1",
                endNum = "5",
                lottoNumbers = listOf(1 to 3, 2 to 2, 3 to 1)
            ),
            ItemEntity(
                id = 2,
                name = "Second Item",
                startNum = "6",
                endNum = "10",
                lottoNumbers = listOf(4 to 2, 5 to 1)
            )
        )
        
        whenever(mockRepository.getAllItems()).thenReturn(flowOf(firstItems, secondItems))

        // When
        val resultFlow = useCase()
        val results = resultFlow.toList()

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0].isSuccess)
        assertTrue(results[1].isSuccess)
        assertEquals(firstItems, results[0].getOrNull())
        assertEquals(secondItems, results[1].getOrNull())
    }
}