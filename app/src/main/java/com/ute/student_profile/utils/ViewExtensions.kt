package com.ute.student_profile.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.Toast

fun View.show(){visibility = View.VISIBLE}
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

//Extension cho Context (Toast)
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun EditText.trimmedText(): String = text.toString().trim()

//Extension nghiệp vụ quy đổi điểm sang Xếp loại
fun Double.toAcademicRanking(): String = when {
    this >= 3.6 -> "Xuất sắc"
    this >= 3.2 -> "Giỏi"
    this >= 2.5 -> "Khá"
    this >= 2.0 -> "Trung bình"
    this >= 1.0 -> "Yếu"
    else -> "Kém"
}


// Extension đổi màu Badge theo điểm
fun Double.toRankingColor(): Int = when {
    this >= 3.6 -> Color.parseColor("#34B469") // Xanh lá
    this >= 3.2 -> Color.parseColor("#00BCD4") // Xanh Cyan
    this >= 2.5 -> Color.parseColor("#FF9800") // Cam Amber
    else -> Color.parseColor("#F44336")        // Đỏ
}