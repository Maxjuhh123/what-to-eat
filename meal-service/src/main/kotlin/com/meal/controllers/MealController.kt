package com.meal.controllers

import com.meal.model.MealPostRequest
import com.meal.services.MealService
import org.springframework.http.ResponseEntity.ok
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

    @GetMapping
    fun getAllMeals() =
        ok(mealService.getAllMeals())

    @GetMapping("/{mealId}")
    fun getMealById(@PathVariable mealId: Long) =
        ok(mealService.getMealById(mealId))

    @PostMapping
    fun addMeal(@RequestBody mealPostRequest: MealPostRequest) =
        ok(mealService.addMeal(mealPostRequest))

}