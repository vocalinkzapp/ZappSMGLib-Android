package com.zapp.smg.moviestore.service;

import com.msagi.flashbus.annotation.Subscribe;
import com.msagi.flashbus.annotation.ThreadId;
import com.zapp.smg.moviestore.FlashBus;
import com.zapp.smg.moviestore.model.backend.AddItemToShoppingCartRequest;
import com.zapp.smg.moviestore.model.backend.AddItemToShoppingCartResponse;
import com.zapp.smg.moviestore.model.backend.CreateOrderRequest;
import com.zapp.smg.moviestore.model.backend.CreateOrderResponse;
import com.zapp.smg.moviestore.model.backend.GetCatalogueRequest;
import com.zapp.smg.moviestore.model.backend.GetCatalogueResponse;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusRequest;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusResponse;

import android.support.annotation.NonNull;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class BackendService {

    private interface Gateway {

        @GET("/catalogue")
        Call<GetCatalogueResponse> getMovieList();

        @POST("/cart/add/{itemId}")
        Call<AddItemToShoppingCartResponse> addItemToShoppingCart(@Path("itemId") String itemId);

        @POST("/payment/{paymentMethod}")
        Call<CreateOrderResponse> submitPaymentRequest(@Path("paymentMethod") String paymentMethod);

        @GET("/payment/status/{transactionId}")
        Call<GetPaymentStatusResponse> getPaymentStatus(@Path("transactionId") String transactionId);
    }

    /**
     * The FlashBus instance.
     */
    private FlashBus mBus;

    /**
     * Create new instance and connect to the bus.
     */
    public BackendService() {
        mBus = FlashBus.getDefault();
//        mBus.register(this);
    }

    /**
     * Return the base url for gateway.
     *
     * @return The gateway base url.
     */
    protected String getGatewayBaseUrl() {
        throw new IllegalStateException("Gateway must be mocked");
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    public void getCatalogue(@NonNull final GetCatalogueRequest event) {
        try {
            final Gateway gateway = getGateway();
            final Call<GetCatalogueResponse> gatewayCall = gateway.getMovieList();
            final Response<GetCatalogueResponse> gatewayResponse = gatewayCall.execute();
            final GetCatalogueResponse response = gatewayResponse.body();

            mBus.post(response);
        } catch (Exception e) {
            mBus.post(new BackendAPIError<>(e));
        }
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    public void addItemToShoppingCart(@NonNull final AddItemToShoppingCartRequest event) {
        try {
            final Gateway gateway = getGateway();
            final Call<AddItemToShoppingCartResponse> gatewayCall = gateway.addItemToShoppingCart(event.getItemUUID().toString());
            final Response<AddItemToShoppingCartResponse> gatewayResponse = gatewayCall.execute();
            final AddItemToShoppingCartResponse response = gatewayResponse.body();

            mBus.post(response);
        } catch (Exception e) {
            mBus.post(new BackendAPIError<>(e));
        }
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    public void createOrder(@NonNull final CreateOrderRequest event) {
        try {
            final Gateway gateway = getGateway();
            final Call<CreateOrderResponse> gatewayCall = gateway.submitPaymentRequest(event.getPaymentMethod().toString());
            final Response<CreateOrderResponse> gatewayResponse = gatewayCall.execute();
            if (gatewayResponse.isSuccess()) {
                final CreateOrderResponse response = gatewayResponse.body();
                mBus.post(response);
            } else {

            }
        } catch (Exception e) {
            mBus.post(new BackendAPIError<>(e));
        }
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    public void getPaymentStatus(@NonNull final GetPaymentStatusRequest event) {
        try {
            final Gateway gateway = getGateway();
            final Call<GetPaymentStatusResponse> gatewayCall = gateway.getPaymentStatus(event.getTransactionId().toString());
            final Response<GetPaymentStatusResponse> gatewayResponse = gatewayCall.execute();
            final GetPaymentStatusResponse response = gatewayResponse.body();

            mBus.post(response);
        } catch (Exception e) {
            mBus.post(new BackendAPIError<>(e));
        }
    }

    /**
     * Get new gateway client instance.
     *
     * @return The gateway client.
     */
    private synchronized Gateway getGateway() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getGatewayBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Gateway.class);
    }
}