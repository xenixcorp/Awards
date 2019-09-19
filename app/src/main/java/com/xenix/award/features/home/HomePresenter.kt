package com.xenix.award.features.home

import android.content.Context
import android.util.Log
import com.xenix.award.helper.database.DBHelper
import com.xenix.award.model.Filter

class HomePresenter(val view: HomeView, val context: Context, val DBHelper: DBHelper) {

    fun loadData(filter: Filter, isScroll: Boolean) {
        HomeActivity.mIsLoading = true
        view.showLoading()
        view.onSuccessLoadData(DBHelper.readRewardsWithFilter(filter), isScroll)
        view.hideLoading()
        HomeActivity.mIsLoading = false
    }

    fun getMin(): Int {
        return DBHelper.getMinPoin()
    }

    fun getMax(): Int {
        return DBHelper.getMaxPoin()
    }
}