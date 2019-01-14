package com.example.ivanp.book;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import stanford.androidlib.SimpleActivity;
import stanford.androidlib.data.SimpleDatabase;

public class LocalStorageCRUDActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_storage_crud);

//        SimpleDatabase.with(this).executeSqlFile("bookscript");
        loadData();
    }

    public void add(View view) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("bookscript", MODE_PRIVATE, null);
            db.execSQL("INSERT INTO Books (id, title, author) VALUES ("+getIDFromEditText()+", '"+getTitleFromEditText()+"', '"+getAuthorFromEditText()+"')");
            loadData();
        }catch (SQLiteConstraintException err){

        }

    }

    public void delete(View view) {
        SQLiteDatabase db = openOrCreateDatabase("bookscript", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM Books WHERE id="+getIDFromEditText());
        loadData();
    }

    public void edit(View view) {
        SQLiteDatabase db = openOrCreateDatabase("bookscript", MODE_PRIVATE, null);
        db.execSQL("UPDATE Books SET title='"+getTitleFromEditText()+"', author='"+getAuthorFromEditText()+"' WHERE id="+getIDFromEditText());
        loadData();
    }

    private void loadData(){
        SQLiteDatabase db = openOrCreateDatabase("bookscript", MODE_PRIVATE, null);
        Cursor cr = db.rawQuery(
                "SELECT * FROM Books ", null);
        List<String> books = new ArrayList<String>();
        if (cr.moveToFirst()) {
            do {
                books.add(""+
                        cr.getInt(cr.getColumnIndex("id")) +". "+
                        cr.getString(cr.getColumnIndex("title"))  +", by "+
                        cr.getString(cr.getColumnIndex("author"))
                );
            } while (cr.moveToNext());
            cr.close();

            ListView lv = (ListView) findViewById(R.id.book_list_local_storage);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    books );
            lv.setAdapter(arrayAdapter);
        }
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
