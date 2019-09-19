package com.xenix.award.model

class Filter {
    private var offset: Int = 0
    private var limit: Int = 8
    private var award_type: String = ""
    private var point_needed_min: Int = 0
    private var point_needed_max: Int = 0

    fun setOffset(offset: Int) {
        this.offset = offset
    }

    fun getOffset(): Int {
        return offset
    }

    fun getLimit(): Int {
        return limit
    }

    fun setLimit(limit: Int) {
        this.limit = limit
    }

    fun getAwardType(): String {
        return award_type
    }

    fun setAwardType(award_type: String) {
        this.award_type = award_type
    }

    fun getPointNeededMin(): Int {
        return point_needed_min
    }

    fun setPointNeededMin(point_needed_min: Int) {
        this.point_needed_min = point_needed_min
    }

    fun getPointNeededMax(): Int {
        return point_needed_max
    }

    fun setPointNeededMax(point_needed_max: Int) {
        this.point_needed_max = point_needed_max
    }
}
