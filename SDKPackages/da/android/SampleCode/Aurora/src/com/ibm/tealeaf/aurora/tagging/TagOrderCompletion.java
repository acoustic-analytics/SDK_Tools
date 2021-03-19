/********************************************************************************************
* Copyright (C) 2015 Acoustic, L.P. All rights reserved.
*
* NOTICE: This file contains material that is confidential and proprietary to
* Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
* industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
* Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
* prohibited.
********************************************************************************************/
package com.ibm.tealeaf.aurora.tagging;

import java.util.UUID;
import java.util.ArrayList;

import android.util.Log;

import com.ibm.tealeaf.aurora.AuroraListItem;

import com.digitalanalytics.api.DigitalAnalytics;

/**
 * 
 * @author Sohil Shah (sohishah@us.ibm.com)
 * 
 */
public class TagOrderCompletion {

    public TagOrderCompletion() {
    }

    public void executeTag() {
        try {

            Boolean success = false;

            final TagSession session = TagSession.getInstance();

            // Setup Data to be tagged
            String orderId = TagConstants.CATEGORY + "/" + UUID.randomUUID().toString();
            String subtotal = session.getOrderSubtotal();
            String customerId = session.getCustomerId();
            String currency = "USD";

            // Fire a Shop Action9 for Product in the cart
            ArrayList<AuroraListItem> products = session.getCartItems();
            for (AuroraListItem product : products) {
                String productId = product.getId();
                String productName = product.getTitle();
                String categoryId = TagConstants.CATEGORY;
                String quantity = "1";
                String baseUnitPrice = product.getPrice();

                success = DigitalAnalytics.fireShopAction9(productId, productName, quantity, baseUnitPrice, categoryId,
                        orderId, subtotal, customerId, currency, null);
                if (!success) {
                    Log.e(TagConstants.LOG_TAG, "Failure: ShopAction9 Tag: " + productName);
                }
            }

            success = this.fireOrderTag(orderId, session);
            if (!success) {
                Log.e(TagConstants.LOG_TAG, "Failure: Order Tag");
            }
        } catch (Exception e) {
            Log.e(TagConstants.LOG_TAG, e.getMessage(), e);
        }
    }

    private Boolean fireOrderTag(final String orderId, final TagSession session) {
        String subtotal = session.getOrderSubtotal();
        String shippingCharge = "5.00";
        String customerId = session.getCustomerId();
        String city = session.getCity();
        String state = session.getState();
        String zip = session.getZip();
        String currency = "USD";

        // fire the order tag
        return DigitalAnalytics.fireOrder(orderId, subtotal, shippingCharge, customerId, city, state, zip, currency,
                null);
    }
}
