package com.meal.images

import com.meal.images.model.ImageData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ImageRepository: JpaRepository<ImageData, Long> {

    fun findByMealId(mealId: Long): Optional<ImageData>

}