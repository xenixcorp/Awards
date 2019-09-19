package com.xenix.award.features.login

import android.content.Context
import android.view.View
import com.xenix.award.R
import com.xenix.award.helper.database.DBHelper
import com.xenix.award.model.Awards

class LoginPresenter(val view: LoginView, val context: Context, val DBHelper: DBHelper) {

    fun onLoginTapped(email: String, it: View) {
        if (email.equals("")) {
            view.onFailedLogin(it, context.getString(R.string.mandatory_email))
        } else if (email.equals(context.getString(R.string.auth_email), ignoreCase = true)) {
            view.onSuccessLogin()
        } else {
            view.onFailedLogin(it, context.getString(R.string.incorrect_email))
        }
    }

    fun checkTable() {
        if(DBHelper.readAllAwards().size <= 0) {
            DBHelper.insertAward(Awards("Vouchers", "", "Gift Card IDR 1.000.000", 500000))
            DBHelper.insertAward(Awards("Vouchers", "", "Gift Card IDR 500.000", 250000))
            DBHelper.insertAward(Awards("Products", "", "Old Fashion Cake", 100000))
            DBHelper.insertAward(Awards("Vouchers", "https://image.freepik.com/free-photo/beautiful-mountain-landscape-small-village-among-snow-pine-trees-mountain-background_35312-175.jpg", "New Fashion Cake", 50000))
            DBHelper.insertAward(Awards("Giftcard", "", "Voucher Game", 20000))
            DBHelper.insertAward(Awards("Products", "https://www.starpik.com/wp-content/uploads/2018/12/Winter-rural-landscape-Vector.-Small-village-and-lot-of-snow-background-illustration-21956.jpg", "Pulsa and Paket", 10000))
            DBHelper.insertAward(Awards("Giftcard", "https://i.pinimg.com/originals/a4/f9/1a/a4f91a56c7d5ddb4b8f5a99d6c30d52d.jpg", "Asuransi", 5000))
            DBHelper.insertAward(Awards("Products", "", "Promo Token PLN", 1000))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Giftcard", "https://www.featurepics.com/StockImage/20160924/fairy-tale-house-stock-illustration-4230866.jpg", "Award", 100))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
            DBHelper.insertAward(Awards("Vouchers", "", "Promo Ojek", 500))
        }
    }
}