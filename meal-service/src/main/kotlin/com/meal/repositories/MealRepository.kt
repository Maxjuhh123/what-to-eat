package com.meal.repositories

import com.meal.model.Meal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MealRepository: JpaRepository<Meal, Long>