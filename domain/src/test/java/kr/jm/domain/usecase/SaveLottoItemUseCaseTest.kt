package kr.jm.domain.usecase

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.repository.Repository
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.never

class SaveLottoItemUseCaseTest {

    private val mockRepository = mock<Repository>()
    private val useCase = SaveLottoItemUseCase(mockRepository)

    @Test
    fun `invoke should save item successfully when item does not exist`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        
        whenever(mockRepository.getItem(startNum, endNum)).thenReturn(flowOf())

        // When
        val result = useCase(name, startNum, endNum, lottoNumbers)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).insertItem(any())
    }

    @Test
    fun `invoke should return failure when item already exists`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        val existingItem = ItemEntity(
            id = 1,
            name = "Existing Item",
            startNum = startNum,
            endNum = endNum,
            lottoNumbers = lottoNumbers
        )
        
        whenever(mockRepository.getItem(startNum, endNum)).thenReturn(flowOf(existingItem))

        // When
        val result = useCase(name, startNum, endNum, lottoNumbers)

        // Then
        assertTrue(result.isFailure)
        assertEquals("이미 저장한 회차입니다", result.exceptionOrNull()?.message)
        verify(mockRepository, never()).insertItem(any())
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        val exception = RuntimeException("Database error")
        
        whenever(mockRepository.getItem(startNum, endNum)).thenThrow(exception)

        // When
        val result = useCase(name, startNum, endNum, lottoNumbers)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(mockRepository, never()).insertItem(any())
    }

    @Test
    fun `invoke should handle empty lotto numbers list`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = emptyList<Pair<Int, Int>>()
        
        whenever(mockRepository.getItem(startNum, endNum)).thenReturn(flowOf())

        // When
        val result = useCase(name, startNum, endNum, lottoNumbers)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).insertItem(any())
    }

    @Test
    fun `invoke should handle empty name`() = runTest {
        // Given
        val name = ""
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        
        whenever(mockRepository.getItem(startNum, endNum)).thenReturn(flowOf())

        // When
        val result = useCase(name, startNum, endNum, lottoNumbers)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).insertItem(any())
    }

    @Test
    fun `invoke should create correct ItemEntity`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        
        whenever(mockRepository.getItem(startNum, endNum)).thenReturn(flowOf())

        // When
        val result = useCase(name, startNum, endNum, lottoNumbers)

        // Then
        assertTrue(result.isSuccess)
        verify(mockRepository).insertItem(
            ItemEntity(
                name = name,
                startNum = startNum,
                endNum = endNum,
                lottoNumbers = lottoNumbers
            )
        )
    }
}