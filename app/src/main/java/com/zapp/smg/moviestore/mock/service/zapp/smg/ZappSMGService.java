package com.zapp.smg.moviestore.mock.service.zapp.smg;

import com.google.gson.Gson;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.zapp.library.smg.core.model.network.LitePaymentStatusResponse;
import com.zapp.library.smg.mcom.model.LiteMcomPaymentRequest;
import com.zapp.library.smg.mcom.model.LiteMcomTransaction;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by msagi on 10/12/2015.
 */
public class ZappSMGService {

    /**
     * Tag for logging.
     */
    private static final String TAG = ZappSMGService.class.getSimpleName();

    /**
     * The Zapp Small Merchant Gateway
     */
    private interface ZappSmallMerchantGateway {

        String BASE_URL = "http://52.6.25.39:8080/merchant-gateway-mt/";

        @PUT("mcom/2/paymentrequest")
        Call<LiteMcomTransaction> submitMcomPaymentRequest(@Body LiteMcomPaymentRequest litePaymentRequest);

        @GET("2/paymentrequest/status/{aptrid}")
        Call<LitePaymentStatusResponse> retrievePaymentStatus(@Path("aptrid") String aptrid);

    }

    public LiteMcomTransaction submitPaymentRequest(@NonNull final LiteMcomPaymentRequest liteMcomPaymentRequest) {
        try {

            final ZappSmallMerchantGateway gateway = getZappSmallMerchantGateway();

            final Call<LiteMcomTransaction> gatewayCall = gateway.submitMcomPaymentRequest(liteMcomPaymentRequest);
            final Response<LiteMcomTransaction> gatewayResponse = gatewayCall.execute();

            if (gatewayResponse.isSuccess()) {
                final LiteMcomTransaction response = gatewayResponse.body();
                return response;
            } else {
                final ZappSMGAPIError apiError =  parseError(gatewayResponse, ZappSMGAPIError.class);
                Log.e(TAG, "Error: submitPaymentRequest: " + apiError);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error submitting payment request to Zapp Small Merchant Gateway", e);
        }

        return null;
    }

    public LitePaymentStatusResponse checkPaymentStatus(@NonNull final String aptrId) {
        try {
            final ZappSmallMerchantGateway gateway = getZappSmallMerchantGateway();
            final Call<LitePaymentStatusResponse> gatewayCall = gateway.retrievePaymentStatus(aptrId);
            final Response<LitePaymentStatusResponse> gatewayResponse = gatewayCall.execute();

            if (gatewayResponse.isSuccess()) {
                final LitePaymentStatusResponse response = gatewayResponse.body();
                return response;
            } else {
                final ZappSMGAPIError apiError =  parseError(gatewayResponse, ZappSMGAPIError.class);
                Log.e(TAG, "Error: checkPaymentStatus: " + apiError);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking payment status to Zapp Small Merchant Gateway", e);
        }
        return null;
    }

    /**
     * Get new gateway client instance for Zapp Small Merchant Gateway.
     *
     * @return The Zapp Small Merchant Gateway client.
     */
    public synchronized ZappSmallMerchantGateway getZappSmallMerchantGateway() {
        final OkHttpClient okHttpClient = new OkHttpClient();

        //add standard headers
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(final Chain chain) throws IOException {
                final Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        .addHeader("Accept-Charset", "utf-8")
                        .build();
                return chain.proceed(request);
            }
        });

        //add HTTP logging
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(httpLoggingInterceptor);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZappSmallMerchantGateway.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
        .build();
        return retrofit.create(ZappSmallMerchantGateway.class);
    }

    /**
     * Parse Retrofit 2 error body to Zapp Small Merchant Gateway API error object.
     * @param response The Retrofit 2 response.
     * @param retrofit The Retrofit 2 instance.
     * @return The {@link ZappSMGAPIError} error instance.
     */
    private <T extends ZappSMGAPIError> T parseError(@NonNull final Response response, Class<T> errorClass) {

        T error;

        try {
            Gson gson = new Gson();
            error = gson.fromJson(response.errorBody().string(), errorClass);
        } catch (IOException e) {
            error = null;
        }

        return error;
    }
}
