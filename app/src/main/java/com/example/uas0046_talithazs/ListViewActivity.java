package com.example.uas0046_talithazs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {


    SQLiteDatabase SQLITEDATABASE;
    SQLiteHelper SQLITEHELPER;
    SQLiteListAdapter ListAdapter;

    Cursor cursor;

    ArrayList<String> ID_ArrayList = new ArrayList<>();
    ArrayList<String> TITLE_ArrayList = new ArrayList<>();
    ArrayList<String> X_ArrayList = new ArrayList<>();

    ListView listView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.bt_db);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.bt_db:
                        return true;

                    case R.id.bt_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bt_sensor:
                        startActivity(new Intent(getApplicationContext(),ProximitySensor.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        listView = findViewById(R.id.list);

        SQLITEHELPER = new SQLiteHelper(this);
    }

    @Override
    protected void onResume() {

        TampilSQLiteDBData();
        super.onResume();
    }

    private void TampilSQLiteDBData() {
        SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
        cursor = SQLITEDATABASE.rawQuery("SELECT * FROM Nama_Tabel", null);

        ID_ArrayList.clear();
        TITLE_ArrayList.clear();
        X_ArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                ID_ArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_ID)));
                TITLE_ArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_TITLE)));
                X_ArrayList.add(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_X)));
            } while (cursor.moveToNext());
        }

        ListAdapter = new SQLiteListAdapter(ListViewActivity.this, ID_ArrayList, TITLE_ArrayList,X_ArrayList);

        listView.setAdapter(ListAdapter);
        cursor.close();
    }
}