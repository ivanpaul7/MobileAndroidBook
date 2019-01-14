package com.example.ivanp.book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivanp.book.api.BookResource;
import com.example.ivanp.book.ui.BookListFragment;
import com.example.ivanp.book.vo.Book;
import com.example.ivanp.book.vo.LoginBody;
import com.example.ivanp.book.vo.User;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText usernameView = (EditText) findViewById(R.id.username_input);
        final String username= usernameView.getText().toString();
        EditText passwordView = (EditText) findViewById(R.id.password_input);
        final String password= passwordView.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.login(new LoginBody(username, password));


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.v("Book", response.headers().get("Authorization").toString());
                 Toast.makeText(LoginActivity.this,response.headers().get("Authorization"), Toast.LENGTH_LONG).show();
                // TODO Redirect
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
