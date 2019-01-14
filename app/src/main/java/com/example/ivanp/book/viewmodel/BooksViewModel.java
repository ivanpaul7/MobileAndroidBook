package com.example.ivanp.book.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.ivanp.book.api.BookResource;
import com.example.ivanp.book.vo.Book;
import com.example.ivanp.book.vo.Page;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksViewModel extends ViewModel {
    private static final String TAG = BooksViewModel.class.getCanonicalName();
    private MutableLiveData<List<Book>> books;

    public LiveData<List<Book>> getBooks() {
        if (books == null) {
            books = new MutableLiveData<List<Book>>();
            loadBooks();
//            loadMockBooks();
        }
        return books;
    }

    private void loadBooks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookResource api = retrofit.create(BookResource.class);
        Call<List<Book>> call = api.getBooks(
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTQzODM2MTg1fQ.EY93arQNo2pnkAXz9-MjlLe8bZrqilo7mIk_o0uXSSUVjm6h0CRORlyPRMMPRa88iuAuHlvCXV0FfUBO6M_z9A");
        Log.d(TAG, "loadBook");
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                Log.d(TAG, "loadBook succeeded");
                books.setValue(response.body());
                Log.d(TAG,response.body().toString());
                Log.d(TAG, "11111111111: "+response);

//                Page p=response.body();
//                tasks.setValue(p.getTasks());
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG, "loadBook failed", t);

            }
        });
    }

    private void loadMockBooks() {
        List<Book> booksList= new ArrayList<>();
//        booksList.add(new Book(1,"The Idiot","Feodor Dostoievski"));
//        booksList.add(new Book(2,"Crime and punishment","Feodor Dostoievski"));
//        booksList.add(new Book(3,"Anna Karenina","Leo Tolstoi"));
//        booksList.add(new Book(4,"War and peace","Leo Tolstoi"));
//        books.setValue(booksList);
    }

}
