package com.meal.services

import com.meal.exceptions.MealNotFoundException
import com.meal.model.Meal
import com.meal.model.MealPostRequest
import com.meal.model.Meals
import com.meal.repositories.MealRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal
import java.util.Optional

class MealServiceTest: ShouldSpec({

    lateinit var mealRepository: MealRepository
    lateinit var mealService: MealService

    beforeEach {
        mealRepository = mockk()
        mealService = MealService(mealRepository)
    }

    should("get all meals successfully") {
        val meal1 = Meal(1L, "name1", "description1", BigDecimal(1.0))
        val meal2 = Meal(2L, "name2", "description2", BigDecimal(2.0))
        val meals = listOf(meal1, meal2)
        every { mealRepository.findAll() } returns meals

        val result = mealService.getAllMeals()

        result shouldBe Meals(meals)
        verify(exactly = 1) { mealRepository.findAll() }
    }

    should("throw exception when only null meals") {
        val meals = listOf<Meal?>(null)
        every { mealRepository.findAll() }.returns(meals)

        shouldThrow<MealNotFoundException> {
            mealService.getAllMeals()
        }
        verify(exactly = 1) { mealRepository.findAll() }
    }

    should("throw exception when get all meals empty") {
        val meals = emptyList<Meal>()
        every { mealRepository.findAll() }.returns(meals)

        shouldThrow<MealNotFoundException> {
            mealService.getAllMeals()
        }
        verify(exactly = 1) { mealRepository.findAll() }
    }

    should("get meal by id successfully") {
        val meal = Meal(MEAL_ID, "name1", "description1", BigDecimal(1.0))
        every { mealRepository.findById(MEAL_ID) } returns Optional.of(meal)

        val result = mealService.getMealById(MEAL_ID)

        result shouldBe meal
        verify(exactly = 1) { mealRepository.findById(MEAL_ID) }
    }

    should("throw exception when meal by id not found") {
        every { mealRepository.findById(MEAL_ID) } returns Optional.empty()

        shouldThrow<MealNotFoundException> { mealService.getMealById(MEAL_ID) }
        verify(exactly = 1) { mealRepository.findById(MEAL_ID) }
    }

    should("add meal successfully") {
        val request = MealPostRequest("name", "description", BigDecimal(1.0))
        val meal = request.toMeal(null)
        val mealWithId = Meal(MEAL_ID, "name", "description", BigDecimal(1.0))
        every { mealRepository.saveAndFlush(meal) } returns mealWithId

        val result = mealService.addMeal(request)

        result shouldBe mealWithId
        verify(exactly = 1) { mealRepository.saveAndFlush(meal) }
    }

}) {
    override fun isolationMode() = IsolationMode.InstancePerTest

    companion object {
        private const val MEAL_ID = 1L
    }
}