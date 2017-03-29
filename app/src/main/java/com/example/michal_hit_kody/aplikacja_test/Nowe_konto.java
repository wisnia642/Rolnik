package com.example.michal_hit_kody.aplikacja_test;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.michal_hit_kody.aplikacja_test.R.id.textView;

public class Nowe_konto extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button potwierdz;
    Button powrot;
    Button czysc;
    EditText login;
    EditText email;
    EditText haslo1;
    EditText haslo2;
    TextView napis_haslo;
    TextView napis_haslo1;
    TextView napis_email;
    TextView napis_user;

    String[] user = new String[15];
    String[] pass = new String[15];
    String[] id = new String[15];
    String[] stan = new String[15];


    String hash1,user_spr,pass_spr;
    String zm,zm1,zm2,zm3,zm4;
    String link1,link2,link_glowny,email1,email2,email_pass;
    int x=0,Warunek_do_przejścia=1,y=0,czy_ten_sam=0,poprawny_email=0,polaczenie=0;

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

    private void ToDataBase() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS users (Id VARCHAR,Uzytkownik VARCHAR,Haslo VARCHAR,Email VARCHAR,Stan VARCHAR);");
        } catch (Exception e) {
        }

    }

    //tworzenie tabeli ustawienia
    private void ToDataBase3() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS ustawienia (Link_glowny VARCHAR, VARCHAR,Link1 VARCHAR,Link2 VARCHAR,Email1 VARCHAR,Email2 Varchar,Email_pass VARCHAR)");
            sampleDB.close();
        } catch (Exception e) {
        }

    }

    public void selectmysqlustawienia() {
        x = 0;
        connect();
        if (connection != null) {

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            String sql = ("select * from ustawienia");

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
                    String zm = rs.getString("Link_glowny");
                    if (zm != null) {
                        link_glowny= rs.getString("Link_glowny");
                        link1 = rs.getString("Link1");
                        link2 = rs.getString("Link2");
                        email1 = rs.getString("Email1");
                        email2 = rs.getString("Email2");
                        email_pass = rs.getString("Email_pass");
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

    /*
    //tworzenie tabeli z danymi uzytkownika
    private void CreateTableSQLight() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS '"+zm+"' (Id,Data,Dane1,Dane2,Dane3,Dane4,Dane5,Dane6,Dane7,Dane8);");
            sampleDB.close();
        } catch (Exception e) {
        }

    }*/

    public void zapamietanie_ustawienia()
    {
        ToDataBase3();
        selectmysqlustawienia();
        try {
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB1.execSQL("Delete from ustawienia");
            sampleDB1.execSQL("INSERT INTO ustawienia (Link_glowny,Link1,Link2,Email1,Email2,Email_pass) VALUES ('"+link_glowny+"','"+link1+"','"+link2+"','"+email1+"','"+email2+"','"+email_pass+"')");
            sampleDB1.close();
        } catch (Exception f)
        {}

    }

    public void InsertLoginDataSqligt() {
        ToDataBase();
        Hash();
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("INSERT INTO users (Id,Uzytkownik,Haslo,Email,Stan) VALUES ('"+String.valueOf(x)+"','"+zm+"','"+hash1+"','"+zm1+"','"+zm4+"') ");
            sampleDB.close();
        } catch (Exception e) {
           // showToast(""+e);
        }
    }

    private void Hash() {
        try {
            String input = haslo1.getText().toString();
            hash1 = "%032x440472108104" + String.valueOf(input.hashCode());

        } catch (Exception e) {
        }
    }



    public void InsertLoginDataMysql() {
        connect();

        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


            String sql1 = "INSERT INTO users (Id,Uzytkownik,Haslo,Email,Stan) VALUES" +
                    " (?,?,?,?,?)";

            try {
                ps = connection.prepareStatement(sql1);
                ps.setString(1, String.valueOf(x));
                ps.setString(2, zm);
                ps.setString(3, zm2);
                ps.setString(4, zm1);
                ps.setString(5, zm4);
                ps.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                //  showToast("brak połączenia z internetem" +e);
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                //  showToast("brak połączenia z internetem" +se);
            }
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

                        id[x] = rs.getString("Id");
                        user[x] = rs.getString("Uzytkownik");
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
              //  showToast("brak polaczenia z internetem");
            }

        }

    }

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowe_konto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        potwierdz = (Button) findViewById(R.id.button11);
        powrot = (Button) findViewById(R.id.button2);
        czysc = (Button) findViewById(R.id.button);

        login = (EditText) findViewById(R.id.editText);
        email = (EditText) findViewById(R.id.editText3);
        haslo1 = (EditText) findViewById(R.id.editText4);
        haslo2 = (EditText) findViewById(R.id.editText2);


        Read_Login_MySql();

        czysc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setText("");
                email.setText("");
                haslo1.setText("");
                haslo2.setText("");
            }
        });

        powrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent c = new Intent(Nowe_konto.this, MainActivity.class);
                startActivity(c);
            }
        });



        potwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zm = login.getText().toString();
                zm1 = email. getText().toString();
                zm2 = haslo1.getText().toString();
                zm3 = haslo2.getText().toString();
                zm4 = "0";

                //Log.i("aaa","wartosc  "+String.valueOf(user.length));

                //sprawdzam czy dany użytkownik już istnieje w sytstemie

                y=0;
                czy_ten_sam=0;
                for (int i = 0; i <  user.length; i=i+0) {

                    if (!zm.equals(user[i])) {
                        y++;
                    }

                    i++;
                }
                if(y== user.length)
                {
                    Log.i("aaa","wartosc y  "+y);
                    Log.i("aaa","wartosc i  "+czy_ten_sam);
                    czy_ten_sam=1;
                }



                // tworzenie nowego uzytkownika

                if(Warunek_do_przejścia==1) {

                        if(czy_ten_sam == 1) {

                            if (zm1!=null) {

                                if (zm2.equals(zm3)) {

                                    //insert tabela user mysql
                                    InsertLoginDataMysql();

                                    //insert tabela user sqlight
                                    InsertLoginDataSqligt();

                                    //update tabela ustawienia
                                    zapamietanie_ustawienia();
                                    showToast("Użytkownik został dodany");

                                    Intent c = new Intent(Nowe_konto.this, MainActivity.class);
                                    startActivity(c);

                                } else {

                                    showToast("hasła nie są takie same");
                                }
                            } else
                            {
                              showToast("Podaj adres email");
                            }

                        }else {

                            showToast("dana nazwa użytkownika jest już zajęta");
                        }
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
      //  getMenuInflater().inflate(R.menu.nowe_konto, menu);
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

            showToast("Jesteś wylogowany");

        } else if (id == R.id.nav_gallery) {
            // podglad dodanych rekordow
            showToast("musisz być zalogowany aby wykonać tą opcję");


        } else if (id == R.id.nav_slideshow) {
            // edytowanie hasla lub emaila
            connect();
            if(polaczenie == 1) {
                Intent c = new Intent(Nowe_konto.this, Ustawienia.class);
                startActivity(c);
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do ustawien ");
            }


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
                Intent c = new Intent(Nowe_konto.this, SendHelp.class);
                startActivity(c);
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do napisz do nas ");
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
