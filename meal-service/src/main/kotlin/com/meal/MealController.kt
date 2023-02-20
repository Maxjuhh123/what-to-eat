package com.meal

import com.meal.model.Meal
import com.meal.model.MealPostRequest
import com.meal.model.Meals
import com.meal.util.AuthUtil
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@RestController
@RequestMapping("/meal")
class MealController(
    private val mealService: MealService
) {

    /**
     * Get all the meals in the database.
     *
     * @returns - 200 with a list of meals
     *          - 404 if no meals have been found
     */
    @GetMapping
    fun getAllMeals(): ResponseEntity<Meals> =
        ok(mealService.getAllMeals())

    /**
     * Get a specific meal by id.
     *
     * @param mealId - the id of the meal
     * @returns - 200 with the meal if found
     *          - 404 if the meal does not exist
     */
    @GetMapping("/{mealId}")
    fun getMealById(@PathVariable mealId: Long): ResponseEntity<Meal> =
        ok(mealService.getMealById(mealId))

    /**
     * Add a meal to the database.
     *
     * @param mealPostRequest - contains info about the meal to create
     * @returns - 200 with the created meal
     */
    @PostMapping
    fun addMeal(
        authentication: Authentication,
        @RequestBody mealPostRequest: MealPostRequest
    ): ResponseEntity<Meal> =
        ok(mealService.addMeal(mealPostRequest, AuthUtil.extractUserId(authentication)))

}