package com.ute.student_profile.model

import java.io.Serializable

data class Student (
    val id: String,
    val name: String,
    val className: String,
    val email: String,
    val gpa: Double
    ): Serializable
    {
    val isHonorStudent: Boolean
        get() = gpa>=3.6
}