# Zapp Small Merchant Gateway Library Documentation (mobile commerce journey)

## Introduction

This documentation provides a description about what is the **recommended way** the Merchant Apps to integrate with the Zapp Small Merchant Gateway (through the Merchant backend) to implement the Zapp mobile commerce journey.

## Abbreviations

Abbreviation | Description
------------ | -----------
CFI | Customer's Financial Institution
CTA | Call to Action
PBBA | Pay by Bank app
SDK | Software Development Kit

## Introduction to the mobile commerce journey
The steps of the Zapp mobile commerce journey in the Merchant App are as follows:

1. Check if the device has PBBA CFI App
2. Display the PBBA Button
3. Submit payment request to Merchant backend
4. Start the PBBA CFI App
5. The CFI App updates the status of the payment (the user confirms or declines the payment request)
6. Poll for payment status once the control is returned to the Merchant App
7. Display payment confirmation screen

## Check if the device has PBBA CFI App

As a first step, the Merchant App should check if the user’s device has at least one PBBA enabled CFI App installed. If the device does not have any App installed with PBBA support, the Merchant App can display a CTA text to encourage the user to install any PBBA enabled CFI App or can skip the PBBA payment feature (e.g. not display the PBBA logo in the list of payment methods). Since the user can install or uninstall Apps between using the Merchant App, the actual value should not be cached but the PBBA CFI App status should be checked every time the Merchant App wants to decide whether to display the PBBA button.

```android
import com.zapp.library.smg.core.util.AppUtils;

//(...)

// check if user's device support PBBA payments (provide a Context as the single parameter)
if (AppUtils.isZappCFIAppAvailable(this)) {
  // display PBBA button
} else {
  // display CTA or skip PBBA from methods of payment
}
```

## Display the PBBA Button

As a next step, if there is at least one PBBA enabled CFI App installed on the user's device, the Merchant App should display the PBBA button. 

```android
 <com.zapp.library.smg.core.ui.PBBAButton
                android:id="@+id/pbba_button"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />
```

```android
//in the Activity (or Fragment) of the payment method selection screen...

import com.zapp.library.smg.core.ui.PBBAButton;

//(...)

//in the .onCreate(Bundle) method of the Activity...
final PBBAButton pbbaButton = (PBBAButton) findViewById(R.id.pbba_button);
pbbaButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(final View v) {

        //start the submit payment request here
        //remember to stop the PBBA icon animation and reenable the button again once the submit payment request has been completed
        //by calling pbbaButton.stopAnimation() and pbbaButton.setEnabled(true)
    }
});
```

## Submit payment request to Merchant backend

If the user taps on the PBBA button, its .onClick(View) method is called. The button gets disabled in order to avoid double taps then starts animating the PBBA icon and forwards the click event to the Merchant App click listener which is recommended to submit its payment request to the Merchant backend. The implementation of this step is completely custom to and up to the Merchant.

The Zapp Small Merchant Gateway requires the following parameters in order to create a payment:

Parameter name | Parameter type | Description
---------------|----------------|------------
totalAmount | java.lang.Long | The total amount for which the merchant would like to get the payment made by the customer.
merchantId | java.lang.String | A Zapp provided data after the merchant is successfully registered.
merchantCallbackUrl | java.lang.String | A URL used by the PBBA enabled CFI App to invoke the Merchant App after the payment has been confirmed or declined.
rtpId | java.lang.String | A unique ID for the merchant that represents a purchase. Conditional: the merchant could choose not to generate this ID and let Zapp gateway generate it (this should be decided as part of merchant registration).
adHoc | com.zapp.library.smg.core.model.AdHoc | Key - value pair information that the merchant can use in order for Zapp to relay back the same during payment notification. Optional.

The Merchant backend may require additional (Merchant related custom) parameters which are required to be set in order to start the payment in the Merchant backend. This is why the Merchant App should always call its own backend which starts the payment then forwards the Zapp Small Merchant Gateway related parameters to the Zapp Small Merchant Gateway and starts the Zapp payment too.

The Zapp Small Merchant Gateway Library provides a model object to support the Merchant App to manage the Zapp Small Merchant Gateway related parameters in one object. This class is com.zapp.library.smg.mcom.LiteMcomPaymentRequest. This class has exactly the same fields mentioned in the table above.

If the Zapp payment was successfully submitted to the Zapp Small Merchant Gateway, the Zapp Small Merchant Gateway responds with a JSON data structure can be deserialised to com.zapp.library.smg.mcom.model.LiteMcomTransaction object. This class has the following fields:

Field name | Field type | Description
-----------|------------|------------
aptId | java.lang.String | This is a unique id for the payment request generated by Zapp, mainly to be used by machine as it is a 37 characters long string. Note: this is the data that needs to be passed to the CFI App in the mobile device as part of the Zapp URL.
aptrId | java.lang.String | This is a unique id for the payment request generated by Zapp, mainly to be used by human as t is an 18 characters long string, this ID is used to retrieve payment status. A polling mechanism every 3 seconds is recommended.
retrievalExpiryInterval | java.lang.Integer | This data is in seconds. It is for the merchant for information purposes only. It is used by Zapp to mark a payment request as 'late retrieved' after this interval.
confirmationExpiryInterval | java.lang.Integer | This data is in seconds. It is for the merchant to poll for payment notification until this interval runs out and initiate an payment request enquiry after this.
settlementRetrievalId | java.lang.String | This is a unique id generated by Zapp for all payment requests. It should be used to enquire a payment status for the request which has been reached its confirmation expiry interval timeout.

These values are required to perform the Zapp mobile commerce journey so the Merchant backend should relay these fields back to the Merchant App.

