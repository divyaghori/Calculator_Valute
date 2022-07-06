package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.HashMap;
import java.util.List;


public class IAPHelper {

    private String TAG = IAPHelper.class.getSimpleName();

    private Context context;
    private BillingClient mBillingClient;
    private IAPHelperListener IAPHelperListener;
    private List<String> skuList;
    public IAPHelper(Context context, IAPHelperListener IAPHelperListener, List<String> skuList) {
        this.context = context;
        this.IAPHelperListener = IAPHelperListener;
        this.skuList = skuList;
        this.mBillingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(getPurchaseUpdatedListener())
                .build();
        if (!mBillingClient.isReady()) {
            Log.d(TAG, "BillingClient: Start connection...");

            startConnection();
        }
    }

    private void startConnection() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                int billingResponseCode = billingResult.getResponseCode();
                Log.d(TAG, "onBillingSetupFinished: " + billingResult.getResponseCode());
                if (billingResponseCode == BillingClient.BillingResponseCode.OK) {
                    getPurchasedItems();
                    getSKUDetails(skuList);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected: ");
            }
        });
    }

    public void getPurchasedItems() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (IAPHelperListener != null)
            IAPHelperListener.onPurchasehistoryResponse(purchasesResult.getPurchasesList());
    }
    public void getSKUDetails(List<String> skuList) {
        final HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
        SkuDetailsParams skuParams = SkuDetailsParams.newBuilder().setType(BillingClient.SkuType.INAPP).setSkusList(skuList).build();
        mBillingClient.querySkuDetailsAsync(skuParams, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    for (SkuDetails skuDetails : skuDetailsList) {
                        skuDetailsHashMap.put(skuDetails.getSku(), skuDetails);
                    }
                    if (IAPHelperListener != null)
                        IAPHelperListener.onSkuListResponse(skuDetailsHashMap);
                }
            }
        });
    }
    public void launchBillingFLow(final SkuDetails skuDetails) {
        if(mBillingClient.isReady()){
            BillingFlowParams mBillingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build();
            mBillingClient.launchBillingFlow((Activity) context, mBillingFlowParams);
        }
    }
    private PurchasesUpdatedListener getPurchaseUpdatedListener() {
        return (billingResult, purchases) -> {
            int responseCode = billingResult.getResponseCode();
            if (responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    String type = purchase.getSku().split("_")[0];
                    if(type.equals("nc"))
                        acknowledgePurchase(purchase);
                    else
                        consumePurchase(purchase);
                }
            } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.d(TAG, "user cancelled");
            } else if (responseCode == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                Log.d(TAG , "service disconnected");
                startConnection();
            }
        };
    }

    public void acknowledgePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED
                && isSignatureValid(purchase)) {
            AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                @Override
                public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                    Log.d("purchase", "Purchase Acknowledged");
                }
            });

            if (IAPHelperListener != null)
                IAPHelperListener.onPurchaseCompleted(purchase);
        }
    }

    public void consumePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED
                && isSignatureValid(purchase)) {
            ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            mBillingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(BillingResult billingResult, String s) {
                    Log.d("purchase", "Purchase Consumed");
                }
            });

            if (IAPHelperListener != null)
                IAPHelperListener.onPurchaseCompleted(purchase);
        }
    }

    private boolean isSignatureValid(Purchase purchase) {
        return Security.verifyPurchase(Security.BASE_64_ENCODED_PUBLIC_KEY, purchase.getOriginalJson(), purchase.getSignature());
    }

    public void endConnection() {
        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }
    public interface IAPHelperListener {
        void onSkuListResponse(HashMap<String, SkuDetails> skuDetailsHashMap);
        void onPurchasehistoryResponse(List<Purchase> purchasedItem);
        void onPurchaseCompleted(Purchase purchase);
    }


}