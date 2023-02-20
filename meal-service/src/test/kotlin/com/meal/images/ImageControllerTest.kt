package com.meal.images

import com.fasterxml.jackson.databind.ObjectMapper
import com.meal.MealApplication
import com.meal.images.model.ImageData
import com.meal.images.model.ImageNotFoundException
import com.meal.images.model.InvalidFileTypeException
import com.meal.model.MealNotFoundException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream

@SpringBootTest(classes = [MealApplication::class])
@AutoConfigureMockMvc
@WithUserDetails("test-user")
class ImageControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val imageService: ImageService
): ShouldSpec({

    val mapper = ObjectMapper()

    lateinit var file: MultipartFile

    beforeEach {
        val image = File("images/pasta.jpg")
        val inputStream = FileInputStream(image)
        file  = MockMultipartFile(image.name, image.name, "application/octet-stream", inputStream)
    }

    should("upload image successfully") {
        val expected = ImageData(IMAGE_ID, MEAL_ID, "images/pasta", "image/jpg", file.bytes)
        every { imageService.uploadImage(file, MEAL_ID) } returns expected

        val result = mockMvc.post("/images") {
            contentType = MediaType.MULTIPART_FORM_DATA
            accept = MediaType.APPLICATION_JSON
            param("file", mapper.writeValueAsString(file))
            param("mealId", MEAL_ID.toString())
        }
            .andReturn().response

        result.status shouldBe 200
        result.contentAsString shouldBe mapper.writeValueAsString(expected)
        verify(exactly = 1) { imageService.uploadImage(file, MEAL_ID) }
    }

    should("not upload image if meal does not exist") {
        every { imageService.uploadImage(file, MEAL_ID) } throws MealNotFoundException()

        val result = mockMvc.post("/images") {
            contentType = MediaType.MULTIPART_FORM_DATA
            accept = MediaType.APPLICATION_JSON
            param("file", mapper.writeValueAsString(file))
            param("mealId", MEAL_ID.toString())
        }
            .andReturn().response

        result.status shouldBe 404
        verify(exactly = 1) { imageService.uploadImage(file, MEAL_ID) }
    }

    should("not upload image if filetype not supported") {
        every { imageService.uploadImage(file, MEAL_ID) } throws InvalidFileTypeException("file type not supported")

        val result = mockMvc.post("/images") {
            contentType = MediaType.MULTIPART_FORM_DATA
            accept = MediaType.APPLICATION_JSON
            param("file", mapper.writeValueAsString(file))
            param("mealId", MEAL_ID.toString())
        }
            .andReturn().response

        result.status shouldBe 415
        verify(exactly = 1) { imageService.uploadImage(file, MEAL_ID) }
    }

    should("get image by id") {
        val expected = ImageData(IMAGE_ID, MEAL_ID, "images/pasta", "image/jpg", file.bytes)
        every { imageService.getImageById(IMAGE_ID) } returns expected

        val result = mockMvc.get("/images/$IMAGE_ID") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.contentAsString shouldBe mapper.writeValueAsString(expected)
        result.status shouldBe 200
        verify(exactly = 1) { imageService.getImageById(IMAGE_ID) }
    }

    should("not get image by id if it does not exist") {
        every { imageService.getImageById(IMAGE_ID) } throws ImageNotFoundException()

        val result = mockMvc.get("/images/$IMAGE_ID") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 404
        verify(exactly = 1) { imageService.getImageById(IMAGE_ID) }
    }

    should("get image by mealId") {
        val expected = ImageData(IMAGE_ID, MEAL_ID, "images/pasta", "image/jpg", file.bytes)
        every { imageService.getImageByMealId(MEAL_ID) } returns expected

        val result = mockMvc.get("/images") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            param("mealId", MEAL_ID.toString())
        }
            .andReturn().response

        result.contentAsString shouldBe mapper.writeValueAsString(expected)
        result.status shouldBe 200
        verify(exactly = 1) { imageService.getImageByMealId(MEAL_ID) }
    }

    should("not get image by mealId if it does not exist") {
        every { imageService.getImageByMealId(MEAL_ID) } throws MealNotFoundException()

        val result = mockMvc.get("/images") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            param("mealId", MEAL_ID.toString())
        }
            .andReturn().response

        result.status shouldBe 404
        verify(exactly = 1) { imageService.getImageByMealId(MEAL_ID) }
    }

}) {
    override fun isolationMode() = IsolationMode.InstancePerTest

    override fun extensions() = listOf(SpringExtension)

    companion object {
        private const val MEAL_ID = 342423L
        private const val IMAGE_ID = 34321342L
    }
}