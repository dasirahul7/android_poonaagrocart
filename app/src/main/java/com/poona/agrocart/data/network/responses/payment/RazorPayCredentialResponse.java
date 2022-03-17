package com.poona.agrocart.data.network.responses.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

public class RazorPayCredentialResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private PaymentCredential data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentCredential getData() {
        return data;
    }

    public void setData(PaymentCredential data) {
        this.data = data;
    }

    public class PaymentCredential {
        @SerializedName("payment_credential_id")
        @Expose
        private String paymentCredentialId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("key_id")
        @Expose
        private String keyId;
        @SerializedName("key_secret")
        @Expose
        private String keySecret;
        @SerializedName("currency")
        @Expose
        private String currency;

        public String getPaymentCredentialId() {
            return paymentCredentialId;
        }

        public void setPaymentCredentialId(String paymentCredentialId) {
            this.paymentCredentialId = paymentCredentialId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getKeySecret() {
            return keySecret;
        }

        public void setKeySecret(String keySecret) {
            this.keySecret = keySecret;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
