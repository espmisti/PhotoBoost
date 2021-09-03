package com.koshkatolik.photoboost.subscriptions

import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails

class BillingClientWrapper : BillingProcessor.IBillingHandler{


    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        TODO("Not yet implemented")
    }

    override fun onPurchaseHistoryRestored() {
        TODO("Not yet implemented")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun onBillingInitialized() {
        TODO("Not yet implemented")
    }

}