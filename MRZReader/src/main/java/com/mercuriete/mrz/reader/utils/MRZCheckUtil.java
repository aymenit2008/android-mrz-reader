package com.mercuriete.mrz.reader.utils;

import android.util.Log;

import org.jmrtd.lds.icao.MRZInfo;

public class MRZCheckUtil {

    private MRZCheckUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getMRZString(String mrz) {
        int start = mrz.indexOf("P<");
        int end = mrz.lastIndexOf('<');
        // if (start == -1 || end == -1) {
        if (start == -1) {
            return "null";
        }
        // get string between first and last < character
        String mrzInfo = mrz.substring(start );
        // if mrzInfo is long or equal  44  characters then return mrzInfo
        if (mrzInfo.length() >= 44 ) {
            return mrzInfo;
        }else {
            return "less than 44 characters";
        }

    }

    public static boolean check(String mrz) {
        try {
            // get position of first two P< characters

            // get position of first < and last > character
            //int start = mrz.indexOf('<');
            //int end = mrz.lastIndexOf('<');
            // get string between first and last < character
            //String mrzInfo = mrz.substring(start - 1, end);

            MRZInfo mrzInfo1 = new MRZInfo(mrz);
            // compute check digits using this constructor from JMRTD
            MRZInfo mrzInfo2 = new MRZInfo("P", // workaround for JMRTD ID is not supported
                    mrzInfo1.getIssuingState(),
                    mrzInfo1.getPrimaryIdentifier(),
                    mrzInfo1.getSecondaryIdentifier(),
                    mrzInfo1.getDocumentNumber(),
                    mrzInfo1.getNationality(),
                    mrzInfo1.getDateOfBirth(),
                    mrzInfo1.getGender(),
                    mrzInfo1.getDateOfExpiry(),
                    mrzInfo1.getPersonalNumber());

            mrzInfo2.setDocumentCode(mrzInfo1.getDocumentCode()); //undo workaround

            // If string representation of OCR is equals to computed check digits means is correct
            return mrzInfo1.toString().equals(mrzInfo2.toString());
        } catch (IllegalStateException | IllegalArgumentException e) {
            Log.w("CACA", "checksum fail", e);
            return false;
        }
    }
}
