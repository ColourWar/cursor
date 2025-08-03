package com.monopoly.game.model

data class Property(
    val id: Int,
    val name: String,
    val price: Int,
    val rent: Int,
    val color: PropertyColor,
    val position: Int,
    var owner: Player? = null,
    var hasHouse: Boolean = false,
    var hasHotel: Boolean = false
) {
    fun getCurrentRent(): Int {
        return when {
            hasHotel -> rent * 5
            hasHouse -> rent * 2
            else -> rent
        }
    }
    
    fun isOwned(): Boolean = owner != null
    
    fun canBuildHouse(): Boolean {
        return isOwned() && !hasHouse && !hasHotel
    }
    
    fun canBuildHotel(): Boolean {
        return isOwned() && hasHouse && !hasHotel
    }
    
    fun buildHouse(): Boolean {
        return if (canBuildHouse()) {
            hasHouse = true
            true
        } else false
    }
    
    fun buildHotel(): Boolean {
        return if (canBuildHotel()) {
            hasHouse = false
            hasHotel = true
            true
        } else false
    }
}

enum class PropertyColor {
    BROWN, LIGHT_BLUE, PINK, ORANGE, RED, YELLOW, GREEN, DARK_BLUE, UTILITY, RAILROAD
}