package com.rozmer.service.external.payments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PayOrderRequest {

    @JsonProperty("payment_session_id")
    private String paymentSessionId;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("save_instrument")
    private Boolean saveInstrument;

    @JsonProperty("offer_id")
    private String offerId;

    // Inner classes for payment methods
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentMethod {
        private Card card;
        private Upi upi;
        private Netbanking netbanking;
        private App app;
        // Add other payment methods as needed

        // getters and setters
        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public Upi getUpi() {
            return upi;
        }

        public void setUpi(Upi upi) {
            this.upi = upi;
        }

        public Netbanking getNetbanking() {
            return netbanking;
        }

        public void setNetbanking(Netbanking netbanking) {
            this.netbanking = netbanking;
        }

        public App getApp() {
            return app;
        }

        public void setApp(App app) {
            this.app = app;
        }
    }

    public static class Card {
        private String channel; // link or post
        @JsonProperty("card_number")
        private String cardNumber;
        @JsonProperty("card_holder_name")
        private String cardHolderName;
        @JsonProperty("card_expiry_mm")
        private String cardExpiryMm;
        @JsonProperty("card_expiry_yy")
        private String cardExpiryYy;
        @JsonProperty("card_cvv")
        private String cardCvv;
        @JsonProperty("instrument_id")
        private String instrumentId;
        private String cryptogram;
        @JsonProperty("token_requestor_id")
        private String tokenRequestorId;
        @JsonProperty("token_reference_id")
        private String tokenReferenceId;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("card_display")
        private String cardDisplay;
        @JsonProperty("card_alias")
        private String cardAlias;
        @JsonProperty("card_bank_name")
        private String cardBankName;
        @JsonProperty("emi_tenure")
        private Integer emiTenure;

        // getters and setters
        // ... (omitted for brevity, generate with your IDE)
        // You can generate all getters/setters for these fields
    }

    public static class Upi {
        private String channel; // link, collect, qrcode, podQrCode
        @JsonProperty("upi_id")
        private String upiId;
        @JsonProperty("upi_redirect_url")
        private Boolean upiRedirectUrl;
        @JsonProperty("upi_expiry_minutes")
        private Integer upiExpiryMinutes;
        @JsonProperty("authorize_only")
        private Boolean authorizeOnly;
        private UPIAuthorizeDetails authorization;

        // getters and setters
    }

    public static class UPIAuthorizeDetails {
        @JsonProperty("approve_by")
        private String approveBy;
        @JsonProperty("start_time")
        private String startTime;
        @JsonProperty("end_time")
        private String endTime;

        // getters and setters
    }

    public static class Netbanking {
        private String channel; // always link
        @JsonProperty("netbanking_bank_code")
        private Integer netbankingBankCode;
        @JsonProperty("netbanking_bank_name")
        private String netbankingBankName;

        // getters and setters
    }

    public static class App {
        private String channel; // link
        private String provider; // gpay, phonepe, etc
        private String phone;

        // getters and setters
    }
}
