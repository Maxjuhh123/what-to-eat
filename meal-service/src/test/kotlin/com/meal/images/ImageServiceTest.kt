package com.meal.images

import com.meal.MealService
import com.meal.images.model.ImageData
import com.meal.images.model.ImageNotFoundException
import com.meal.images.model.InvalidFileTypeException
import com.meal.model.MealNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

class ImageServiceTest: ShouldSpec({

    lateinit var imageRepository: ImageRepository
    lateinit var mealService: MealService
    lateinit var imageService: ImageService

    beforeEach {
        imageRepository = mockk()
        mealService = mockk()
        imageService = ImageService(imageRepository, mealService)
    }

    should("upload image successfully") {
        val file: MultipartFile = mockk()
        every { file.contentType } returns "image/png"
        every { file.bytes } returns ByteArray(5)
        every { file.originalFilename } returns "name"
        every { mealService.existsById(MEAL_ID) } returns true
        val expected = ImageData(IMAGE_ID, MEAL_ID, "name", "image/png", ByteArray(5))
        val req = ImageData(null, MEAL_ID, "name", "image/png", ByteArray(5))
        every { imageRepository.saveAndFlush(req) } returns expected

        val result = imageService.uploadImage(file, MEAL_ID)

        result shouldBe expected
        verify(exactly = 1) { imageRepository.saveAndFlush(req) }
        verify(exactly = 1) { mealService.existsById(MEAL_ID) }
    }

    should("not upload image if the meal does not exist") {
        every { mealService.existsById(MEAL_ID) } returns false
        val file: MultipartFile = mockk()

        shouldThrow<MealNotFoundException> {
            imageService.uploadImage(file, MEAL_ID)
        }
        verify(exactly = 1) { mealService.existsById(MEAL_ID) }
        verify(exactly = 0) { imageRepository.saveAndFlush(any()) }
    }

    should("not upload image if filetype not supported") {
        val file: MultipartFile = mockk()
        every { file.contentType } returns "csv"
        every { file.bytes } returns ByteArray(5)
        every { file.originalFilename } returns "name"
        every { mealService.existsById(MEAL_ID) } returns true

        shouldThrow<InvalidFileTypeException> {
            imageService.uploadImage(file, MEAL_ID)
        }

        verify(exactly = 0) { imageRepository.saveAndFlush(any()) }
        verify(exactly = 1) { mealService.existsById(MEAL_ID) }
    }

    should("get image by id if it exists") {
        val image = ImageData(IMAGE_ID, MEAL_ID, "name", "image/png", ByteArray(5))
        every { imageRepository.findById(IMAGE_ID) } returns Optional.of(image)

        val result = imageService.getImageById(IMAGE_ID)

        result shouldBe image
        verify(exactly = 1) { imageRepository.findById(IMAGE_ID) }
    }

    should("not get image by id if it doesn't exist") {
        every { imageRepository.findById(IMAGE_ID) } returns Optional.empty()

        shouldThrow<ImageNotFoundException> {
            imageService.getImageById(IMAGE_ID)
        }
        verify(exactly = 1) { imageRepository.findById(IMAGE_ID) }
    }

    should("get image by mealId if it exists") {
        val image = ImageData(IMAGE_ID, MEAL_ID, "name", "image/png", ByteArray(5))
        every { imageRepository.findByMealId(MEAL_ID) } returns Optional.of(image)

        val result = imageService.getImageByMealId(MEAL_ID)

        result shouldBe image
        verify(exactly = 1) { imageRepository.findByMealId(MEAL_ID) }
    }

    should("not get image by mealId if it does not exist") {
        every { imageRepository.findByMealId(MEAL_ID) } returns Optional.empty()

        shouldThrow<ImageNotFoundException> {
            imageService.getImageByMealId(MEAL_ID)
        }
        verify(exactly = 1) { imageRepository.findByMealId(MEAL_ID) }
    }

}) {
    override fun isolationMode() = IsolationMode.InstancePerTest

    companion object {
        private const val MEAL_ID = 123L
        private const val IMAGE_ID = 567L
    }
}