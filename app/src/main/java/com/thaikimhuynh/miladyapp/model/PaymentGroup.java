package com.thaikimhuynh.miladyapp.model;

import java.util.List;

public class PaymentGroup {

    private List<PaymentItem> itemList;
    private String itemText;
    private boolean isExpandable;
    int selectedItemPosition;

    public PaymentGroup(List<PaymentItem> itemList, String itemText) {
        this.itemList = itemList;
        this.itemText = itemText;
        isExpandable = false;
        selectedItemPosition = -1;
    }

    public List<PaymentItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<PaymentItem> itemList) {
        this.itemList = itemList;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }
}
