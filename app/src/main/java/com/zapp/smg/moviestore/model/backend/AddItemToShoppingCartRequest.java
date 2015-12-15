package com.zapp.smg.moviestore.model.backend;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Event to trigger 'add item to shopping cart' feature. It delivers the UUID of the item to be added to the shopping cart.
 */
public class AddItemToShoppingCartRequest {
    private final UUID mItemUUID;

    public AddItemToShoppingCartRequest(@NonNull final UUID itemUUID) {
        mItemUUID = itemUUID;
    }

    public UUID getItemUUID() {
        return mItemUUID;
    }
}
