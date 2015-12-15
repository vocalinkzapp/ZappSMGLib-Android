package com.zapp.smg.moviestore;

import com.zapp.smg.moviestore.service.BackendService;
import com.zapp.smg.moviestore.mock.service.MockBackendService;

/**
 * Custom application for Movie Store.
 * @author msagi
 */
public class Application extends android.app.Application {

    /**
     * The backend service instance (the application must have a single instance from this).
     */
    private BackendService mBackendService;

    @Override
    public void onCreate() {
        super.onCreate();

        //initialise (mock) backend service
//        mBackendService = new BackendService();
        mBackendService = new MockBackendService(getApplicationContext());
    }
}
