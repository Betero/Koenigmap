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
        lstBook.add(new Book("Кафедральный собор", "Памятник архитектуры",
                "В 1330 году в восточной части Кнайпхофа началось строительство нового Кафедрального собора Кенигсберга, посвященного святому Адальберту и деве Марии.  Освящение состоялось,  вероятно,  до 1351 года. Около 1380 года строительство было закончено." +
                        "До начала церковной реформации XVI века собор являлся главным католическим храмом города Кёнигсберга (был посвящён Высокосвященному телу Иисуса Христа, Деве Марии, Всем Святым и Святому Адальберту), а затем главным лютеранским храмом Пруссии.",
                R.drawable.kaf));
        lstBook.add(new Book("Кирха королевы Луизы", "Памятник архитектуры", "Описание", R.drawable.luiz));
        lstBook.add(new Book("Понарт", "Действующая", "Кирха была освящена 23 июля 1897 года. Кирпичная постройка в готическом стиле имела четырёхэтажную башню на северо-западе и фронтоны на востоке и западе. С двух сторон постройки располагались узкие контрфорсы. С юга находилась алтарная часть с ризницей, к алтарной постройке примыкал фамильный склеп Шиффердекеров. На северной стороне кирхи размещался красивый фронтон с пятью слепыми окнами," +
                " там же был расположен притвор, объединённый пристройкой с башней. С восточной стороны здания окна были высокие, стрельчатые, с запада в стрельчатых нишах находились двойные окна с эмпорами. На двускатной крыше был установлен фонарь с высоким острым шпилем (до наших дней не сохранился).\n", R.drawable.privacy));
        lstBook.add(new Book("The Martian", "Categorie Book", "Description book", R.drawable.privacy));
        lstBook.add(new Book("He Died with...", "Categorie Book", "Description book", R.drawable.privacy));
        lstBook.add(new Book("The Vegitarian", "Categorie Book", "Description book", R.drawable.privacy));
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

