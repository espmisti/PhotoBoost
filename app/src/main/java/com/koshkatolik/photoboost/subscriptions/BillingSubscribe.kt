package com.koshkatolik.photoboost.subscriptions

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.koshkatolik.photoboost.fragments.main.MainFragment
import kotlinx.android.synthetic.main.fragment_main.*

class BillingSubscribe() : BillingProcessor.IBillingHandler{

    init {
        var isSub = false
    }

    private val TAG = "sub_billing"
    var billing: BillingProcessor? = null
    var purchaseTransactionDetails: TransactionDetails? = null

    var donbas = false

    fun initialization(context: Context){
        billing = BillingProcessor.newBillingProcessor(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBC57v3qXmsv08YmJciB877BMKUvOzcr8Rnm2wgZMSESf0g8rg4d9tSbarBOPTbwh/entIvH2B0X0IubpyFxDHe4BoD/2aZsicLh0v7xt5pYvwuj+/lAkmi1ZaN1i4tfTLOkOWOD7aFRgAaegEi/endM/VQn8/1GyhTNP4spr2nVNEC6A7bOfGdE66HIwzlBzLOZNd0MlG8ILTZYekCOfKiBcaZC+CBxvrbOHtf/7zTIVaATVT9bWTQuNWfix3kGwhurKQGmNkGYDIyMfhjeykY3BOKXxCqJ07mTWyc3GrUOSB9duyEDMRtwe27M+5dGJP7YXqicXBeRXQY9htqLtwIDAQAB", this)
        billing?.initialize()
    }
    private fun isHasSub() : Boolean{
        if (purchaseTransactionDetails != null) {
            return purchaseTransactionDetails!!.purchaseInfo != null;
        }
        return false;
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        Log.i(TAG, "onProductPurchased: ")
    }

    override fun onPurchaseHistoryRestored() {
        Log.i(TAG, "onPurchaseHistoryRestored: ")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.i(TAG, "onBillingError: sa")
    }

    override fun onBillingInitialized() {
        billing?.loadOwnedPurchasesFromGoogle()
        purchaseTransactionDetails = billing?.getSubscriptionTransactionDetails("second")
        MainFragment().m_text_subscription.text = "Залупа"
//        if(isHasSub()) {
//            isSub = true
//            donbas = true
//            Log.i(TAG, "- - - Подписка есть")
//        } else {
//            isSub = false
//            Log.i(TAG, "- - - Подписки нету")
//        }
    }
}