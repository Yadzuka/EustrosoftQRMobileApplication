package ru.eustrosoft.androidqr.ui.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CommentsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}