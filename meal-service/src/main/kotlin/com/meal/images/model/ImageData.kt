package com.meal.images.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Lob

@Entity
data class ImageData(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "mealId")
    val mealId: Long = -1,
    @Column(name = "name")
    val name: String = "",
    @Column(name = "type")
    val type: String = "",
    @Lob
    @Column(name = "imageData", length = 5000)
    val bytes: ByteArray = ByteArray(0)
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageData

        if (id != other.id) return false
        if (name != other.name) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        return result
    }

}