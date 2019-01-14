package com.example.ivanp.book.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivanp.book.R;
import com.example.ivanp.book.vo.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> mBooks;

    public BookListAdapter(Context context, List<Book> books) {
        this.mContext = context;
        this.mBooks = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_layout, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.textView.setText(book.getTitle()+ " "+ book.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        BookViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
