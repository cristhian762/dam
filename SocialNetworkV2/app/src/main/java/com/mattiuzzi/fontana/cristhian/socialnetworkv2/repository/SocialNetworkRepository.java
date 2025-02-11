package com.mattiuzzi.fontana.cristhian.socialnetworkv2.repository;

import android.content.Context;
import android.util.Log;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Config;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.HttpRequest;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Essa classe concentra todos os métodos de conexão entre a app e o servidor web
 */
public class SocialNetworkRepository {

    Context context;
    public SocialNetworkRepository(Context context) {
        this.context = context;
    }

    /**
     * Método que cria uma requisição HTTP para registrar um novo usuário junto ao servidor web.
     * @param newLogin o login do novo usuário
     * @param newPassword a senha do novo usuário
     * @return true se o usuário foi cadastrado e false caso contrário
     */
    public boolean register(
            String newLogin,
            String newPassword,
            String name,
            String dateOfbBirth,
            String city,
            String currentPhotoPath
    ) {
        // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "cadastra_usuario.php", "POST", "UTF-8");
        httpRequest.addParam("login", newLogin);
        httpRequest.addParam("senha", newPassword);
        httpRequest.addParam("nome", name);
        httpRequest.addParam("cidade", city);
        httpRequest.addParam("data_nascimento", dateOfbBirth);
        httpRequest.addFile("foto", new File(currentPhotoPath));

        String result = "";

        try {
            // Executa a requisição HTTP. É neste momento que o servidor web é contactado. Ao executar
            // a requisição é aberto um fluxo de dados entre o servidor e a app (InputStream is).
            InputStream is = httpRequest.execute();

            // Obtém a resposta fornecida pelo servidor. O InputStream é convertido em uma String. Essa
            // String é a resposta do servidor web em formato JSON.
            //
            // Em caso de sucesso, será retornada uma String JSON no formato:
            //
            // {"sucesso":1}
            //
            // Em caso de falha, será retornada uma String JSON no formato:
            //
            // {"sucesso":0, "erro":"usuario já existe"}
            result = Util.inputStream2String(is, "UTF-8");

            Log.i("HTTP REGISTER RESULT", result);

            // Fecha a conexão com o servidor web.
            httpRequest.finish();

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, significa que o usuário foi registrado com sucesso.
            if(success == 1) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }
        return false;
    }

    /**
     * Método que cria uma requisição HTTP para autenticar um usuário junto ao servidor web.
     * @param login o login do usuário
     * @param password a senha do usuário
     * @return true se o usuário  foi autenticado, false caso contrário
     */
    public boolean login(String login, String password) {

        // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "login.php", "POST", "UTF-8");
        httpRequest.setBasicAuth(login, password);

        String result = "";
        try {
            // Executa a requisição HTTP. É neste momento que o servidor web é contactado. Ao executar
            // a requisição é aberto um fluxo de dados entre o servidor e a app (InputStream is).
            InputStream is = httpRequest.execute();

            // Obtém a resposta fornecida pelo servidor. O InputStream é convertido em uma String. Essa
            // String é a resposta do servidor web em formato JSON.
            //
            // Em caso de sucesso, será retornada uma String JSON no formato:
            //
            // {"sucesso":1}
            //
            // Em caso de falha, será retornada uma String JSON no formato:
            //
            // {"sucesso":0, "erro":"usuario ou senha não confere"}
            result = Util.inputStream2String(is, "UTF-8");

            // Fecha a conexão com o servidor web.
            httpRequest.finish();

            Log.i("HTTP LOGIN RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, significa que o usuário foi autenticado com sucesso.
            if(success == 1) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }
        return false;
    }

    public JSONObject findUserByLogin(String login) {
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "pegar_usuario.php", "GET", "UTF-8");
        httpRequest.addParam("login", login);

        String result = "";
        JSONObject jsonObject = null;

        try {
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");

            httpRequest.finish();

            Log.i("HTTP LOGIN RESULT", result);

            jsonObject = new JSONObject(result);

            int success = jsonObject.getInt("sucesso");

            if(success == 1) {
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }

        return jsonObject;
    }

    public boolean updateRegister(
            String newLogin,
            String newPassword,
            String name,
            String dateOfbBirth,
            String city,
            String currentPhotoPath
    ) {
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "atualizar_usuario.php", "POST", "UTF-8");
        httpRequest.setBasicAuth(Config.getLogin(context.getApplicationContext()), Config.getPassword(context.getApplicationContext()));

        httpRequest.addParam("login", newLogin);
        httpRequest.addParam("nome", name);
        httpRequest.addParam("cidade", city);
        httpRequest.addParam("data_nascimento", dateOfbBirth);
//        httpRequest.addFile("foto", new File(currentPhotoPath));

        httpRequest.setTimeout(15000);

        if (!newPassword.isEmpty()) {
            httpRequest.addParam("senha", newPassword);
        }

        String result = "";

        try {
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");

            Log.i("HTTP REGISTER RESULT", result);

            httpRequest.finish();

            JSONObject jsonObject = new JSONObject(result);

            int success = jsonObject.getInt("sucesso");

            if(success == 1) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }
        return false;
    }

    public JSONObject getPosts(int limit, int offset) {
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "pegar_posts.php", "GET", "UTF-8");
        httpRequest.setBasicAuth(Config.getLogin(context.getApplicationContext()), Config.getPassword(context.getApplicationContext()));

        httpRequest.addParam("limit", String.valueOf(limit));
        httpRequest.addParam("offset", String.valueOf(offset));
        httpRequest.addParam("usuario", Config.getLogin(context.getApplicationContext()));

        String result = "";

        JSONObject jsonObject = null;

        try {
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");

            Log.i("HTTP REGISTER RESULT", result);

            httpRequest.finish();

            jsonObject = new JSONObject(result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }

        return jsonObject;
    }

    public JSONObject setPost(String currentPhotoPath, String text) {
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "post.php", "POST", "UTF-8");
        httpRequest.setBasicAuth(Config.getLogin(context.getApplicationContext()), Config.getPassword(context.getApplicationContext()));

        httpRequest.addParam("texto", text);
        httpRequest.addFile("imagem", new File(currentPhotoPath));

        String result = "";

        JSONObject jsonObject = null;

        try {
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");

            Log.i("HTTP REGISTER RESULT", result);

            httpRequest.finish();

            jsonObject = new JSONObject(result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }

        return jsonObject;
    }
}
