package com.meal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MealApplication

fun main(args: Array<String>) {
	runApplication<MealApplication>(*args)
}
