package kr.jm.feature_record

import kr.jm.domain.model.ItemEntity

data class RecordScreenState(
    val items: List<ItemEntity> = emptyList(),
    val message: String = ""
)

sealed interface RecordScreenEvent {
    data class ShowMessage(val message: String) : RecordScreenEvent
    data object ClearMessage : RecordScreenEvent
    data class DeleteItem(val item: ItemEntity) : RecordScreenEvent
    data object LoadItems : RecordScreenEvent
}