After this, the Merchant App starts its custom transaction timer which provides callbacks with events for 'poll for payment status' and 'payment confirmation expired'. This timer sends 'poll for payment status' every 5 seconds and a single 'payment confirmation expired' event when it reaches the value received in the 'confirmationExpiryInterval' field of the LiteMcomTransaction object. To implement this timing, you can use the com.zapp.library.smg.core.TransactionTimer (abstract) class. An example of how to implement the transaction timer is as follows:

```android
//in a Merchant App custom class...

import com.zapp.library.smg.core.TransactionTimer;

public class MerchantTransactionTimer extends TransactionTimer {
    
    private final LiteMcomTransaction mTransaction;

    public MerchantTransactionTimer(@NonNull final LiteMcomTransaction transaction) {
        super(transaction);
        mTransaction = transaction;
    }

    @Override
    public void onPollForPaymentStatus() {
        //if the Merchant App is not in the background (e.g. the user put it to the background or the 
        //CFI App took over) then perform payment status polling on the Merchant backend using the aptrId 
        //from the mTransaction field (see its description later), otherwise ignore polling

        //get the aptrId from the mTransaction
        final String aptrId = mTransaction.getAptrId();

        //perform Merchant backend request for payment status (this callback happens on the UI thread so in order to 
        //execute network request, please switch to a background thread)
    }

    @Override
    public void onPaymentConfirmationExpired() {
        //if the Merchant App is not in the background (e.g. the user put it to the background or the 
        //CFI App took over), then display the confirmation screen in the Merchant App with a 
        //'payment declined' message, otherwise save this event and act on it when the Merchant App 
        //comes to the foreground again
    }

}
```

## Start the PBBA CFI App

After the payment journey has been started and the LiteMcomTransaction details have been relayed to the Merchant App, the Merchant App invokes the CFI App automatically in order to provide a convenient way to the user to confirm or decline the payment.

```android
//in the Merchant App payment activity...

//liteTransaction is the com.zapp.library.smg.mcom.model.LiteMcomTransaction object received from the Merchant backend
//after the payment has been submitted to the Zapp Small Merchant Gateway...

//Open the CFI App (provide a Context as first parameter)
AppUtils.openZappCFIApp(this, liteTransaction.getAptId(), liteTransaction.getAptrId());
```

## The CFI App updates the status of the payment (the user confirms or declines the payment request)

After the CFI App has been invoked, the Merchant App goes to the background and pauses its operation. The CFI App parses the aptId and aptrId fields from the Zapp invocation URL and retrieves the payment details with them. Once the payment details have been retrieved, the CFI App displays its payment confirmation screen where the user can confirm or decline the payment. After the user taps the confirm or decline button on this screen, the CFI App updates the payment status in Zapp then displays the payment finish screen in which it summarises the payment details and the outcome of the user interaction (confirmed or declined payment). There is a 'payment finish' button displayed on this screen. If the user taps on this button, the CFI App invokes the URL provided in the 'merchantCallbackUrl' field of the com.zapp.library.smg.mcom.LiteMcomPaymentRequest class (or its equivalent). This invocation returns the control to the Merchant App.

## Poll for payment status once the control is returned to the Merchant App

After the control has been returned to the Merchant App and the payment neither has been timed out nor finished, the Merchant App acts on the 'poll for payment status' and 'payment confirmation expired' events.

Polling for the payment status is required in order to be able to decide how to continue the payment journey. The suggested way to implement this polling is as follows:

* Once the payment has been successfully submitted from the Merchant backend to the Zapp Small Merchant Gateway, the Merchant backend starts polling for payment status to the Zapp Small Merchant Gateway and caches its result
* When the Merchant App requests payment status from the Merchant backend, the Merchant backend returns the last payment status received from the Zapp Small Merchant Gateway or 'in progress' if the Merchant backend has not completed any payment status request to the Zapp Small Merchant Gateway
* Since the Merchant can have additional state (e.g. Merchant custom, payment status related extra fields), it is recommended to return these data with the payment status response returning from the Merchant backend to the Merchant App
* When the Merchant App receives the (possibly extended) response from the Merchant backend and the payment has been completed (its status is not 'in progress' any more) then the Merchant App cancels its polling and moves on to the payment confirmation screen on which it displays the details of the payment status
* The Merchant App moves on to its payment confirmation screen too if the payment has been timed out (the Merchant App payment timer sends 'payment expired' signal)

The implementation of this step is completely custom to and up to the Merchant.

The Zapp Small Merchant Gateway requires the following parameters in order to return the payment status for a particular payment:

Field name | Field type | Description
-----------|------------|------------
aptrid | java.lang.String | The unique id for the payment request generated by Zapp.

If the Zapp payment status request was successfully completed in the Zapp Small Merchant Gateway, the Zapp Small Merchant Gateway responds with a JSON data structure can be deserialised to com.zapp.library.smg.core.model.network.LitePaymentStatusResponse object. This class has the following fields:

Field name | Field type | Description
-----------|------------|------------
txnStatus | java.lang.Integer | The payment status.
txnStatusDesc | java.lang.Integer | Additional description about the payment status. Optional.
settlementAmount | java.lang.Long | This value is the same as the totalAmount field in the payment request. Optional.
settlementPaymentRef | java.lang.String | The payment reference data provided by the CFI. Optional.
settlementType | java.lang.Integer | Additional information for the Merchant about the channel the CFI used to make the payment (e.g. ONUS or FPS). Optional.
adHoc | com.zapp.library.smg.core.model.AdHoc | This value is the same as the adHoc field value in the payment request. Optionald
 
## Display payment confirmation screen

After the payment has been completed, the Merchant App displays its payment confirmation screen on which it can choose to display any details came in the payment status response.
