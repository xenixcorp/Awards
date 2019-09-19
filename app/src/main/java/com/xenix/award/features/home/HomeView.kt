package com.xenix.award.features.home

import com.xenix.award.model.Awards

interface HomeView {
    fun onSuccessLoadData(awards: ArrayList<Awards>, isScroll: Boolean)
    fun showLoading()
    fun hideLoading()
}