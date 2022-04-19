package ru.eustrosoft.androidqr.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Gallery");
    }

    public LiveData<String> getText() {
        return mText;
    }
}