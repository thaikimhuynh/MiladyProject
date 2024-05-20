package com.thaikimhuynh.miladyapp.model;

import java.io.Serializable;

public class PointWallet implements Serializable {
    private int walletId;
    private String userId;
    private int totalPoint;

    public PointWallet(int walletId, String userId, int totalPoint) {
        this.walletId = walletId;
        this.userId = userId;
        this.totalPoint = totalPoint;
    }

    public PointWallet() {
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }
}
