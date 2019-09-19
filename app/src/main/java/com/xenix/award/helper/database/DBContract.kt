package com.xenix.award.helper.database

import android.provider.BaseColumns

object DBContract {

    class AwardEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "awards"
            val COLUMN_AWARD_TYPE = "award_type"
            val COLUMN_IMAGE_URL = "image_url"
            val COLUMN_NAME = "name"
            val COLUMN_POINT_NEEDED = "point_needed"
        }
    }
}