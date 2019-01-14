package com.example.ivanp.book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ivanp.book.api.BookResource;
import com.example.ivanp.book.vo.Book;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stanford.androidlib.SimpleActivity;
import stanford.androidlib.data.SimpleDatabase;


public class MixedCRUDActivity extends SimpleActivity {
    private static final String TAG = MixedCRUDActivity.class.getCanonicalName();
    private final String TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTQ1NzY4ODYxfQ.ynYeKbGPzhThrW-zr4JaLGYuY9B4V66BzkDCUPOWzcto3oaneo2pciDxN20oXLsnGxedD_cLYiiwoGu95RgBuQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixed_crud);

//        SimpleDatabase.with(this).executeSqlFile("syncbookscript");
        loadData();
    }

    public void add(View view) {
        if (hasInternetConnection()) {
            addOnServer(getAuthorFromEditText(), getTitleFromEditText(), false);
        } else {
            addInLocalStorage( getIDFromEditText(),getAuthorFromEditText(),getTitleFromEditText(),false);
        }
    }

    private void addInLocalStorage(int id, String author, String title,boolean isAddedOnServer) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
            db.execSQL("INSERT INTO SyncBooks (id, title, author, isFromServer, isModified, isActive) " +
                    "VALUES (" + id + ", '" + title + "', '" + author + "', " + (isAddedOnServer ? 1 : 0) + ", 0, 1 )");
            loadData();
        } catch (SQLiteConstraintException err) {

        }
    }

    public void delete(View view) {
        if (isFromServer(getIDFromEditText())) {
            deleteOnServer(getIDFromEditText(),false);
        } else {
            deleteFromLocalStorage(false);
        }
    }

    private void deleteFromLocalStorage(boolean softDelete) {
        if (softDelete) {
            SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
            db.execSQL("UPDATE SyncBooks SET isActive=0 WHERE id=" + getIDFromEditText());
        } else {
            SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
            db.execSQL("DELETE FROM SyncBooks WHERE id=" + getIDFromEditText());
        }
        loadData();
    }

    private boolean isFromServer(int id) {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT * FROM SyncBooks WHERE id=" + getIDFromEditText(), null);
        if (cr.getCount() > 0) {
            cr.close();
            return true;
        } else {
            cr.close();
            return false;
        }
    }


    public void edit(View view) {
        editOnServer(getIDFromEditText(),getAuthorFromEditText(), getTitleFromEditText());
        editLocalStorage();
    }

    public void editLocalStorage() {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        db.execSQL("UPDATE SyncBooks " +
                "SET title='" + getTitleFromEditText() + "', author='" + getAuthorFromEditText() + "', isModified=1 " +
                "WHERE id=" + getIDFromEditText());
        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT * FROM SyncBooks ", null);
        List<String> books = new ArrayList<String>();
        if (cr.moveToFirst()) {
            do {
                books.add("" +
                        cr.getInt(cr.getColumnIndex("id")) + ". " +
                        cr.getString(cr.getColumnIndex("title")) + ", by " +
                        cr.getString(cr.getColumnIndex("author")) + "  (" +
                        cr.getString(cr.getColumnIndex("isFromServer")) + " " +
                        cr.getString(cr.getColumnIndex("isModified")) + " " +
                        cr.getString(cr.getColumnIndex("isActive")) + ")"
                );
            } while (cr.moveToNext());
            cr.close();

            ListView lv = (ListView) findViewById(R.id.book_list_local_storage);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    books);
            lv.setAdapter(arrayAdapter);
        }
    }

    private String getTitleFromEditText() {
        EditText titleTV = (EditText) findViewById(R.id.title_input);
        return titleTV.getText().toString();
    }

    private String getAuthorFromEditText() {
        EditText authorTV = (EditText) findViewById(R.id.author_input);
        return authorTV.getText().toString();
    }

    private int getIDFromEditText() {
        EditText idTV = (EditText) findViewById(R.id.id_input);
        return Integer.parseInt(idTV.getText().toString());
    }

    public void addOnServer(String author, String title, final boolean isOnSync) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.addBook(
                TOKEN,
                book
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isOnSync)
                    addInLocalStorage(getIDFromEditText(),getAuthorFromEditText(),getTitleFromEditText(),true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isOnSync)
                    addInLocalStorage(getIDFromEditText(),getAuthorFromEditText(),getTitleFromEditText(),false);
            }
        });
    }

    public void deleteOnServer(int id, final boolean isOnSync) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.deleteBook(
                TOKEN,
                id
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!isOnSync)
                    deleteFromLocalStorage(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!isOnSync)
                    deleteFromLocalStorage(true);
            }
        });
    }

    public void editOnServer(int id, String author, String title) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        BookResource api = retrofit.create(BookResource.class);
        Call<ResponseBody> call = api.updateBook(
                TOKEN,
                id,
                book
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public boolean hasInternetConnection() {
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public void sync(View view) {
        if (hasInternetConnection()) {
            addLocalBooksToServer();
            updateLocalBooksToServer();
            deleteLocalBooksToServer();
            deleteAllBooksFromLocalStorage();
            getAllBooksFromServer();
            Toast.makeText(MixedCRUDActivity.this, "Done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MixedCRUDActivity.this, "The device is offline", Toast.LENGTH_SHORT).show();
        }
    }


    private void addLocalBooksToServer() {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT * FROM SyncBooks WHERE isFromServer=0 AND isActive=1", null);
        if (cr.moveToFirst()) {
            do {
                addOnServer(cr.getString(cr.getColumnIndex("author")), cr.getString(cr.getColumnIndex("title")), true);
            } while (cr.moveToNext());
            cr.close();
        }
    }

    private void updateLocalBooksToServer() {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT * FROM SyncBooks WHERE isFromServer=1 AND isModified=1 AND isActive=1", null);
        if (cr.moveToFirst()) {
            do {
                editOnServer(Integer.parseInt(cr.getString(cr.getColumnIndex("id"))) ,cr.getString(cr.getColumnIndex("author")), cr.getString(cr.getColumnIndex("title")));
            } while (cr.moveToNext());
            cr.close();
        }
    }

    private void deleteLocalBooksToServer() {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT * FROM SyncBooks WHERE isFromServer=1 AND isActive=0", null);
        if (cr.moveToFirst()) {
            do {
                deleteOnServer(Integer.parseInt(cr.getString(cr.getColumnIndex("id"))), true);
            } while (cr.moveToNext());
            cr.close();
        }
    }

    private void deleteAllBooksFromLocalStorage() {
        SQLiteDatabase db = openOrCreateDatabase("syncbookscript", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM SyncBooks");
    }

    private void getAllBooksFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookResource api = retrofit.create(BookResource.class);
        Call<List<Book>> call = api.getBooks(  TOKEN);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                for(Book book: response.body()){
                   addInLocalStorage(book.getId(),book.getAuthor(),book.getTitle(), true);
                }
                loadData();
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d(TAG,"Error");
            }
        });
    }

}
