package com.monopoly.game.model

data class Player(
    val id: Int,
    val name: String,
    val color: Int,
    var money: Int = 1500,
    var position: Int = 0,
    var isInJail: Boolean = false,
    var jailTurns: Int = 0,
    var isBankrupt: Boolean = false,
    val ownedProperties: MutableList<Property> = mutableListOf()
) {
    fun addMoney(amount: Int) {
        money += amount
    }
    
    fun removeMoney(amount: Int): Boolean {
        return if (money >= amount) {
            money -= amount
            true
        } else {
            false
        }
    }
    
    fun addProperty(property: Property) {
        ownedProperties.add(property)
        property.owner = this
    }
    
    fun removeProperty(property: Property) {
        ownedProperties.remove(property)
        property.owner = null
    }
    
    fun getTotalAssets(): Int {
        return money + ownedProperties.sumOf { it.price }
    }
    
    fun canAfford(amount: Int): Boolean {
        return money >= amount
    }
}