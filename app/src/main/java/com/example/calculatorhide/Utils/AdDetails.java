package com.example.calculatorhide.Utils;

import java.io.Serializable;

public class AdDetails implements Serializable {
    private String CalAdId, openAppId, homeAdId;

    public String getCalAdId() {
        return CalAdId;
    }

    public void setCalAdId(String calAdId) {
        this.CalAdId = calAdId;
    }

    public String getOpenAppId() {
        return openAppId;
    }

    public void setOpenAppId(String openAppId) {
        this.openAppId = openAppId;
    }

    public String getHomeAdId() {
        return homeAdId;
    }

    public void setHomeAdId(String homeAdId) {
        this.homeAdId = homeAdId;
    }
}
