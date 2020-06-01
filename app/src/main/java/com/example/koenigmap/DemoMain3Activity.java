package com.example.koenigmap;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DemoMain3Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MainData> dataArrayList = new ArrayList<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_main3);

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new MainAdapter(DemoMain3Activity.this,dataArrayList);
        //Set layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //Set adapter
        recyclerView.setAdapter(adapter);

        //create get data method
        getDate();
    }

    private void getDate() {
        final ProgressDialog dialog = new ProgressDialog(DemoMain3Activity.this);
        //Set massage on dialog
        dialog.setMessage("Пожалуйста, подождите");
        //Set non cancelable
        dialog.setCancelable(false);
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        //Создаем интерфейс
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> stringCall = mainInterface.STRING_CALL();

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check condition
                if (response.isSuccessful() && response.body() != null){
                    //When response is succussful and not null
                    //Dismiss dialog
                    dialog.dismiss();
                    //Initialize response json array
                    try {
                        //Initialize response json array
                        JSONArray jsonArray = new JSONArray(response.body());
                        //Parse json array
                        parseArray(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void parseArray(JSONArray jsonArray) {
        //Clear array list
        dataArrayList.clear();
        //Use for loop
        for (int i=0; i<jsonArray.length(); i++){
            try {
                //Initialize json object
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Initialize main data
                MainData data = new MainData();
                //Set image
                data.setImage(jsonObject.getString("download_url"));
                //Set name
                data.setName(jsonObject.getString("author"));
                //Add data in list
                dataArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new MainAdapter(DemoMain3Activity.this, dataArrayList);
            //Set adapter
            recyclerView.setAdapter(adapter);
        }
    }
}
