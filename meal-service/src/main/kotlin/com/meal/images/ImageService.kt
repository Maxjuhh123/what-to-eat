package com.meal.images

import com.meal.MealService
import com.meal.images.model.ImageData
import com.meal.images.model.ImageNotFoundException
import com.meal.images.model.InvalidFileTypeException
import com.meal.model.MealNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val mealService: MealService
) {

    /**
     * Upload an image for a specific meal.
     *
     * @param imageFile - the file containing the image
     * @param mealId - the id of the meal to upload image for
     * @return the uploaded image
     * @throws InvalidFileTypeException - if the type of the image file is not accepted
     * @throws MealNotFoundException - if the meal does not exist
     */
    fun uploadImage(imageFile: MultipartFile, mealId: Long): ImageData {
        if(!mealService.existsById(mealId)) {
            throw MealNotFoundException()
        }
        validateImage(imageFile)
        val imageData = ImageData(
            name = imageFile.originalFilename!!,
            mealId = mealId,
            bytes = imageFile.bytes,
            type = imageFile.contentType!!
        )
        return imageRepository.saveAndFlush(imageData)
    }

    /**
     * Get an image by mealId.
     *
     * @param mealId - the id of the meal
     * @return the meal found
     * @throws ImageNotFoundException - if the image does not exist
     */
    fun getImageByMealId(mealId: Long): ImageData =
        imageRepository.findByMealId(mealId).orElseThrow { ImageNotFoundException() }

    /**
     * Get an image by imageId.
     *
     * @param imageId - the id of the image
     * @return the meal found
     * @throws ImageNotFoundException - if the image does not exist
     */
    fun getImageById(imageId: Long): ImageData =
        imageRepository.findById(imageId).orElseThrow { ImageNotFoundException() }

    /**
     * Validate the type of the file is a valid image type.
     *
     * @param file - the file to check the type of
     * @throws InvalidFileTypeException - if the type is not valid
     */
    private fun validateImage(file: MultipartFile) {
        val allowedTypes = VALID_TYPES.map { "image/$it" }
        if(!allowedTypes.contains(file.contentType)) {
            throw InvalidFileTypeException("Content-type: " + file.contentType + " not allowed for image.")
        }
    }

    companion object {
        private val VALID_TYPES = listOf("jpg", "png", "jpeg")
    }

}