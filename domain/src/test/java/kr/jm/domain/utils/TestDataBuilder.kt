package kr.jm.domain.utils

import kr.jm.domain.model.ItemEntity
import kr.jm.domain.model.LottoNumberEntity

object TestDataBuilder {
    
    fun createLottoNumberEntity(
        round: Int = 1,
        numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6),
        bonusNumber: Int = 7
    ): LottoNumberEntity {
        return LottoNumberEntity(
            round = round,
            numbers = numbers,
            bonusNumber = bonusNumber
        )
    }
    
    fun createItemEntity(
        id: Int = 1,
        name: String = "Test Item",
        startNum: String = "1",
        endNum: String = "10",
        numbers: List<Pair<Int, Int>> = listOf(1 to 5, 7 to 3, 13 to 2)
    ): ItemEntity {
        return ItemEntity(
            id = id,
            name = name,
            startNum = startNum,
            endNum = endNum,
            numbers = numbers
        )
    }
    
    fun createMultipleLottoNumberEntities(count: Int = 3): List<LottoNumberEntity> {
        return (1..count).map { index ->
            createLottoNumberEntity(
                round = index,
                numbers = listOf(
                    index, 
                    index + 6, 
                    index + 12, 
                    index + 18, 
                    index + 24, 
                    index + 30
                ).take(6),
                bonusNumber = index + 36
            )
        }
    }
    
    fun createMultipleItemEntities(count: Int = 3): List<ItemEntity> {
        return (1..count).map { index ->
            createItemEntity(
                id = index,
                name = "Test Item $index",
                startNum = "$index",
                endNum = "${index + 10}",
                numbers = listOf(
                    index to 5,
                    index + 1 to 3,
                    index + 2 to 2
                )
            )
        }
    }
    
    fun createLottoNumbersWithFrequency(): List<Pair<Int, Int>> {
        return listOf(
            1 to 10,
            7 to 8,
            13 to 6,
            23 to 4,
            32 to 3,
            45 to 2
        )
    }
    
    fun createLottoNumberEntityWithSpecificNumbers(
        round: Int = 1,
        vararg numbers: Int
    ): LottoNumberEntity {
        require(numbers.size == 6) { "Lotto numbers must be exactly 6 numbers" }
        return createLottoNumberEntity(
            round = round,
            numbers = numbers.toList(),
            bonusNumber = 45
        )
    }
}