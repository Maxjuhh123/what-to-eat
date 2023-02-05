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

    /**
     * Get all the meals in the database.
     *
     * @return Meals object with the list of meals found.
     * @throws MealNotFoundException if no meals found
     */
    fun getAllMeals(): Meals =
        Meals(
            mealRepository.findAll()
                .filterNotNull()
                .ifEmpty { throw MealNotFoundException() }
        )

    /**
     * Get a specific meal by id.
     *
     * @param mealId - the id of the meal
     * @return the meal found
     * @throws MealNotFoundException when the meal does not exist
     */
    fun getMealById(mealId: Long): Meal =
        mealRepository.findById(mealId).orElseThrow { MealNotFoundException() }

    /**
     * Add a meal to the database.
     *
     * @param mealPostRequest - contains data of the meal to add.
     * @return the created meal
     */
    fun addMeal(mealPostRequest: MealPostRequest): Meal =
        mealRepository.saveAndFlush(mealPostRequest.toMeal(null))

}