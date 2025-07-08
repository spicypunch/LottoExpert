package kr.jm.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.data.local.room.ItemDao
import kr.jm.data.remote.api.LottoApiService
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.model.LottoNumber
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    private val mockApiService = mock<LottoApiService>()
    private val mockItemDao = mock<ItemDao>()
    private val repository = RepositoryImpl(mockApiService, mockItemDao)

    @Test
    fun `getLottoNum should return data from api service`() = runTest {
        // Given
        val drawNumber = "1109"
        val expectedLottoNumber = LottoNumber(
            bonusNo = 2,
            drwNo = 1109,
            drwNoDate = "2024-03-02",
            drwtNo1 = 10,
            drwtNo2 = 12,
            drwtNo3 = 13,
            drwtNo4 = 19,
            drwtNo5 = 33,
            drwtNo6 = 40,
            firstPrzwnerCo = 17,
            returnValue = "success",
            totSellamnt = 117196327000L
        )
        
        whenever(mockApiService.getLottoNum(drawNumber)).thenReturn(expectedLottoNumber)

        // When
        val result = repository.getLottoNum(drawNumber)

        // Then
        assertEquals(expectedLottoNumber, result)
        verify(mockApiService).getLottoNum(drawNumber)
    }

    @Test
    fun `getItem should return flow from dao`() = runTest {
        // Given
        val startNum = "1"
        val endNum = "10"
        val expectedItem = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = startNum,
            endNum = endNum,
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )
        val expectedFlow = flowOf(expectedItem)
        
        whenever(mockItemDao.getItem(startNum, endNum)).thenReturn(expectedFlow)

        // When
        val result = repository.getItem(startNum, endNum)

        // Then
        assertEquals(expectedFlow, result)
        verify(mockItemDao).getItem(startNum, endNum)
    }

    @Test
    fun `getAllItems should return flow from dao`() = runTest {
        // Given
        val expectedItems = listOf(
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
        val expectedFlow = flowOf(expectedItems)
        
        whenever(mockItemDao.getAllItems()).thenReturn(expectedFlow)

        // When
        val result = repository.getAllItems()

        // Then
        assertEquals(expectedFlow, result)
        verify(mockItemDao).getAllItems()
    }

    @Test
    fun `insertItem should call dao insertItem with correct context`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )

        // When
        repository.insertItem(itemEntity)

        // Then
        verify(mockItemDao).insertItem(itemEntity)
    }

    @Test
    fun `deleteItem should call dao deleteItem with correct context`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )

        // When
        repository.deleteItem(itemEntity)

        // Then
        verify(mockItemDao).deleteItem(itemEntity)
    }

    @Test
    fun `getLottoNum should propagate api service exceptions`() = runTest {
        // Given
        val drawNumber = "1109"
        val exception = RuntimeException("Network error")
        
        whenever(mockApiService.getLottoNum(drawNumber)).thenThrow(exception)

        // When & Then
        try {
            repository.getLottoNum(drawNumber)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }

    @Test
    fun `insertItem should handle dao exceptions`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )
        val exception = RuntimeException("Database error")
        
        whenever(mockItemDao.insertItem(itemEntity)).thenThrow(exception)

        // When & Then
        try {
            repository.insertItem(itemEntity)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }

    @Test
    fun `deleteItem should handle dao exceptions`() = runTest {
        // Given
        val itemEntity = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        )
        val exception = RuntimeException("Database error")
        
        whenever(mockItemDao.deleteItem(itemEntity)).thenThrow(exception)

        // When & Then
        try {
            repository.deleteItem(itemEntity)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}