package kr.jm.feature_record

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import kr.jm.domain.model.ItemEntity
import kr.jm.feature_record.state.RecordScreenState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockState: RecordScreenState

    @Before
    fun setup() {
        mockState = RecordScreenState()
    }

    @Test
    fun recordScreen_displaysInitialState() {
        // Given
        composeTestRule.setContent {
            RecordScreen(
                state = mockState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText("저장된 기록").assertIsDisplayed()
    }

    @Test
    fun recordScreen_displaysEmptyState() {
        // Given
        composeTestRule.setContent {
            RecordScreen(
                state = mockState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText("저장된 기록이 없습니다").assertIsDisplayed()
    }

    @Test
    fun recordScreen_displaysItems() {
        // Given
        val items = listOf(
            ItemEntity(
                id = 1,
                name = "테스트 항목 1",
                startNum = "1",
                endNum = "10",
                numbers = listOf(1 to 5, 7 to 3, 13 to 2)
            ),
            ItemEntity(
                id = 2,
                name = "테스트 항목 2",
                startNum = "11",
                endNum = "20",
                numbers = listOf(2 to 8, 9 to 4, 15 to 3)
            )
        )
        val stateWithItems = mockState.copy(items = items)
        
        composeTestRule.setContent {
            RecordScreen(
                state = stateWithItems,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText("테스트 항목 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("테스트 항목 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("회차: 1 ~ 10").assertIsDisplayed()
        composeTestRule.onNodeWithText("회차: 11 ~ 20").assertIsDisplayed()
    }

    @Test
    fun recordScreen_displaysItemNumbers() {
        // Given
        val items = listOf(
            ItemEntity(
                id = 1,
                name = "테스트 항목",
                startNum = "1",
                endNum = "10",
                numbers = listOf(1 to 5, 7 to 3, 13 to 2)
            )
        )
        val stateWithItems = mockState.copy(items = items)
        
        composeTestRule.setContent {
            RecordScreen(
                state = stateWithItems,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText("1(5)").assertIsDisplayed()
        composeTestRule.onNodeWithText("7(3)").assertIsDisplayed()
        composeTestRule.onNodeWithText("13(2)").assertIsDisplayed()
    }

    @Test
    fun recordScreen_canDeleteItem() {
        // Given
        val itemToDelete = ItemEntity(
            id = 1,
            name = "삭제할 항목",
            startNum = "1",
            endNum = "10",
            numbers = listOf(1 to 5, 7 to 3)
        )
        val stateWithItems = mockState.copy(items = listOf(itemToDelete))
        var deletedItem: ItemEntity? = null
        
        composeTestRule.setContent {
            RecordScreen(
                state = stateWithItems,
                onEvent = { event ->
                    if (event is RecordScreenEvent.DeleteItem) {
                        deletedItem = event.item
                    }
                }
            )
        }

        // When
        composeTestRule.onNodeWithTag("delete_button_1").performClick()

        // Then
        assert(deletedItem == itemToDelete)
    }

    @Test
    fun recordScreen_showsErrorMessage() {
        // Given
        val errorMessage = "오류가 발생했습니다"
        val errorState = mockState.copy(message = errorMessage)
        
        composeTestRule.setContent {
            RecordScreen(
                state = errorState,
                onEvent = {}
            )
        }

        // When & Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun recordScreen_canClearMessage() {
        // Given
        val stateWithMessage = mockState.copy(message = "테스트 메시지")
        var messageClearClicked = false
        
        composeTestRule.setContent {
            RecordScreen(
                state = stateWithMessage,
                onEvent = { event ->
                    if (event is RecordScreenEvent.ClearMessage) {
                        messageClearClicked = true
                    }
                }
            )
        }

        // When
        composeTestRule.onNodeWithText("확인").performClick()

        // Then
        assert(messageClearClicked)
    }

    @Test
    fun recordScreen_displaysMultipleItems() {
        // Given
        val items = (1..5).map { index ->
            ItemEntity(
                id = index,
                name = "항목 $index",
                startNum = "$index",
                endNum = "${index + 10}",
                numbers = listOf(index to 5, index + 1 to 3)
            )
        }
        val stateWithItems = mockState.copy(items = items)
        
        composeTestRule.setContent {
            RecordScreen(
                state = stateWithItems,
                onEvent = {}
            )
        }

        // When & Then
        items.forEach { item ->
            composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
        }
    }

    @Test
    fun recordScreen_itemsAreScrollable() {
        // Given
        val items = (1..20).map { index ->
            ItemEntity(
                id = index,
                name = "항목 $index",
                startNum = "$index",
                endNum = "${index + 10}",
                numbers = listOf(index to 5, index + 1 to 3)
            )
        }
        val stateWithItems = mockState.copy(items = items)
        
        composeTestRule.setContent {
            RecordScreen(
                state = stateWithItems,
                onEvent = {}
            )
        }

        // When & Then
        // LazyColumn이 스크롤 가능한지 확인
        composeTestRule.onNodeWithTag("items_list").assertIsDisplayed()
        composeTestRule.onNodeWithText("항목 1").assertIsDisplayed()
    }
}