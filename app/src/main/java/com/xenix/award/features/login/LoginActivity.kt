package com.xenix.award.features.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.xenix.award.features.home.HomeActivity
import com.xenix.award.R
import com.xenix.award.base.BaseActivity
import com.xenix.award.helper.database.DBHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginView {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter(this, this, DBHelper(this))
        presenter.checkTable()
        btnSignIn.setOnClickListener {
            presenter.onLoginTapped(etEmail.text.toString(), it)
        }
    }

    override fun onSuccessLogin() {
        goToLoginLogout(HomeActivity::class.java)
        finish()
    }

    override fun onFailedLogin(it: View, message: String) {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
    }
}
