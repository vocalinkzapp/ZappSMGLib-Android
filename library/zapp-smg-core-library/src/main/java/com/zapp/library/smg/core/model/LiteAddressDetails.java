package com.zapp.library.smg.core.model;

import com.zapp.library.smg.core.util.ValidationUtils;

/**
 * Address details model objects for Lite merchant gateway.
 * @author msagi
 */
public class LiteAddressDetails implements IValidatable {

    private String addressId;

    private String firstName;

    private String lastName;

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;

    private String addressLine4;

    private String postCode;

    public LiteAddressDetails(final String addressId, final String firstName, final String lastName, final String addressLine1, final String addressLine2,
            final String addressLine3, final String addressLine4, final String postCode) throws ZappModelValidationException {
        this.addressId = addressId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.addressLine4 = addressLine4;
        this.postCode = postCode;
        validate();
    }

    public String getAddressId() {
        return addressId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    public String getPostCode() {
        return postCode;
    }

    @Override
    public void validate() throws ZappModelValidationException {
        ValidationUtils.require(firstName, "firstName");
        ValidationUtils.require(addressLine1, "addressLine1");
    }
}
