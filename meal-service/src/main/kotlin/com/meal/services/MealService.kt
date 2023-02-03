package com.meal.services

import com.meal.exceptions.MealNotFoundException
import com.meal.model.Meal
import com.meal.model.MealPostRequest
import com.meal.model.Meals
import com.meal.repositories.MealRepository
import org.springframework.stereotype.Service

@Service
class MealService(
    private val mealRepository: MealRepository
) {

    fun getAllMeals(): Meals {
        val meals = mealRepository.findAll().filterNotNull()
        if(meals.isEmpty()) {
            throw MealNotFoundException()
        }

        return Meals(meals)
    }

    fun getMealById(mealId: Long): Meal =
        mealRepository.findById(mealId).orElseThrow { MealNotFoundException() }

    fun addMeal(mealPostRequest: MealPostRequest): Meal =
        mealRepository.saveAndFlush(mealPostRequest.toMeal(null))

}