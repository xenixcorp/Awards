package com.xenix.award.base

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.xenix.award.helper.Helper
import com.xenix.award.helper.RxBus



abstract class BaseActivity: AppCompatActivity() {

    protected var helper = Helper.instance

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val v = currentFocus

        if (ev != null) {
            if (v != null &&
                    (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                    v is EditText &&
                    !v.javaClass.simpleName.startsWith("android.webkit.")) {
                val scrcoords = IntArray(2)
                v.getLocationOnScreen(scrcoords)
                val x = ev.getRawX() + v.left - scrcoords[0]
                val y = ev.getRawY() + v.top - scrcoords[1]

                //if (x < v.left || x > v.right || y < v.top || y > v.bottom)
                //    helper.hideSoftKeypad(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    protected infix fun goTo(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }

    protected infix fun goToLoginLogout(cls: Class<*>) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    companion object {
        private var rxBus: RxBus? = null
        val getRxBus: RxBus
            get() {
                if (rxBus == null) {
                    rxBus = RxBus()
                }

                return rxBus as RxBus
            }
    }
}