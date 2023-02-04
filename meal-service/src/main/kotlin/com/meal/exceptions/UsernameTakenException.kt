package com.meal.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_MODIFIED)
class UsernameTakenException: RuntimeException()