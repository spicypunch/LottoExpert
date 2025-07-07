package kr.jm.domain.usecase

import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.LottoNumber
import kr.jm.domain.repository.Repository
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetLottoNumberUseCaseTest {

    private val mockRepository = mock<Repository>()
    private val useCase = GetLottoNumberUseCase(mockRepository)

    @Test
    fun `invoke should return frequency count when repository returns valid data`() = runTest {
        // Given
        val startNumber = 1
        val endNumber = 2
        val mockLottoData1 = LottoNumber(
            bonusNo = 1,
            drwNo = 1,
            drwNoDate = "2024-01-01",
            drwtNo1 = 1,
            drwtNo2 = 2,
            drwtNo3 = 3,
            drwtNo4 = 4,
            drwtNo5 = 5,
            drwtNo6 = 6,
            firstPrzwnerCo = 10,
            returnValue = "success",
            totSellamnt = 1000000L
        )
        val mockLottoData2 = LottoNumber(
            bonusNo = 2,
            drwNo = 2,
            drwNoDate = "2024-01-02",
            drwtNo1 = 1,
            drwtNo2 = 2,
            drwtNo3 = 7,
            drwtNo4 = 8,
            drwtNo5 = 9,
            drwtNo6 = 10,
            firstPrzwnerCo = 15,
            returnValue = "success",
            totSellamnt = 2000000L
        )

        whenever(mockRepository.getLottoNum("1")).thenReturn(mockLottoData1)
        whenever(mockRepository.getLottoNum("2")).thenReturn(mockLottoData2)

        // When
        val result = useCase(startNumber, endNumber)

        // Then
        assertTrue(result.isSuccess)
        val frequencyList = result.getOrNull()!!
        
        // 1과 2가 2번씩 나타나야 함
        assertEquals(2, frequencyList.find { it.first == 1 }?.second)
        assertEquals(2, frequencyList.find { it.first == 2 }?.second)
        
        // 빈도수 기준 내림차순 정렬 확인
        val frequencies = frequencyList.map { it.second }
        assertEquals(frequencies.sortedDescending(), frequencies)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val startNumber = 1
        val endNumber = 1
        val exception = RuntimeException("Network error")
        
        whenever(mockRepository.getLottoNum("1")).thenThrow(exception)

        // When
        val result = useCase(startNumber, endNumber)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke should handle single number correctly`() = runTest {
        // Given
        val startNumber = 1
        val endNumber = 1
        val mockLottoData = LottoNumber(
            bonusNo = 1,
            drwNo = 1,
            drwNoDate = "2024-01-01",
            drwtNo1 = 10,
            drwtNo2 = 20,
            drwtNo3 = 30,
            drwtNo4 = 40,
            drwtNo5 = 50,
            drwtNo6 = 60,
            firstPrzwnerCo = 5,
            returnValue = "success",
            totSellamnt = 500000L
        )

        whenever(mockRepository.getLottoNum("1")).thenReturn(mockLottoData)

        // When
        val result = useCase(startNumber, endNumber)

        // Then
        assertTrue(result.isSuccess)
        val frequencyList = result.getOrNull()!!
        assertEquals(6, frequencyList.size) // 6개 번호 모두 포함
        
        // 모든 번호가 1번씩 나타나야 함
        frequencyList.forEach { (number, count) ->
            assertEquals(1, count)
        }
    }

    @Test
    fun `invoke should handle empty range correctly`() = runTest {
        // Given
        val startNumber = 5
        val endNumber = 3 // 잘못된 범위

        // When
        val result = useCase(startNumber, endNumber)

        // Then
        assertTrue(result.isSuccess)
        val frequencyList = result.getOrNull()!!
        assertTrue(frequencyList.isEmpty())
    }
}