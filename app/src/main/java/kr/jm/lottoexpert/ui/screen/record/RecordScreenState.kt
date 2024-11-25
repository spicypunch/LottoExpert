package kr.jm.lottoexpert.ui.screen.record

import kr.jm.lottoexpert.data.ItemEntity

data class RecordScreenState(
    val items: List<ItemEntity> = emptyList(),
)

sealed interface RecordScreenEvent {
    data class DeleteItem(val item: ItemEntity) : RecordScreenEvent
    data object LoadItems : RecordScreenEvent
}