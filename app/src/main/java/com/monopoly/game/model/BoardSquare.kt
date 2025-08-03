package com.monopoly.game.model

sealed class BoardSquare(
    val id: Int,
    val name: String,
    val position: Int
) {
    class PropertySquare(
        id: Int,
        name: String,
        position: Int,
        val property: Property
    ) : BoardSquare(id, name, position)
    
    class SpecialSquare(
        id: Int,
        name: String,
        position: Int,
        val type: SpecialType
    ) : BoardSquare(id, name, position)
    
    class ChanceSquare(
        id: Int,
        name: String,
        position: Int
    ) : BoardSquare(id, name, position)
    
    class CommunityChestSquare(
        id: Int,
        name: String,
        position: Int
    ) : BoardSquare(id, name, position)
}

enum class SpecialType {
    START, JAIL, FREE_PARKING, GO_TO_JAIL, TAX
}