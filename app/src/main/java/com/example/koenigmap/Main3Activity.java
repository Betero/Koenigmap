package com.example.koenigmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private ArrayList<Book> lstBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        lstBook = new ArrayList<>();
        lstBook.add(new Book("The Vegitarian", "Categorie Book", "Description book", R.drawable.kaf));
        lstBook.add(new Book("Название", "Другая категория", "Описание", R.drawable.luiz));
        lstBook.add(new Book("Maria Semples", "Categorie Book", "Description book", R.drawable.kirh_marker_icon));
        lstBook.add(new Book("The Martian", "Categorie Book", "Description book", R.drawable.altshtadt));
        lstBook.add(new Book("He Died with...", "Categorie Book", "Description book", R.drawable.frida));
        lstBook.add(new Book("The Vegitarian", "Categorie Book", "Description book", R.drawable.kaf));
        lstBook.add(new Book("The Wild Robot", "Categorie Book", "Description book", R.drawable.luiz));
        lstBook.add(new Book("Maria Semples", "Categorie Book", "Description book", R.drawable.kirh_marker_icon));
        lstBook.add(new Book("The Martian", "Categorie Book", "Description book", R.drawable.altshtadt));
        lstBook.add(new Book("He Died with...", "Categorie Book", "Description book", R.drawable.frida));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstBook);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));
        myrv.setAdapter(myAdapter);


    }
}

