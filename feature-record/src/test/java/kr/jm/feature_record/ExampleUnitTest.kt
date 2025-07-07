package kr.jm.feature_record

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.usecase.DeleteItemUseCase
import kr.jm.domain.usecase.GetAllItemsUseCase
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RecordViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetAllItemsUseCase = mock<GetAllItemsUseCase>()
    private val mockDeleteItemUseCase = mock<DeleteItemUseCase>()

    @Test
    fun `init should load items automatically`() = runTest {
        // Given
        val mockItems = listOf(
            ItemEntity(
                id = 1,
                name = "Test Item",
                startNum = "1",
                endNum = "10",
                lottoNumbers = listOf(1 to 5, 2 to 3)
            )
        )
        
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(mockItems)))

        // When
        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)

        // Then
        assertEquals(mockItems, viewModel.state.value.items)
    }

    @Test
    fun `showMessage should update message in state`() = runTest {
        // Given
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(emptyList())))
        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)
        val message = "Test message"

        // When
        viewModel.onEvent(RecordScreenEvent.ShowMessage(message))

        // Then
        assertEquals(message, viewModel.state.value.message)
    }

    @Test
    fun `clearMessage should clear message in state`() = runTest {
        // Given
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(emptyList())))
        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)
        
        viewModel.onEvent(RecordScreenEvent.ShowMessage("Test message"))

        // When
        viewModel.onEvent(RecordScreenEvent.ClearMessage)

        // Then
        assertEquals("", viewModel.state.value.message)
    }

    @Test
    fun `loadItems should update items in state on success`() = runTest {
        // Given
        val mockItems = listOf(
            ItemEntity(
                id = 1,
                name = "Test Item 1",
                startNum = "1",
                endNum = "10",
                lottoNumbers = listOf(1 to 5, 2 to 3)
            ),
            ItemEntity(
                id = 2,
                name = "Test Item 2",
                startNum = "11",
                endNum = "20",
                lottoNumbers = listOf(3 to 4, 4 to 2)
            )
        )
        
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(mockItems)))

        // When
        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)
        viewModel.onEvent(RecordScreenEvent.LoadItems)

        // Then
        assertEquals(mockItems, viewModel.state.value.items)
    }

    @Test
    fun `loadItems should handle failure gracefully`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.failure(exception)))

        // When
        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)

        // Then
        // 실패 시에도 앱이 크래시하지 않고, 로그만 남기고 상태는 초기값 유지
        assertEquals(emptyList<ItemEntity>(), viewModel.state.value.items)
    }

    @Test
    fun `deleteItem should remove item from state on success`() = runTest {
        // Given
        val itemToDelete = ItemEntity(
            id = 1,
            name = "Test Item 1",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3)
        )
        val remainingItem = ItemEntity(
            id = 2,
            name = "Test Item 2",
            startNum = "11",
            endNum = "20",
            lottoNumbers = listOf(3 to 4, 4 to 2)
        )
        val initialItems = listOf(itemToDelete, remainingItem)
        
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(initialItems)))
        whenever(mockDeleteItemUseCase(itemToDelete)).thenReturn(Result.success(Unit))

        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)

        // When
        viewModel.onEvent(RecordScreenEvent.DeleteItem(itemToDelete))

        // Then
        assertEquals(listOf(remainingItem), viewModel.state.value.items)
        verify(mockDeleteItemUseCase).invoke(itemToDelete)
    }

    @Test
    fun `deleteItem should handle failure gracefully`() = runTest {
        // Given
        val itemToDelete = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3)
        )
        val initialItems = listOf(itemToDelete)
        val exception = RuntimeException("Delete failed")
        
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(initialItems)))
        whenever(mockDeleteItemUseCase(itemToDelete)).thenReturn(Result.failure(exception))

        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)

        // When
        viewModel.onEvent(RecordScreenEvent.DeleteItem(itemToDelete))

        // Then
        // 실패 시 아이템이 그대로 남아있어야 함
        assertEquals(initialItems, viewModel.state.value.items)
    }

    @Test
    fun `deleteItem should handle empty list correctly`() = runTest {
        // Given
        val itemToDelete = ItemEntity(
            id = 1,
            name = "Test Item",
            startNum = "1",
            endNum = "10",
            lottoNumbers = listOf(1 to 5, 2 to 3)
        )
        
        whenever(mockGetAllItemsUseCase()).thenReturn(flowOf(Result.success(emptyList())))
        whenever(mockDeleteItemUseCase(itemToDelete)).thenReturn(Result.success(Unit))

        val viewModel = RecordViewModel(mockGetAllItemsUseCase, mockDeleteItemUseCase)

        // When
        viewModel.onEvent(RecordScreenEvent.DeleteItem(itemToDelete))

        // Then
        assertEquals(emptyList<ItemEntity>(), viewModel.state.value.items)
    }
}