package com.meal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "meals")
data class Meal(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mealId")
    val mealId: Long? = null,
    @Column(name = "name")
    val name: String = "",
    @Column(name = "description")
    val description: String = "",
    @Column(name = "price")
    val price: BigDecimal = BigDecimal(0),
    @Column(name = "userId")
    val userId: Long = 0L
)
