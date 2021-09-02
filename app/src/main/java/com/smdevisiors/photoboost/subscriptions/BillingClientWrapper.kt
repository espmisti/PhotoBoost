package com.smdevisiors.photoboost.subscriptions
//
//import android.content.Context
//import com.android.billingclient.api.*
//
//class BillingClientWrapper(context: Context) : PurchasesUpdatedListener {
//
//    interface OnQueryProductsListener {
//        fun onSuccess(products: List<SkuDetails>)
//        fun onFailure(error: Error)
//    }
//
//    class Error(val responseCode: Int, val debugMessage: String)
//
//    private val billingClient = BillingClient
//        .newBuilder(context)
//        .enablePendingPurchases()
//        .setListener(this)
//        .build()
//
//    fun queryProducts(listener: OnQueryProductsListener) {
//        val skusList = listOf("premium_sub_month", "premium_sub_year", "some_inapp")
//
//        queryProductsForType(
//            skusList,
//            BillingClient.SkuType.SUBS
//        ) { billingResult, skuDetailsList ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                val products = skuDetailsList ?: mutableListOf()
//                queryProductsForType(
//                    skusList,
//                    BillingClient.SkuType.INAPP
//                ) { billingResult, skuDetailsList ->
//                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                        products.addAll(skuDetailsList ?: listOf())
//                        listener.onSuccess(products)
//                    } else {
//                        listener.onFailure(
//                            Error(billingResult.responseCode, billingResult.debugMessage)
//                        )
//                    }
//                }
//            } else {
//                listener.onFailure(
//                    Error(billingResult.responseCode, billingResult.debugMessage)
//                )
//            }
//        }
//    }
//
//
//
//    private fun queryProductsForType(
//        skusList: List<String>,
//        @BillingClient.SkuType type: String,
//        listener: SkuDetailsResponseListener
//    ) {
//        onConnected {
//            billingClient.querySkuDetailsAsync(
//                SkuDetailsParams.newBuilder().setSkusList(skusList).setType(type).build(),
//                listener
//            )
//        }
//    }
//
//    private fun onConnected(block: () -> Unit) {
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                block()
//            }
//
//            override fun onBillingServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//            }
//        })
//    }
//
//    override fun onPurchasesUpdated(billingResult: BillingResult, purchaseList: MutableList<Purchase>?) {
//        // here come callbacks about new purchases
//    }
//}