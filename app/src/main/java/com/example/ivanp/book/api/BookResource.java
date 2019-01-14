package com.example.ivanp.book.api;

import com.example.ivanp.book.vo.Book;
import com.example.ivanp.book.vo.LoginBody;
import com.example.ivanp.book.vo.Page;
import com.example.ivanp.book.vo.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookResource {
//    String BASE_URL = "http://10.0.2.2:4926/api/";
    String BASE_URL="http://192.168.100.4:8080";   // mobile hotspot
//    String BASE_URL="http://192.168.100.4:8080";

    @POST("login")
    Call<ResponseBody> login(@Body LoginBody loginBody);

    @GET("books")
    Call<List<Book>> getBooks(@Header("Authorization") String credentials);

    @POST("books")
    Call<ResponseBody> addBook(@Header("Authorization") String credentials, @Body Book book);

    @DELETE("books/{id}")
    Call<ResponseBody> deleteBook(@Header("Authorization") String credentials, @Path("id") int bookId);

    @PUT("books/{id}")
    Call<ResponseBody> updateBook(@Header("Authorization") String credentials, @Path("id") int bookId, @Body Book book);
}
