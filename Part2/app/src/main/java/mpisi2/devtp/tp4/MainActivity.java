package mpisi2.devtp.tp4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {



    EditText etNom, etPrenom;
    Button btnSupprimer, btnAjouter, btnPreferences;

    SQLiteDatabase sqlDb;

    ListView lvEtudiants;
    ArrayList listEtudiants;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPreNom);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnPreferences = findViewById(R.id.btnPreferences);
        btnSupprimer = findViewById(R.id.btnSupprimer);

        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterEtudiant();
            }
        });
        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supprimerEtudiants();
            }
        });
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPreferenceActivity();
            }
        });


        sqlDb = openOrCreateDatabase("mydatabase.db", SQLiteDatabase.OPEN_READWRITE, null);

        sqlDb.execSQL("CREATE TABLE IF NOT EXISTS etudiants (" +
                "id INTEGER "+ " PRIMARY KEY AUTOINCREMENT, " +
                "nomEtudiant VARCHAR, " +
                "prenomEtudiant VARCHAR);");

        lvEtudiants = findViewById(R.id.lvEtudiants);
        listEtudiants = new ArrayList();

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listEtudiants);
        lvEtudiants.setAdapter(adapter);

        Cursor resultSet = sqlDb.rawQuery("SELECT * FROM etudiants", null);
        if (resultSet.moveToFirst()) {
            //lire une ligne et verifier s’il y en a d’autres
            do {
                String id = resultSet.getString(0);
                String nomEtudiant = resultSet.getString(1);
                String prenomEtudiant = resultSet.getString(2);
                String text = id + " -" + nomEtudiant + " " + prenomEtudiant;
                System.out.println(text);

                //ajouter les resultats de la requete dans la liste array
                listEtudiants.add(id + " - " + nomEtudiant + " " + prenomEtudiant);

            } while (resultSet.moveToNext());
        }
        resultSet.close();
        Toast.makeText(this, "Lecture dans la base reussie", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();//rafraichir la list view de l'interface

    }

    private void ajouterEtudiant(){

        ContentValues values = new ContentValues();
        values.put("nomEtudiant", etNom.getText().toString());
        values.put("prenomEtudiant", etPrenom.getText().toString());
        long id = sqlDb.insert("etudiants", null, values);
        //sqlDb.insert() retourn -1 au cas d'erreur
        if (id == -1) {
            Toast.makeText(this, "Erreur d'insertion dans la base", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "insertion dans la base reussie", Toast.LENGTH_SHORT).show();

            //ajouter dans la liste array
            listEtudiants.add(id + " - " + etNom.getText().toString() + " " + etPrenom.getText().toString());

            //rafraichir la list view de l'interface
            adapter.notifyDataSetChanged();
        }

    }

    private void supprimerEtudiants(){
        sqlDb.delete("etudiants", "1", null); //req pour supprimer tous les etudiants
        listEtudiants.clear();  //vider la liste array
        adapter.notifyDataSetChanged(); //rafraichir la list view de l'interface
        Toast.makeText(this, "Suprression de tous les etudiants dans la base reussie", Toast.LENGTH_SHORT).show();
    }

    private void startPreferenceActivity(){
        startActivityForResult(new Intent(MainActivity.this, MyPreferenceActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (requestCode == 1){

            String Titre = preferences.getString("title", "no title");

            setTitle("" + Titre);
        }
    }

}