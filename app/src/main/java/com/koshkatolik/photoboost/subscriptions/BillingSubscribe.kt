package com.koshkatolik.photoboost.subscriptions

import android.content.Context
import android.util.Log
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails

class BillingSubscribe() : BillingProcessor.IBillingHandler{

    private val TAG = "sub_billing"
    var billing: BillingProcessor? = null
    var purchaseTransactionDetails: TransactionDetails? = null

    fun initialization(context: Context){
        billing = BillingProcessor.newBillingProcessor(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBC57v3qXmsv08YmJciB877BMKUvOzcr8Rnm2wgZMSESf0g8rg4d9tSbarBOPTbwh/entIvH2B0X0IubpyFxDHe4BoD/2aZsicLh0v7xt5pYvwuj+/lAkmi1ZaN1i4tfTLOkOWOD7aFRgAaegEi/endM/VQn8/1GyhTNP4spr2nVNEC6A7bOfGdE66HIwzlBzLOZNd0MlG8ILTZYekCOfKiBcaZC+CBxvrbOHtf/7zTIVaATVT9bWTQuNWfix3kGwhurKQGmNkGYDIyMfhjeykY3BOKXxCqJ07mTWyc3GrUOSB9duyEDMRtwe27M+5dGJP7YXqicXBeRXQY9htqLtwIDAQAB", this)
        billing?.initialize()
    }
    fun isHasSub() : Boolean{
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
//        purchaseTransactionDetails = billing?.getSubscriptionTransactionDetails("leonid")
//        billing?.loadOwnedPurchasesFromGoogle()
//        if(isHasSub()){
//            Log.i(TAG, "У пользователя есть подписка!")
//        } else {
//            Log.i(TAG, "У пользователя нет подписки!")
//        }
    }

}