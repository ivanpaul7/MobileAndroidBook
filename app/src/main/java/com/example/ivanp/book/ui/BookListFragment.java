package com.example.ivanp.book.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivanp.book.R;
import com.example.ivanp.book.api.BookResource;
import com.example.ivanp.book.vo.Book;
import com.example.ivanp.book.viewmodel.BooksViewModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookListFragment extends Fragment {

    private BooksViewModel mBooksViewModel;
    private RecyclerView mBookList;

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBookList = view.findViewById(R.id.book_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBookList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBooksViewModel = ViewModelProviders.of(this).get(BooksViewModel.class);
        mBooksViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                BookListAdapter booksAdapter = new BookListAdapter(getActivity(), books);
                mBookList.setAdapter(booksAdapter);
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add_button:
//                add(v);
//                break;
//            case R.id.delete_button:
//                delete(v);
//            case R.id.edit_button:
//                edit(v);
//            default:
//        }
//    }



}
