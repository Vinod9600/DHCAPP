package com.dhc.app.data.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vinod Durge on  07 Nov 2020
 */
public class Customer {
    private String CardCode;

    private String CardType;

    public Customer() {

    }

    public Customer(String cardCode, String cardType) {
        CardCode = cardCode;
        CardType = cardType;
    }

    public Customer(JSONObject jsonObject) throws JSONException {
        setCardCode(jsonObject.get("CardCode").toString());
        setCardType(jsonObject.get("CardType").toString());
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }
}
