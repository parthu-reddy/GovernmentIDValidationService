package com.fooddelivery.governmentid.entity;

public enum DocumentType {
    AADHAAR(10),
    PAN(20),
    DRIVING_LICENSE(30),
    RC(40),
    SELFIE(50),
    GSTIN(60),
    FSSAI(70);

    private final int code;

    DocumentType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
