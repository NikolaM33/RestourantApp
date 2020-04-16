package com.example.restourantapp.view;

public class CountrySpinner {
    private String mCountryCode;
    private int mFlagImage;

    public CountrySpinner(String countryCode, int flagImage) {
        mCountryCode = countryCode;
        mFlagImage = flagImage;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public int getFlagImage() {

        return mFlagImage;
    }//https://www.youtube.com/watch?v=GeO5F0nnzAw
}
