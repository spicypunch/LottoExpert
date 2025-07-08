package kr.jm.feature_search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import kr.jm.feature_search.state.SearchScreenState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockState: SearchScreenState

    @Before
    fun setup() {
        mockState = SearchScreenState()
    }

    @Test
    fun searchScreen_displaysInitialState() {
        // Given
        composeTestRule.setContent {
            SearchScreen(
                state = mockState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText("로또 번호 분석").assertIsDisplayed()
        composeTestRule.onNodeWithText("시작 회차").assertIsDisplayed()
        composeTestRule.onNodeWithText("끝 회차").assertIsDisplayed()
        composeTestRule.onNodeWithText("검색").assertIsDisplayed()
    }

    @Test
    fun searchScreen_displaysInputFields() {
        // Given
        composeTestRule.setContent {
            SearchScreen(
                state = mockState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithTag("start_num_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("end_num_field").assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsLoadingState() {
        // Given
        val loadingState = mockState.copy(isLoading = true)
        
        composeTestRule.setContent {
            SearchScreen(
                state = loadingState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsErrorMessage() {
        // Given
        val errorMessage = "오류가 발생했습니다"
        val errorState = mockState.copy(message = errorMessage)
        
        composeTestRule.setContent {
            SearchScreen(
                state = errorState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun searchScreen_displaysLottoNumbers() {
        // Given
        val lottoNumbers = listOf(
            Pair(1, 10),
            Pair(7, 8),
            Pair(13, 5)
        )
        val stateWithNumbers = mockState.copy(
            lottoNumbers = lottoNumbers,
            showDialog = true
        )
        
        composeTestRule.setContent {
            SearchScreen(
                state = stateWithNumbers,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText("분석 결과").assertIsDisplayed()
        composeTestRule.onNodeWithText("번호: 1, 빈도: 10").assertIsDisplayed()
        composeTestRule.onNodeWithText("번호: 7, 빈도: 8").assertIsDisplayed()
        composeTestRule.onNodeWithText("번호: 13, 빈도: 5").assertIsDisplayed()
    }

    @Test
    fun searchScreen_canInputText() {
        // Given
        var capturedStartNum = ""
        var capturedEndNum = ""
        
        composeTestRule.setContent {
            SearchScreen(
                state = mockState,
                onEvent = { event ->
                    when (event) {
                        is SearchScreenEvent.StartNumChanged -> capturedStartNum = event.startNum
                        is SearchScreenEvent.EndNumChanged -> capturedEndNum = event.endNum
                        else -> {}
                    }
                }
            )
        }

        // When
        composeTestRule.onNodeWithTag("start_num_field").performTextInput("1")
        composeTestRule.onNodeWithTag("end_num_field").performTextInput("100")

        // Then
        assert(capturedStartNum == "1")
        assert(capturedEndNum == "100")
    }

    @Test
    fun searchScreen_canClickSearchButton() {
        // Given
        var searchClicked = false
        
        composeTestRule.setContent {
            SearchScreen(
                state = mockState,
                onEvent = { event ->
                    if (event is SearchScreenEvent.Search) {
                        searchClicked = true
                    }
                }
            )
        }

        // When
        composeTestRule.onNodeWithText("검색").performClick()

        // Then
        assert(searchClicked)
    }

    @Test
    fun searchScreen_canClickSaveButton() {
        // Given
        var saveClicked = false
        val stateWithNumbers = mockState.copy(
            lottoNumbers = listOf(Pair(1, 10)),
            showDialog = true
        )
        
        composeTestRule.setContent {
            SearchScreen(
                state = stateWithNumbers,
                onEvent = { event ->
                    if (event is SearchScreenEvent.Insert) {
                        saveClicked = true
                    }
                }
            )
        }

        // When
        composeTestRule.onNodeWithText("저장").performClick()

        // Then
        assert(saveClicked)
    }

    @Test
    fun searchScreen_canDismissDialog() {
        // Given
        var dialogDismissed = false
        val stateWithDialog = mockState.copy(
            lottoNumbers = listOf(Pair(1, 10)),
            showDialog = true
        )
        
        composeTestRule.setContent {
            SearchScreen(
                state = stateWithDialog,
                onEvent = { event ->
                    if (event is SearchScreenEvent.DialogDismiss) {
                        dialogDismissed = true
                    }
                }
            )
        }

        // When
        composeTestRule.onNodeWithText("닫기").performClick()

        // Then
        assert(dialogDismissed)
    }
}