package com.jagatbrahma.android.transmitsecuritydemo.sdk;

import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

public class TransmitSDK {

    private static final String GoodPassword = "goodpassword";
    private static final String GoodPincode = "12345";
    private static final boolean GoodFinger = true;

    public interface OnListResult {
        void onComplete(List<Authenticator> result);
    }

    public interface OnResult {
        void onComplete();

        void onReject(String error);
    }

    public enum Authenticator {
        PASSWORD, PINCODE, FINGERPRINT
    }

    private static final TransmitSDK instance = new TransmitSDK();

    public static TransmitSDK getInstance() {
        return instance;
    }

    public void authenticatorsList(TransmitSDK.OnListResult onResult) {
        new TransmitSDK.ListAsync(onResult).execute();
    }

    public void authenticateWithPassword(String password, TransmitSDK.OnResult onResult) {
        password = password.trim();
        new TransmitSDK.AuthAsync(TransmitSDK.Authenticator.PASSWORD, password, onResult).execute();
    }

    public void authenticateWithPincode(String pincode, TransmitSDK.OnResult onResult) {
        new TransmitSDK.AuthAsync(TransmitSDK.Authenticator.PINCODE, pincode, onResult).execute();
    }

    public void authenticateWithFingerprint(boolean useFingerprint, TransmitSDK.OnResult onResult) {
        new TransmitSDK.AuthAsync(TransmitSDK.Authenticator.FINGERPRINT, useFingerprint, onResult).execute();
    }

    private static class AuthAsync extends AsyncTask<Void, Void, Boolean> {
        private final TransmitSDK.Authenticator type;
        private final Object content;
        private final TransmitSDK.OnResult onResult;

        private AuthAsync(TransmitSDK.Authenticator type, Object content, TransmitSDK.OnResult onResult) {
            this.type = type;
            this.content = content;
            this.onResult = onResult;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(720);
                if (type == TransmitSDK.Authenticator.PASSWORD) {
                    return content.equals(GoodPassword);
                } else if (type == TransmitSDK.Authenticator.PINCODE) {
                    return content.equals(GoodPincode);
                } else if (type == TransmitSDK.Authenticator.FINGERPRINT) {
                    return content.equals(GoodFinger);
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                onResult.onComplete();
            } else {
                onResult.onReject("Authenticator failed due to invalid input.");
            }
        }
    }

    private static class ListAsync extends AsyncTask<Void, Void, List<TransmitSDK.Authenticator>> {
        private final TransmitSDK.OnListResult onResult;

        private ListAsync(TransmitSDK.OnListResult onResult) {
            this.onResult = onResult;
        }

        @Override
        protected List<TransmitSDK.Authenticator> doInBackground(Void... voids) {
            List<TransmitSDK.Authenticator> list = Arrays.asList(TransmitSDK.Authenticator.values());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                return null;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<TransmitSDK.Authenticator> authenticators) {
            super.onPostExecute(authenticators);
            onResult.onComplete(authenticators);
        }
    }

}