package com.meal.controllers

import com.meal.exceptions.MealNotFoundException
import com.meal.model.Meal
import com.meal.model.MealPostRequest
import com.meal.model.Meals
import com.meal.services.MealService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@WebMvcTest(controllers = [MealController::class])
@AutoConfigureMockMvc
class MealControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val mealService: MealService
): ShouldSpec ({

    val mapper = ObjectMapper()

    should("get all meals successfully") {
        val meal = Meal(MEAL_ID, "name", "description", BigDecimal(1.0))
        val meals = Meals(listOf(meal))
        every { mealService.getAllMeals() } returns meals

        val result = mockMvc.get("/meal") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 200
        result.contentAsString shouldBe mapper.writeValueAsString(meals)
        verify(exactly = 1) { mealService.getAllMeals() }
    }

    should("get all meals not found") {
        every { mealService.getAllMeals() } throws MealNotFoundException()

        val result = mockMvc.get("/meal") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 404
        result.contentAsString shouldBe ""
        verify(exactly = 1) { mealService.getAllMeals() }
    }

    should("get meal by id successfully") {
        val meal = Meal(MEAL_ID, "name", "description", BigDecimal(1.0))
        every { mealService.getMealById(MEAL_ID) } returns meal

        val result = mockMvc.get("/meal/$MEAL_ID") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 200
        result.contentAsString shouldBe mapper.writeValueAsString(meal)
        verify(exactly = 1) { mealService.getMealById(MEAL_ID) }
    }

    should("get meal by id not found") {
        every { mealService.getMealById(MEAL_ID) } throws MealNotFoundException()

        val result = mockMvc.get("/meal/$MEAL_ID") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 404
        result.contentAsString shouldBe ""
        verify(exactly = 1) { mealService.getMealById(MEAL_ID) }
    }

    should("add meal successfully") {
        val request = MealPostRequest("name", "description", BigDecimal(2.0))
        val meal = Meal(MEAL_ID, "name", "description", BigDecimal(2.0))
        every { mealService.addMeal(request) } returns meal

        val result = mockMvc.post("/meal") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 200
        result.contentAsString shouldBe mapper.writeValueAsString(meal)
        verify(exactly = 1) { mealService.addMeal(request)}
    }

}) {
    override fun isolationMode() = IsolationMode.InstancePerTest

    override fun extensions() = listOf(SpringExtension)

    companion object {
        private const val MEAL_ID = 1L
    }
}