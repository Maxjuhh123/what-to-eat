package com.meal.images

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/images")
class ImageController(
    private val imageService: ImageService
) {

    /**
     * Get an image by id.
     *
     * @param imageId - the id of the image
     * @return - 200 with image found
     *         - 404 if image not found
     */
    @GetMapping("/{imageId}")
    fun getImageById(@PathVariable(name = "imageId") imageId: Long): ResponseEntity<ByteArray> =
        ok(imageService.getImageById(imageId).bytes)

    /**
     * Get an image by mealId.
     *
     * @param mealId - the id of the meal
     * @return - 200 with image found
     *         - 404 if image not found
     */
    @GetMapping
    fun getImageByMealId(@RequestParam(name = "mealId") mealId: Long): ResponseEntity<ByteArray> =
        ok(imageService.getImageByMealId(mealId).bytes)

    /**
     * Add image for specific meal to database.
     *
     * @param imageFile - the file containing the image
     * @param mealId - the id of the meal
     * @return - 200 with the image if uploaded successfully
     *         - 404 if the meal does not exist
     *         - 415 if the media type of the image is not supported
     */
    @PostMapping
    fun uploadImage(
        @RequestParam("file") imageFile: MultipartFile,
        @RequestParam mealId: Long
    ) = ok(imageService.uploadImage(imageFile, mealId))

}