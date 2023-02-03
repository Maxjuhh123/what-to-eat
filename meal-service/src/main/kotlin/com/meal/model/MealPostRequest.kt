package com.meal.model

import java.math.BigDecimal

data class MealPostRequest(
    val name: String,
    val description: String,
    val price: BigDecimal
) {

    fun toMeal(mealId: Long?) =
        Meal(mealId = mealId, name = name, description = description, price = price)

}