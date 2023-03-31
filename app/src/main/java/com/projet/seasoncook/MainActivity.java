package com.projet.seasoncook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.projet.seasoncook.database.DatabaseHelper;
import com.projet.seasoncook.models.CookType;
import com.projet.seasoncook.models.Etape;
import com.projet.seasoncook.models.Ingredient;
import com.projet.seasoncook.models.IngredientUnity;
import com.projet.seasoncook.models.Recette;
import com.projet.seasoncook.models.Seasons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Seasons params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setParams();

        RadioGroup rGroup = (RadioGroup)findViewById(R.id.choose_season);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String season = "";
                for(Seasons s : Seasons.values()){
                    if(s.getId() == checkedId){
                        season = s.name();
                    }
                }
                
                ContentValues value = new ContentValues();
                value.put("season", season);
                db.update("settings", value, " id = 1", null);

                params = Seasons.valueOf(season);
                db.close();
            }
        });

        findViewById(R.id.btnEntree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, chooseCook.class);
                intent.putExtra("season", params.name());
                intent.putExtra("cooktype", CookType.starter.name());
                startActivity(intent);
            }
        });

        findViewById(R.id.btnPlat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, chooseCook.class);
                Log.v("[MainActivity]", params.name());
                intent.putExtra("season", params.name());
                intent.putExtra("cooktype", CookType.main_course.name());
                startActivity(intent);
            }
        });

        findViewById(R.id.btnDesserts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, chooseCook.class);
                Log.v("[MainActivity]", params.name());
                intent.putExtra("season", params.name());
                intent.putExtra("cooktype", CookType.dessert.name());
                startActivity(intent);
            }
        });
    }

    public void setParams(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("settings", new String[] {"season"}, "", new String[] {}, null, null, null);
        if(cursor.moveToFirst()){
            this.params = Seasons.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("season")));
            ((RadioButton)findViewById(this.params.getId())).setChecked(true);
        }
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acceuil, menu);
        return true;
    }

}