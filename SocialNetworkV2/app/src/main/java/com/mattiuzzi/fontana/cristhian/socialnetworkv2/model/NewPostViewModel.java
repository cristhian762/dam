package com.mattiuzzi.fontana.cristhian.socialnetworkv2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.repository.SocialNetworkRepository;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewPostViewModel extends AndroidViewModel {

    String selectPhotoLocation = "";

    public String getSelectPhotoLocation() {
        return selectPhotoLocation;
    }

    public void setSelectPhotoLocation(String selectPhotoLocation) {
        this.selectPhotoLocation = selectPhotoLocation;
    }

    public NewPostViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<JSONObject> setPost(
            String currentPhotoPath,
            String text
    ) {
        MutableLiveData<JSONObject> result = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                SocialNetworkRepository socialNetworkRepository = new SocialNetworkRepository(getApplication());

                JSONObject b = socialNetworkRepository.setPost(currentPhotoPath, text);

                result.postValue(b);
            }
        });

        return result;
    }
}
