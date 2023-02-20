package com.meal.images.model

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
class InvalidFileTypeException(override val message: String?): RuntimeException()