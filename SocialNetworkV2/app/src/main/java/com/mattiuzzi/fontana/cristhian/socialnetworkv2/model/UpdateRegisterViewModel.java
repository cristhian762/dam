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

/**
 * ViewModel referente a RegisterActivity
 */
public class UpdateRegisterViewModel extends AndroidViewModel {

    String selectPhotoLocation = "";

    public String getSelectPhotoLocation() {
        return selectPhotoLocation;
    }

    public void setSelectPhotoLocation(String selectPhotoLocation) {
        this.selectPhotoLocation = selectPhotoLocation;
    }

    public UpdateRegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<JSONObject> findUserByLogin(
            String login
    ) {
        MutableLiveData<JSONObject> result = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SocialNetworkRepository socialNetworkRepository = new SocialNetworkRepository(getApplication());
                JSONObject b = socialNetworkRepository.findUserByLogin(login);
                result.postValue(b);
            }
        });

        return result;
    }

    public LiveData<Boolean> updateRegister(
            String newLogin,
            String newPassword,
            String name,
            String dateOfbBirth,
            String city,
            String currentPhotoPath
    ) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SocialNetworkRepository socialNetworkRepository = new SocialNetworkRepository(getApplication());

                boolean b = socialNetworkRepository.updateRegister(newLogin, newPassword, name, dateOfbBirth, city, currentPhotoPath);

                result.postValue(b);
            }
        });

        return result;
    }
}
