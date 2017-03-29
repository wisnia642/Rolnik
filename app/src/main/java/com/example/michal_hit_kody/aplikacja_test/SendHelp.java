package com.example.michal_hit_kody.aplikacja_test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SendHelp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText uzytkownik;
    Button wyslij;
    Button cancel;
    TextView TextView6;
    AutoCompleteTextView dane;


    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    String user="";
    String email = "";
    String subject = "";
    String message = "";
    String attechment = "";

    String hash1,user_spr,pass_spr,empty,link1,link2;
    int x=0,Warunek_do_przejścia=0,y=0,polaczenie=0;

    private static final String SAMPLE_DB_NAME = "Baza";
    private static final String SAMPLE_TABLE_NAME = "Karta";

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
            showToast("brak polaczenia z internetem" + e);
            Log.i("aaa",String.valueOf(e));
            polaczenie=0;
            return;
        }

        try {
            connection = DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            showToast("brak polaczenia z internetem" + e);
            Log.i("aaa",String.valueOf(e));
            polaczenie=0;
            return;
        }

    }

    public void Read_Login_MySql() {
        x = 0;
        user = uzytkownik.getText().toString();
        connect();
        if (connection != null) {

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            String sql = ("select * from users where Uzytkownik='"+user+"'");

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
                        email = rs.getString("Email");
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

    protected void sendEmail() {
        String recipients = "wisnia642@gmail.com";
        String subject = "Przypomnienie hasła";
        String body ="Siema nie zapomniałeś o czymś :)";
        String kody = "1234";

        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));

        // prompts email clients only
        email.setType("Przypomnienie hasła do aplikacji wprowadzania danych");
        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, body+" "+kody);
        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendHelp.this, "No email client installed.",
                    Toast.LENGTH_LONG).show();
        }
    }
*/
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }


    private void ToDataBase() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS users (Id VARCHAR,Uzytkownik VARCHAR,Haslo VARCHAR,Email VARCHAR,Stan VARCHAR);");
        } catch (Exception e) {
        }

    }

    private void sendemail() {
        //Getting content for email


        //Creating SendMail object
        SendEmail sm = new SendEmail(this, email, subject, message, attechment,user_spr);

        //Executing sendmail to send email
        sm.execute();
    }

/*
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
*/
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
                        link1 = rs.getString("Link1");
                        link2 = rs.getString("Link2");
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        uzytkownik = (EditText) findViewById(R.id.editText);
        wyslij = (Button) findViewById(R.id.button);
        cancel = (Button) findViewById(R.id.button2);
        TextView6 = (TextView) findViewById(R.id.textView6);
        dane = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        // Read_Login_SqlLigt();

        selectmysqlustawienia();




        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = uzytkownik.getText().toString();
                Read_Login_MySql();
                try {
                    if (TextUtils.isEmpty(uzytkownik.getText().toString()) ) {

                        showToast("Uzupełnij wszystkie pola");
                    } else if (x>0 & polaczenie==1) {

                        Log.i("poczta",""+email);
                        subject = "Email od uzytkownika " + user;
                        message = "Treść emaila: " + dane.getText().toString();

                        //Funkcja do wysyłania emaila
                        // showToast("Wysłano email z hasłem do użytkownika");
                        sendemail();

                        Intent c = new Intent(SendHelp.this, MainActivity.class);
                        startActivity(c);

                    } else {
                        showToast("Brak podanego loginu w bazie danych");
                    }
                }catch (Exception e)

                {
                    Log.i("blad","poczta"+e);
                }
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent c = new Intent(SendHelp.this, MainActivity.class);
                startActivity(c);
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
       // getMenuInflater().inflate(R.menu.send_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

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
                Intent c = new Intent(SendHelp.this, Ustawienia.class);
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


            showToast("Aktualnie uruchomione");



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
