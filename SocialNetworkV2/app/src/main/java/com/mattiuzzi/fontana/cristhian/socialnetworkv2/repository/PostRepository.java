package com.mattiuzzi.fontana.cristhian.socialnetworkv2.repository;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.R;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.activity.FeedActivity;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.activity.UpdateRegisterActivity;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.data.PostData;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    private final Context context;

    public PostRepository(@NonNull Context context) {
        this.context = context;
    }

    public List<PostData> loadPostData(int limit, int offset) throws FileNotFoundException {
        SocialNetworkRepository socialNetworkRepository = new SocialNetworkRepository(context);

        JSONObject object = socialNetworkRepository.getPosts(limit, offset);

        int nPosts = 0;

        try {
            nPosts = object.getInt("n_posts");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        List<PostData> postDataList = new ArrayList<>();

        if (nPosts != 0) {
            try {
                JSONArray posts = object.getJSONArray("posts");

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);

                    int id = post.getInt("id");
                    String image = post.getString("imagem");
                    String authorImage = post.getString("usuario_foto");

                    String date = post.getString("data_hora");
                    String text = post.getString("texto");
                    String authorLogin = post.getString("usuario_login");
                    String authorName = post.getString("usuario_nome");

                    postDataList.add(new PostData(
                            id,
                            image,
                            authorImage,
                            date,
                            text,
                            authorName,
                            authorLogin)
                    );
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            postDataList.add(new PostData(
                    0,
                    null,
                    null,
                    "Agora",
                    "Ainda não fez um post? Clique no ícone a direita para realizar a sua primeira postagem!",
                    "Administrador",
                    "admin")
            );
        }

        return postDataList;
    }
}
