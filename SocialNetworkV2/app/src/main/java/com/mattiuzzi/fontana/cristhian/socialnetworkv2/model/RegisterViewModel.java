package com.mattiuzzi.fontana.cristhian.socialnetworkv2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.repository.SocialNetworkRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel referente a RegisterActivity
 */
public class RegisterViewModel extends AndroidViewModel {

    String selectPhotoLocation = "";

    public String getSelectPhotoLocation() {
        return selectPhotoLocation;
    }

    public void setSelectPhotoLocation(String selectPhotoLocation) {
        this.selectPhotoLocation = selectPhotoLocation;
    }

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> register(
            String newLogin,
            String newPassword,
            String name,
            String dateOfbBirth,
            String city,
            String currentPhotoPath
    ) {
        // Cria um container do tipo MutableLiveData (um LiveData que pode ter seu conteúdo alterado).
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        // Cria uma nova linha de execução (thread). O android obriga que chamadas de rede sejam feitas
        // em uma linha de execução separada da principal.
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Executa a nova linha de execução. Dentro dessa linha, iremos realizar as requisições ao
        // servidor web.
        executorService.execute(new Runnable() {

            /**
             * Tudo o que colocármos dentro da função run abaixo será executada dentro da nova linha
             * de execução.
             */
            @Override
            public void run() {

                // Criamos uma instância de ProductsRepository. É dentro dessa classe que estão os
                // métodos que se comunicam com o servidor web.
                SocialNetworkRepository socialNetworkRepository = new SocialNetworkRepository(getApplication());

                // O método login envia os dados de novo usuário ao servidor. Ele retorna
                // um booleano indicando true caso o cadastro de novo usuário tenha sido feito com sucesso e false
                // em caso contrário
                boolean b = socialNetworkRepository.register(newLogin, newPassword, name, dateOfbBirth, city, currentPhotoPath);

                // Aqui postamos o resultado da operação dentro do LiveData. Quando fazemos isso,
                // quem estiver observando o LiveData será avisado de que o resultado está disponível.
                result.postValue(b);
            }
        });

        return result;
    }
}
