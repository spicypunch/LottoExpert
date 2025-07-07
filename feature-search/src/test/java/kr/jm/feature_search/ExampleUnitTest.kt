package kr.jm.feature_search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kr.jm.domain.usecase.GetLottoNumberUseCase
import kr.jm.domain.usecase.SaveLottoItemUseCase
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetLottoNumberUseCase = mock<GetLottoNumberUseCase>()
    private val mockSaveLottoItemUseCase = mock<SaveLottoItemUseCase>()
    private val viewModel = SearchViewModel(
        getLottoNumberUseCase = mockGetLottoNumberUseCase,
        saveLottoItemUseCase = mockSaveLottoItemUseCase
    )

    @Test
    fun `updateStartNum should update state correctly`() {
        // Given
        val startNum = "100"

        // When
        viewModel.onEvent(SearchScreenEvent.UpdateStartNum(startNum))

        // Then
        assertEquals(startNum, viewModel.state.value.startNum)
    }

    @Test
    fun `updateEndNum should update state correctly`() {
        // Given
        val endNum = "200"

        // When
        viewModel.onEvent(SearchScreenEvent.UpdateEndNum(endNum))

        // Then
        assertEquals(endNum, viewModel.state.value.endNum)
    }

    @Test
    fun `showMessage should update message in state`() {
        // Given
        val message = "Test message"

        // When
        viewModel.onEvent(SearchScreenEvent.ShowMessage(message))

        // Then
        assertEquals(message, viewModel.state.value.message)
    }

    @Test
    fun `clearMessage should clear message in state`() {
        // Given
        viewModel.onEvent(SearchScreenEvent.ShowMessage("Test message"))

        // When
        viewModel.onEvent(SearchScreenEvent.ClearMessage)

        // Then
        assertEquals("", viewModel.state.value.message)
    }

    @Test
    fun `closeDialog should set showDialog to false`() {
        // Given
        // 먼저 다이얼로그가 표시되는 상태를 만들어야 함
        
        // When
        viewModel.onEvent(SearchScreenEvent.CloseDialog)

        // Then
        assertFalse(viewModel.state.value.showDialog)
    }

    @Test
    fun `validateInput should show error message when startNum is blank`() {
        // Given
        val startNum = ""
        val endNum = "100"

        // When
        viewModel.onEvent(SearchScreenEvent.ValidateInput(startNum, endNum))

        // Then
        assertEquals("빈칸을 채워주세요", viewModel.state.value.message)
    }

    @Test
    fun `validateInput should show error message when endNum is blank`() {
        // Given
        val startNum = "100"
        val endNum = ""

        // When
        viewModel.onEvent(SearchScreenEvent.ValidateInput(startNum, endNum))

        // Then
        assertEquals("빈칸을 채워주세요", viewModel.state.value.message)
    }

    @Test
    fun `validateInput should show error message when startNum is not a number`() {
        // Given
        val startNum = "abc"
        val endNum = "100"

        // When
        viewModel.onEvent(SearchScreenEvent.ValidateInput(startNum, endNum))

        // Then
        assertEquals("유효한 숫자를 입력해주세요", viewModel.state.value.message)
    }

    @Test
    fun `validateInput should show error message when endNum is not a number`() {
        // Given
        val startNum = "100"
        val endNum = "abc"

        // When
        viewModel.onEvent(SearchScreenEvent.ValidateInput(startNum, endNum))

        // Then
        assertEquals("유효한 숫자를 입력해주세요", viewModel.state.value.message)
    }

    @Test
    fun `validateInput should show error message when startNum is greater than endNum`() {
        // Given
        val startNum = "200"
        val endNum = "100"

        // When
        viewModel.onEvent(SearchScreenEvent.ValidateInput(startNum, endNum))

        // Then
        assertEquals("시작 회차가 끝 회차보다 큽니다", viewModel.state.value.message)
    }

    @Test
    fun `getLottoNumbers should set loading state and show dialog on success`() = runTest {
        // Given
        val startNumber = 1
        val endNumber = 10
        val mockLottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        
        whenever(mockGetLottoNumberUseCase(startNumber, endNumber))
            .thenReturn(Result.success(mockLottoNumbers))

        // When
        viewModel.onEvent(SearchScreenEvent.GetLottoNum(startNumber, endNumber))

        // Then
        val state = viewModel.state.value
        assertEquals(mockLottoNumbers, state.lottoNumbers)
        assertTrue(state.showDialog)
        assertFalse(state.isLoading)
    }

    @Test
    fun `getLottoNumbers should show error message on failure`() = runTest {
        // Given
        val startNumber = 1
        val endNumber = 10
        val exception = RuntimeException("Network error")
        
        whenever(mockGetLottoNumberUseCase(startNumber, endNumber))
            .thenReturn(Result.failure(exception))

        // When
        viewModel.onEvent(SearchScreenEvent.GetLottoNum(startNumber, endNumber))

        // Then
        val state = viewModel.state.value
        assertEquals("값을 가져오는데 실패하였습니다", state.message)
        assertFalse(state.isLoading)
        assertFalse(state.showDialog)
    }

    @Test
    fun `insertItem should show success message on success`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        
        whenever(mockSaveLottoItemUseCase(name, startNum, endNum, lottoNumbers))
            .thenReturn(Result.success(Unit))

        // When
        viewModel.onEvent(SearchScreenEvent.InsertItem(name, startNum, endNum, lottoNumbers))

        // Then
        assertEquals("저장되었습니다", viewModel.state.value.message)
    }

    @Test
    fun `insertItem should show error message on failure`() = runTest {
        // Given
        val name = "Test Item"
        val startNum = "1"
        val endNum = "10"
        val lottoNumbers = listOf(1 to 5, 2 to 3, 3 to 2)
        val exception = RuntimeException("이미 저장한 회차입니다")
        
        whenever(mockSaveLottoItemUseCase(name, startNum, endNum, lottoNumbers))
            .thenReturn(Result.failure(exception))

        // When
        viewModel.onEvent(SearchScreenEvent.InsertItem(name, startNum, endNum, lottoNumbers))

        // Then
        assertEquals("이미 저장한 회차입니다", viewModel.state.value.message)
    }
}