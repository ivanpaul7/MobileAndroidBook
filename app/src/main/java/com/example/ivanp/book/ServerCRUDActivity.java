package com.example.ivanp.book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivanp.book.api.BookResource;
import com.example.ivanp.book.vo.Book;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerCRUDActivity extends AppCompatActivity {
    private final String TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTQzODM2MTg1fQ.EY93arQNo2pnkAXz9-MjlLe8bZrqilo7mIk_o0uXSSUVjm6h0CRORlyPRMMPRa88iuAuHlvCXV0FfUBO6M_z9A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_crud);
    }

    public void add(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Book book= new Book();
        book.setAuthor(getAuthorFromEditText());
        book.setTitle(getTitleFromEditText());
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.addBook(
                TOKEN,
                book
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ServerCRUDActivity.this, "Yuppy", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ServerCRUDActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.deleteBook(
                TOKEN,
                getIDFromEditText()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ServerCRUDActivity.this, "Yuppy", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ServerCRUDActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void edit(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Book book= new Book();
        book.setAuthor(getAuthorFromEditText());
        book.setTitle(getTitleFromEditText());
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.updateBook(
                TOKEN,
                getIDFromEditText(),
                book
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ServerCRUDActivity.this, "Yuppy", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ServerCRUDActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getTitleFromEditText(){
        EditText titleTV = (EditText) findViewById(R.id.title_input);
        return titleTV.getText().toString();
    }

    private String getAuthorFromEditText(){
        EditText authorTV = (EditText) findViewById(R.id.author_input);
        return  authorTV.getText().toString();
    }

    private int getIDFromEditText(){
        EditText idTV = (EditText) findViewById(R.id.id_input);
        return Integer.parseInt(idTV.getText().toString());
    }
}
