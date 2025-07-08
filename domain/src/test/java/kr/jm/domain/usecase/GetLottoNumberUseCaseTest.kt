package kr.jm.domain.usecase

import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.model.LottoNumberEntity
import kr.jm.domain.repository.Repository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetLottoNumberUseCaseTest {

    private lateinit var repository: Repository
    private lateinit var useCase: GetLottoNumberUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GetLottoNumberUseCase(repository)
    }

    @Test
    fun `invoke should return success with sorted lotto numbers when repository returns data`() = runTest {
        // Given
        val mockLottoNumbers = listOf(
            LottoNumberEntity(
                round = 1,
                numbers = listOf(1, 7, 13, 23, 32, 40),
                bonusNumber = 6
            ),
            LottoNumberEntity(
                round = 2,
                numbers = listOf(7, 14, 21, 28, 35, 42),
                bonusNumber = 1
            ),
            LottoNumberEntity(
                round = 3,
                numbers = listOf(1, 7, 15, 25, 35, 45),
                bonusNumber = 10
            )
        )
        whenever(repository.getLottoNumbers(1, 3)).thenReturn(mockLottoNumbers)

        // When
        val result = useCase.invoke(1, 3)

        // Then
        assertTrue(result.isSuccess)
        val sortedNumbers = result.getOrNull()
        assertEquals(6, sortedNumbers?.size)
        
        // Verify numbers are sorted by frequency (descending)
        val firstNumber = sortedNumbers?.first()
        val lastNumber = sortedNumbers?.last()
        assertTrue(firstNumber?.second ?: 0 >= lastNumber?.second ?: 0)
        
        // Verify expected numbers appear with correct frequency
        val numberFrequencyMap = sortedNumbers?.toMap()
        assertEquals(3, numberFrequencyMap?.get(1)) // 1 appears 2 times
        assertEquals(3, numberFrequencyMap?.get(7)) // 7 appears 3 times
        assertEquals(2, numberFrequencyMap?.get(35)) // 35 appears 2 times
    }

    @Test
    fun `invoke should return success with empty list when repository returns empty data`() = runTest {
        // Given
        whenever(repository.getLottoNumbers(1, 100)).thenReturn(emptyList())

        // When
        val result = useCase.invoke(1, 100)

        // Then
        assertTrue(result.isSuccess)
        val numbers = result.getOrNull()
        assertTrue(numbers?.isEmpty() == true)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        whenever(repository.getLottoNumbers(1, 100)).thenThrow(RuntimeException("Network error"))

        // When
        val result = useCase.invoke(1, 100)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should handle single lotto number correctly`() = runTest {
        // Given
        val singleLottoNumber = listOf(
            LottoNumberEntity(
                round = 1,
                numbers = listOf(1, 2, 3, 4, 5, 6),
                bonusNumber = 7
            )
        )
        whenever(repository.getLottoNumbers(1, 1)).thenReturn(singleLottoNumber)

        // When
        val result = useCase.invoke(1, 1)

        // Then
        assertTrue(result.isSuccess)
        val numbers = result.getOrNull()
        assertEquals(6, numbers?.size)
        
        // All numbers should have frequency of 1
        numbers?.forEach { (number, frequency) ->
            assertEquals(1, frequency)
            assertTrue(number in 1..6)
        }
    }

    @Test
    fun `invoke should correctly calculate frequency for duplicate numbers across multiple draws`() = runTest {
        // Given
        val mockLottoNumbers = listOf(
            LottoNumberEntity(round = 1, numbers = listOf(1, 2, 3, 4, 5, 6), bonusNumber = 7),
            LottoNumberEntity(round = 2, numbers = listOf(1, 2, 3, 10, 11, 12), bonusNumber = 8),
            LottoNumberEntity(round = 3, numbers = listOf(1, 2, 20, 21, 22, 23), bonusNumber = 9)
        )
        whenever(repository.getLottoNumbers(1, 3)).thenReturn(mockLottoNumbers)

        // When
        val result = useCase.invoke(1, 3)

        // Then
        assertTrue(result.isSuccess)
        val numberFrequencyMap = result.getOrNull()?.toMap()
        
        // Numbers 1 and 2 should appear 3 times each
        assertEquals(3, numberFrequencyMap?.get(1))
        assertEquals(3, numberFrequencyMap?.get(2))
        assertEquals(2, numberFrequencyMap?.get(3))
        
        // Numbers that appear only once
        assertEquals(1, numberFrequencyMap?.get(4))
        assertEquals(1, numberFrequencyMap?.get(10))
        assertEquals(1, numberFrequencyMap?.get(20))
    }

    @Test
    fun `invoke should sort numbers by frequency in descending order`() = runTest {
        // Given
        val mockLottoNumbers = listOf(
            LottoNumberEntity(round = 1, numbers = listOf(1, 2, 3, 4, 5, 6), bonusNumber = 7),
            LottoNumberEntity(round = 2, numbers = listOf(1, 2, 3, 10, 11, 12), bonusNumber = 8),
            LottoNumberEntity(round = 3, numbers = listOf(1, 20, 21, 22, 23, 24), bonusNumber = 9)
        )
        whenever(repository.getLottoNumbers(1, 3)).thenReturn(mockLottoNumbers)

        // When
        val result = useCase.invoke(1, 3)

        // Then
        assertTrue(result.isSuccess)
        val sortedNumbers = result.getOrNull()
        
        // Verify sorting: frequency should be in descending order
        var previousFrequency = Int.MAX_VALUE
        sortedNumbers?.forEach { (_, frequency) ->
            assertTrue(frequency <= previousFrequency)
            previousFrequency = frequency
        }
        
        // First number should have highest frequency (1 appears 3 times)
        assertEquals(1, sortedNumbers?.first()?.first)
        assertEquals(3, sortedNumbers?.first()?.second)
    }

    @Test
    fun `invoke should handle invalid range gracefully`() = runTest {
        // Given
        whenever(repository.getLottoNumbers(100, 1)).thenReturn(emptyList())

        // When
        val result = useCase.invoke(100, 1)

        // Then
        assertTrue(result.isSuccess)
        val numbers = result.getOrNull()
        assertTrue(numbers?.isEmpty() == true)
    }
}