package com.xenix.award.helper

import android.content.Context.INPUT_METHOD_SERVICE
import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import java.util.*


class Helper {

    companion object {
        val instance = Helper()
    }

    fun hideSoftKeypad(activity: Activity) {
        try {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {

        }

    }

    fun hideSoftKeypad(activity: FragmentActivity?) {
        try {
            val view = activity?.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {

        }

    }

    fun toPoinFormat(nominal: Int): String {
        var locale = Locale("in")
        val format = NumberFormat.getInstance(locale)
        val result = format.format(nominal)

        return "$result Poin"
    }
}