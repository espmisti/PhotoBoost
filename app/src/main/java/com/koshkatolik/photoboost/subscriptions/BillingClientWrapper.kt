package com.koshkatolik.photoboost.subscriptions

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.koshkatolik.photoboost.R

class BillingClientWrapper : AppCompatActivity(), BillingProcessor.IBillingHandler{
    private val TAG = "billing"
    var bp: BillingProcessor? = null
    var purchaseTransactionDetails: TransactionDetails? = null
    var resultSub: Boolean = false

    fun turboMisha(context: Context?){
        bp = BillingProcessor.newBillingProcessor(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwBC57v3qXmsv08YmJciB877BMKUvOzcr8Rnm2wgZMSESf0g8rg4d9tSbarBOPTbwh/entIvH2B0X0IubpyFxDHe4BoD/2aZsicLh0v7xt5pYvwuj+/lAkmi1ZaN1i4tfTLOkOWOD7aFRgAaegEi/endM/VQn8/1GyhTNP4spr2nVNEC6A7bOfGdE66HIwzlBzLOZNd0MlG8ILTZYekCOfKiBcaZC+CBxvrbOHtf/7zTIVaATVT9bWTQuNWfix3kGwhurKQGmNkGYDIyMfhjeykY3BOKXxCqJ07mTWyc3GrUOSB9duyEDMRtwe27M+5dGJP7YXqicXBeRXQY9htqLtwIDAQAB", this)
        bp?.initialize()
    }

    fun hasSub() : Boolean{
        if (purchaseTransactionDetails != null) {
            return purchaseTransactionDetails!!.purchaseInfo != null;
        }
        return false;
    }


    override fun onProductPurchased(productId: String, details: TransactionDetails?) { Log.e(TAG, "подписка куплена") }
    override fun onPurchaseHistoryRestored() { Log.e(TAG, "onPurchaseHistoryRestored: ") }
    override fun onBillingError(errorCode: Int, error: Throwable?) {

    }

    override fun onBillingInitialized() {
        purchaseTransactionDetails = bp?.getSubscriptionTransactionDetails("leonid")
        bp?.loadOwnedPurchasesFromGoogle()
        resultSub = hasSub()
    }
    fun getSub() : Boolean{
        return resultSub
    }

}