package com.xenix.award.features.login

import android.view.View

interface LoginView {
    fun onSuccessLogin()
    fun onFailedLogin(it: View, message: String)
}