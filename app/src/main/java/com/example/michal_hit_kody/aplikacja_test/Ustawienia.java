package com.example.michal_hit_kody.aplikacja_test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ustawienia extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    String[] user = new String[15];
    String[] pass = new String[15];
    String[] email = new String[15];

    String dane_user,dane_pass,dane_email,dane_pass1,dane_pass2,link1,link2;
    String hash1;

    int x=0,Warunek_do_przejścia=0,polaczenie=0,poprawny_email=0;

    EditText uzytkonik;
    EditText haslo;
    EditText email_txt;
    EditText haslo1;
    EditText haslo2;

    CheckBox zap_haslo;
    CheckBox zap_email;

    Button powrot;
    Button czysc;
    Button zatwierdz;

    private static final String SAMPLE_DB_NAME = "Baza";
    private static final String SAMPLE_TABLE_NAME = "Karta";

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }



    //tworzenie polaczenia z baza danych
    public void connect()
    {
        String url="jdbc:mysql://mysql2.hekko.net.pl/regatzo_app";
        String user="regatzo_admin";
        String pass="Regatzo123";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        polaczenie=1;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //   showToast("brak polaczenia z internetem");
            Log.i("aaa",String.valueOf(e));
            polaczenie=0;
            return;
        }

        try {
            connection = DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            showToast("brak polaczenia z internetem");
            Log.i("aaa",String.valueOf(e));
            polaczenie=0;
            return;
        }

    }

    //tworznie bazy uzytkownikow
    private void ToDataBase() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS users (Id VARCHAR,Uzytkownik VARCHAR,Haslo VARCHAR,Email VARCHAR,Stan VARCHAR);");
            sampleDB.close();
        } catch (Exception e) {
        }

    }

    private void Hash() {
        try {
            String input = haslo.getText().toString();
            hash1 = "%032x440472108104" + String.valueOf(input.hashCode());

        } catch (Exception e) {
        }
    }

    private void Read_Login_SqlLigt() {
        x = 0;
        ToDataBase();

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from users", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(1));
                if (zm != null) {
                    user[x] = String.valueOf(c.getString(1));
                    pass[x] = String.valueOf(c.getString(2));
                    x++;
                }
            }
            sampleDB.close();
        } catch (Exception e) {

        }

    }

    public void Read_Login_MySql() {
        x = 0;
        connect();
        if (connection != null) {

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            String sql = ("select * from users");

            try {
                rs = st.executeQuery(sql);
            } catch (SQLException e1) {
                //  e1.printStackTrace();
                //Log.i("myTag", "2" + e1);
            }
            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String zm = rs.getString("Uzytkownik");
                    if (zm != null) {
                        user[x] = rs.getString("Uzytkownik");
                        pass[x] = rs.getString("Haslo");
                        x++;
                    }

                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "3" + e1);
            }

            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                Log.i("myTag", "4" + se);
                showToast("brak polaczenia z internetem");
            }

        }

    }

    public void UpdateSql1haslo() {
        ToDataBase();
        hashCode();
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("UPDATE users SET Haslo=('"+hash1+"') WHERE Uzytkownik='" + dane_user + "'");
            sampleDB.close();
        } catch (Exception e) {

        }


        connect();
        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


            String sql1 = "UPDATE users SET Haslo = ('"+dane_pass1+"') WHERE Uzytkownik = '" + dane_user + "'";

            try {
                st.executeUpdate(sql1);
            } catch (SQLException e1) {
                // e1.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                showToast("brak połączenia z internetem");
            }
        }
    }

    public void UpdateSql1email() {
        ToDataBase();

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("UPDATE users SET Email=('"+dane_email+"') WHERE Uzytkownik='" + dane_user + "'");
            sampleDB.close();
        } catch (Exception e) {

        }


        connect();
        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


            String sql1 = "UPDATE users SET Email = ('"+dane_email+"') WHERE Uzytkownik = '" + dane_user + "'";

            try {
                st.executeUpdate(sql1);
            } catch (SQLException e1) {
                // e1.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                showToast("brak połączenia z internetem");
            }
        }
    }

    private void ustawienia_SqlLigt() {
        x = 0;


        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from ustawienia", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(0));
                if (zm != null) {
                    link1 = String.valueOf(c.getString(1));
                    link2 = String.valueOf(c.getString(2));
                    //   showToast(link_glowny);
                    x++;
                }
            }
            sampleDB.close();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zatwierdz = (Button) findViewById(R.id.button11);
        czysc = (Button) findViewById(R.id.button) ;
        powrot = (Button) findViewById(R.id.button2);

        uzytkonik = (EditText) findViewById(R.id.editText);
        haslo = (EditText) findViewById(R.id.editText3);
        email_txt = (EditText) findViewById(R.id.editText4);
        haslo1 = (EditText) findViewById(R.id.editText5);
        haslo2 = (EditText) findViewById(R.id.editText2);

        zap_haslo = (CheckBox) findViewById(R.id.checkBox3);
        zap_email = (CheckBox) findViewById(R.id.checkBox2);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ustawienia_SqlLigt();

        connect();
        if(polaczenie==1) {
            Read_Login_MySql();
        }

        powrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(Ustawienia.this, MainActivity.class);
                startActivity(c);
            }
        });


        czysc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uzytkonik.setText("");
                haslo.setText("");
                email_txt.setText("");
                haslo1.setText("");
                haslo2.setText("");
            }
        });

        zatwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
                if (polaczenie == 1) {
                    try {
                        dane_user= uzytkonik.getText().toString();
                        dane_pass = haslo.getText().toString();
                        dane_email = email_txt.getText().toString();
                        dane_pass1 = haslo1.getText().toString();
                        dane_pass2 = haslo2.getText().toString();

                        for (int i = 0; i < user.length; i = i + 0) {
                            if (dane_user.equals(user[i])) {
                                if (dane_pass.equals(pass[i])) {

                                    //sprawdzanie czy hasła są takie same update
                                    if(dane_pass1.equals(dane_pass2)){
                                    if(zap_haslo.isChecked())
                                    {
                                        UpdateSql1haslo();
                                        showToast("Hasło zostało zmienione");
                                    }
                                    }else
                                    {
                                        showToast("Hasła nie są takie same");
                                    }

                                    if(dane_email!=null){

                                        //sprawdza czy email jest poprawny i update
                                    if(zap_email.isChecked())
                                    {
                                        UpdateSql1email();
                                        showToast("Email został zmieniony");
                                    }
                                    }else
                                    {
                                        showToast("Podany email jest nie poprawny");
                                    }

                                    //  showToast("Hasło zostało zmienione");
                                    //nie wiem dlaczego to wstawiłem ale ok :p
                                    Warunek_do_przejścia = 1;

                                    Intent c = new Intent(Ustawienia.this, MainActivity.class);
                                    startActivity(c);

                                }
                            }
                            i++;
                        }

                        if (TextUtils.isEmpty(uzytkonik.getText().toString()) & TextUtils.isEmpty(haslo.getText().toString())) {

                            showToast("Uzupełnij wszystkie pola");
                        } else if (Warunek_do_przejścia == 0) {
                            showToast("Błędny login lub hasło");
                        }
                    } catch (Exception e) {
                        showToast("błąd wczytywania");
                    }
                } else

                {
                    showToast("Brak połączenia z internetem");
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.ustawienia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.app_bar_search) {
            // wylogowanie uzytkownika

            showToast("Jesteś wylogowany");

        } else if (id == R.id.nav_gallery) {
            // podglad dodanych rekordow
            showToast("musisz być zalogowany aby wykonać tą opcję");


        } else if (id == R.id.nav_slideshow) {
            // edytowanie hasla lub emaila
            showToast("Aktualnie uruchomione ");

        } else if (id == R.id.nav_manage) {
            //zapis do PDF
            showToast("musisz być zalogowany aby wykonać tą opcję");

        } else if (id == R.id.nev_manage1) {
            //zapis do EXCEL
            showToast("musisz być zalogowany aby wykonać tą opcję");

        } else if (id == R.id.nav_help) {

            //napisanie emaila do pomocy
            connect();
            if(polaczenie == 1) {
                Intent c = new Intent(Ustawienia.this, SendHelp.class);
                startActivity(c);
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do ustawien ");
            }


        } else if (id == R.id.nav_share) {

            connect();
            if(polaczenie == 1) {
                //pierwszy link
                Uri uri = Uri.parse(link1); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do strony ");
            }

        } else if (id == R.id.nav_send) {

            connect();
            if(polaczenie == 1) {
                //drugi link
                Uri uri = Uri.parse(link2); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do strony ");
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
