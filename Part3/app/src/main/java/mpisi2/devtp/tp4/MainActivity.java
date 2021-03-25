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

    View viewLogin, viewMain;
    EditText etLogin, etPassword;
    Button btnAuthOk;

    EditText etNom, etPrenom;
    Button btnSupprimer, btnAjouter, btnPreferences;

    SQLiteDatabase sqlDb;

    ListView lvEtudiants;
    ArrayList listEtudiants;
    ArrayAdapter adapter;

    private void setContentView(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useLoggin = preferences.getBoolean("useLoginPassword", false);

        if(useLoggin == true){
            setContentView(viewLogin);
            etLogin = findViewById(R.id.etLogin);
            etPassword = findViewById(R.id.etPassword);
            btnAuthOk = findViewById(R.id.btnAuthOk);

            btnAuthOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifierLoginPassword();
                }
            });
        }
        else {
            setContentView(viewMain);

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

    }
    private void verifierLoginPassword() {

        String loginPreference, passwordPreference;

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        loginPreference = preferences.getString("login", "");
        passwordPreference = preferences.getString("password", "");

        //jutse pour supprimer les espances de debut et de fin et toute la chaine en minuscule
        loginPreference = loginPreference.trim().toLowerCase();
        passwordPreference = passwordPreference.trim();

        if(loginPreference.equals(etLogin.getText().toString().trim().toLowerCase())
                &&
           passwordPreference.equals(etPassword.getText().toString().trim())){
            // alors le login et le mot de passe sont correcte avec celle des preferences
            setContentView(viewMain);

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
        else {
            //login erreur ou mot de passe erreur
            Toast.makeText(this, "Erreur: login ou password n'est pas correct ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewLogin = getLayoutInflater().inflate(R.layout.activity_main_auth, null);
        viewMain = getLayoutInflater().inflate(R.layout.activity_main, null);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String Titre = preferences.getString("title", "no title");
        setTitle("" + Titre);

        setContentView();

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