package com.xenix.award.model

data class Awards(
    val award_type: String,
    val image_url: String,
    val name: String,
    val point_needed: Int
)