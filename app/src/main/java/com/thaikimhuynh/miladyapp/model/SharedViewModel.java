package com.thaikimhuynh.miladyapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedParentPosition = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedChildPosition = new MutableLiveData<>();

    public void setSelectedItem(int parentPosition, int childPosition) {
        selectedParentPosition.setValue(parentPosition);
        selectedChildPosition.setValue(childPosition);
    }

    public LiveData<Integer> getSelectedParentPosition() {
        return selectedParentPosition;
    }

    public LiveData<Integer> getSelectedChildPosition() {
        return selectedChildPosition;
    }
}
