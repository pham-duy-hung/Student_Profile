package com.ute.student_profile.model
import java.io.Serializable
data class Student (
    val id: String,
    val name: String,
    val className: String,
    val email: String,
    val GPA: Double
    ): Serializable{
    val isHonorStudent: Boolean
        get() = GPA>=3.6
}