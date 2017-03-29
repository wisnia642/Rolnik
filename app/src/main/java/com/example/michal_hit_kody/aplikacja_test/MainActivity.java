package com.example.michal_hit_kody.aplikacja_test;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVWriter;

import static android.R.id.progress;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    Button potwierdz;
    Button wyczysc;
    Button strona;
    CheckBox znacznik;
    CheckBox aktualizacja;
    EditText login;
    EditText haslo;
    TextView konto;
    TextView przypomnienie;
    private AdView mAdView;

    private ProgressDialog progressDialog;

    String user, pass, stan, email;

    String dane1[] = new String[200];
    String dane2[] = new String[200];
    String dane3[] = new String[200];
    String dane4[] = new String[200];
    String dane5[] = new String[200];
    String dane6[] = new String[200];
    String dane7[] = new String[200];
    String dane8[] = new String[200];
    String dane9[] = new String[200];
    String dane10[] = new String[200];
    String dane11[] = new String[200];
    String dane12[] = new String[200];
    String dane13[] = new String[200];

    String sdane1[] = new String[200];
    String sdane2[] = new String[200];
    String sdane3[] = new String[200];
    String sdane4[] = new String[200];
    String sdane5[] = new String[200];
    String sdane6[] = new String[200];
    String sdane7[] = new String[200];
    String sdane8[] = new String[200];
    String sdane9[] = new String[200];
    String sdane10[] = new String[200];
    String sdane11[] = new String[200];
    String sdane12[] = new String[200];
    String sdane13[] = new String[200];


    String dane_user, dane_pass, dane_status;
    String hash1, user_spr, pass_spr;
    String link1, link2, link_glowny, email1, email2, email_pass, email_sql;
    int x = 0, y = 0, z = 0, m = 0, Warunek_do_przejścia = 0, polaczenie = 0;
    String smtp_host, socetfactory_port, socetfactory_class, smtp_auth, smtp_port;

    private static final String SAMPLE_DB_NAME = "Baza";
    private static final String SAMPLE_TABLE_NAME = "Karta";
    private ProgressBar spinner;


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    private void ToDataBaseSqllight() {

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS '" + user_spr + "' (Id VARCHAR,Kiedy VARCHAR,Gatunek VARCHAR,Odmiana VARCHAR,Data_Godzina VARCHAR,Powierzchnia VARCHAR,Nr_Działki VARCHAR," +
                    "Preparat VARCHAR,Dawka VARCHAR,Substancja_czynna VARCHAR,Temperatura VARCHAR,Faza_rozwoju VARCHAR,Przyczyny_zabiegu VARCHAR,Uwagi VARCHAR);");
        } catch (Exception e) {
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void TodataBaseMysql() {

        connect();
        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();

            }
            String sql = "CREATE TABLE IF NOT EXISTS " + user_spr + "(`ID` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Kiedy` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Gatunek` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Odmiana` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Data_Godzina` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Powierzchnia` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Nr_Działki` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Preparat` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Dawka` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Substancja_czynna` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Temperatura` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Faza_rozwoju` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Przyczyny_zabiegu` VARCHAR(300) NULL DEFAULT NULL," +
                    "`Uwagi` VARCHAR(300) NULL DEFAULT NULL)";

            try {
                st.executeUpdate(sql);
            } catch (SQLException e1) {
                showToast("" + e1);
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                showToast("brak połączenia z internetem");
            }
        }
    }

    //tworzenie polaczenia z baza danych
    public void connect() {
        String url = "jdbc:mysql://mysql2.hekko.net.pl/regatzo_app";
        String user = "regatzo_admin";
        String pass = "Regatzo123";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        polaczenie = 1;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //   showToast("brak polaczenia z internetem");
            Log.i("aaa", String.valueOf(e));
            polaczenie = 0;
            return;
        }

        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            showToast("brak polaczenia z internetem");
            Log.i("aaa", String.valueOf(e));
            polaczenie = 0;
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

    //tworzenie bazy edytowany uzytkownik
    private void ToDataBase1() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS edit (Id VARCHAR,Gatunek VARCHAR,Odmiana VARCHAR,Data_Godzina VARCHAR,Powierzchnia VARCHAR,Nr_Działki VARCHAR," +
                    "Preparat VARCHAR,Dawka VARCHAR,Substancja_czynna VARCHAR,Temperatura VARCHAR,Faza_rozwoju VARCHAR,Przyczyny_zabiegu VARCHAR,Uwagi VARCHAR, Uzytkownik VARCHAR);");
            sampleDB.close();
        } catch (Exception e) {
        }

    }

    //tworzenie tabeli pamiec
    private void ToDataBase2() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS pamiec (user VARCHAR,pass VARCHAR,status VARCHAR)");
            sampleDB.close();
        } catch (Exception e) {
        }

    }

    //tworzenie tabeli ustawienia
    private void ToDataBase3() {
        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS ustawienia (Link_glowny VARCHAR,Link1 VARCHAR,Link2 VARCHAR,Email1 VARCHAR,Email2 Varchar,Email_pass VARCHAR," +
                    "Smtp_host VARCHAR, SocketFactory_port VARCHAR, SocketFactory_class VARCHAR, Smtp_auth VARCHAR, Smtp_port VARCHAR)");
            sampleDB.close();
        } catch (Exception e) {
            Log.i("bbb", "dupa" + e);
        }

    }

    public void zapamietanie_ustawienia() {
        ToDataBase3();
        selectmysqlustawienia();
        try {
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB1.execSQL("Delete from ustawienia");
            sampleDB1.execSQL("INSERT INTO ustawienia (Link_glowny,Link1,Link2,Email1,Email2,Email_pass,Smtp_host,SocketFactory_port,SocketFactory_class,Smtp_auth,Smtp_port)" +
                    " VALUES ('" + link_glowny + "','" + link1 + "','" + link2 + "','" + email1 + "','" + email2 + "','" + email_pass + "','" + smtp_host + "','" + socetfactory_port + "'," +
                    "'" + socetfactory_class + "','" + smtp_auth + "','" + smtp_port + "')");
            sampleDB1.close();
        } catch (Exception f) {
        }

    }

    public void zapamietanie_hasla() {
        ToDataBase2();
        try {
            user_spr = login.getText().toString();
            pass_spr = haslo.getText().toString();
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB1.execSQL("Delete from pamiec");
            sampleDB1.execSQL("INSERT INTO pamiec (user,pass,status) VALUES ('" + user_spr + "','" + pass_spr + "','1')");
            sampleDB1.close();
        } catch (Exception f) {
            Log.i("siema", "" + f);
        }

    }

    public void nie_pamietanie_hasla() {
        ToDataBase2();
        try {
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB1.execSQL("Delete from pamiec");
            sampleDB1.close();
        } catch (Exception f) {
        }
    }

    public void InsertLight() {
        ToDataBase1();
        try {
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            user_spr = login.getText().toString();
            sampleDB1.execSQL("Delete from edit");
            sampleDB1.execSQL("INSERT INTO edit (Id,Uzytkownik) SELECT * FROM (SELECT '1','" + user_spr + "') AS tmp WHERE NOT EXISTS (SELECT Id FROM edit WHERE Id = '1') LIMIT 1");
            sampleDB1.close();
        } catch (Exception f) {
        }

    }

    public void cleanstatususer_sqllight() {
        try {
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            user_spr = login.getText().toString();
            sampleDB1.execSQL("UPDATE users SET Stan=('0')");
            sampleDB1.close();
        } catch (Exception f) {
        }

    }

    private void Hash() {
        try {
            String input = haslo.getText().toString();
            hash1 = "%032x440472108104" + String.valueOf(input.hashCode());

        } catch (Exception e) {
        }
    }

    //statyczne dodawanie użytkownika
/*

    public void InsertLoginDataSqligt() {
        ToDataBase();

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null); //%032x44047210810492668751
            sampleDB.execSQL("INSERT INTO users (Id,Uzytkownik,Haslo,Email,Stan) VALUES ('0','admin','%032x44047210810492668751','','0') ");
            sampleDB.close();
        } catch (Exception e) {
            showToast(""+e);
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
                ps.setString(1, "1");
                ps.setString(2, "admin");
                ps.setString(3, "admin");
                ps.setString(4, "wisnia642@gmail.com");
                ps.setString(5, "1");
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
*/

    public void UpdateSqlLight() {
        ToDataBase();

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("UPDATE users SET Stan=('1') WHERE Uzytkownik='" + user_spr + "'");
            sampleDB.close();
        } catch (Exception e) {

        }
    }

    public void UpdateMySql(){

        connect();
        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


            String sql1 = "UPDATE users SET Stan = ('1') WHERE Uzytkownik = '" + user_spr + "'";

            try {
                st.executeUpdate(sql1);
            } catch (SQLException e1) {
                // e1.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                // showToast("brak połączenia z internetem");
            }
        }
    }

    private void Read_Login_SqlLigt() {
        x = 0;
        ToDataBase();

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        Hash();
        try {
            Cursor c = sampleDB.rawQuery("select * from users where Uzytkownik='"+user_spr+"' and Haslo='"+hash1+"'", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(1));
                if (zm != null) {
                    user = String.valueOf(c.getString(1));
                    Log.i("blad",""+user);
                    pass = String.valueOf(c.getString(2));
                    email = String.valueOf(c.getString(3));
                    y++;
                }
            }
            sampleDB.close();
        } catch (Exception e) {

        }

    }

    private void checkbox() {
        x = 0;
        ToDataBase2();

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from pamiec", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(0));
                Log.i("bbb", "masakra_całkowita" + dane_status);
                if (zm != null) {
                    dane_user = String.valueOf(c.getString(0));
                    dane_pass = String.valueOf(c.getString(1));
                    dane_status = String.valueOf(c.getString(2));
                    Log.i("bbb", "masakra" + dane_status);
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

            user_spr = login.getText().toString();
            pass_spr = haslo.getText().toString();
            Log.i("baza",""+user_spr+"  "+pass_spr);
            String sql = ("select * from users where Uzytkownik='"+user_spr+"' and Haslo='"+pass_spr+"' ");

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
                    Log.i("baza",""+zm);
                    if (zm != null) {
                        user = rs.getString("Uzytkownik");
                        pass = rs.getString("Haslo");
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
                //   showToast("brak polaczenia z internetem");
            }

        }

    }

    public void UpdateSql1wyloguj() {
        ToDataBase();

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("UPDATE users SET Stan=('0') WHERE Uzytkownik='" + user_spr + "'");
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


            String sql1 = "UPDATE users SET Stan = ('0') WHERE Uzytkownik = '" + user_spr + "'";

            try {
                st.executeUpdate(sql1);
            } catch (SQLException e1) {
                // e1.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                //   showToast("brak połączenia z internetem");
            }
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
                        link_glowny = rs.getString("Link_glowny");
                        link1 = rs.getString("Link1");
                        link2 = rs.getString("Link2");
                        email1 = rs.getString("Email1");
                        email2 = rs.getString("Email2");
                        email_pass = rs.getString("Email_pass");
                        smtp_host = rs.getString("Smtp.host");
                        socetfactory_port = rs.getString("SocketFactory.port");
                        socetfactory_class = rs.getString("SocketFactory.class");
                        smtp_auth = rs.getString("Smtp.auth");
                        smtp_port = rs.getString("Smtp.port");
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

    //aktualizowanie bazy danych sqlight
    public void insertuser() {
        //ToDataBase1();
        y = y + 1;
        Hash();
        try {
            SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            // Haslo VARCHAR,Email VARCHAR,Stan VARCHAR
            sampleDB1.execSQL("INSERT INTO users (Id,Uzytkownik,Haslo,Email,Stan) SELECT * FROM (SELECT '" + y + "','" + user_spr + "','" + hash1 + "','" + email_sql + "','1')" +
                    " AS tmp WHERE NOT EXISTS (SELECT Uzytkownik FROM users WHERE Uzytkownik = '" + user_spr + "')");
            sampleDB1.close();
        } catch (Exception f) {
            //    showToast(""+f);
        }
    }

    public void aktualizacja_danych() {
        pobieranie_danych_mysql();
        pobieranie_danych_sqlight();

        if (z < m) { //z jest mniejsze od m
            try {

                SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

                sampleDB1.execSQL("delete from '" + user_spr + "'");

                //   INSERT INTO users (Id,Uzytkownik,Haslo,Email,Stan) SELECT * FROM (SELECT '"+y+"','"+user_spr+"','"+hash1+"','"+email_sql+"','1')" +
                //  " AS tmp WHERE NOT EXISTS (SELECT Uzytkownik FROM users WHERE Uzytkownik = '"+user_spr+"')

                sampleDB1.close();


                for (int j = 0; j < m; j = j + 0) {

                    // z++;
                    Log.i("blad", "" + sdane1[j]);

                    SQLiteDatabase sampleDB2 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

                    sampleDB2.execSQL("INSERT INTO '" + user_spr + "' (Id,Kiedy,Gatunek,Odmiana," +
                            "Data_Godzina,Powierzchnia,Nr_Działki,Preparat,Dawka,Substancja_czynna," +
                            "Temperatura,Faza_rozwoju,Przyczyny_zabiegu,Uwagi)" +
                            " VALUES ('" + String.valueOf(j + 1) + "','" + sdane1[j] + "'," +
                            "'" + sdane2[j] + "','" + sdane3[j] + "','" + sdane4[j] + "','" + sdane5[j] + "'," +
                            "'" + sdane6[j] + "','" + sdane7[j] + "','" + sdane8[j] + "','" + sdane9[j] + "'," +
                            "'" + sdane10[j] + "','" + sdane11[j] + "','" + sdane12[j] + "','" + sdane13[j] + "')");

                    //   INSERT INTO users (Id,Uzytkownik,Haslo,Email,Stan) SELECT * FROM (SELECT '"+y+"','"+user_spr+"','"+hash1+"','"+email_sql+"','1')" +
                    //  " AS tmp WHERE NOT EXISTS (SELECT Uzytkownik FROM users WHERE Uzytkownik = '"+user_spr+"')

                    sampleDB2.close();

                    j++;
                }


            } catch (Exception k) {
                showToast("blad" + k);
            }
        }

        if (z > m & polaczenie == 1) {

            connect();

            if (connection != null) {
                try {
                    st = connection.createStatement();
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                }


                String sql1 = "delete from " + user_spr + "";

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

            connect();
            if (connection != null) {
                try {
                    st = connection.createStatement();
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                }

                for (int j = 0; j < z; j = j + 0) {
                    Log.i("blad", "" + j);


                    String sql1 = "INSERT INTO " + user_spr + " " +
                            "(ID,Kiedy,Gatunek,Odmiana,Data_Godzina,Powierzchnia,Nr_Działki,Preparat,Dawka,Substancja_czynna,Temperatura,Faza_rozwoju,Przyczyny_zabiegu,Uwagi)" +
                            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    try {
                        ps = connection.prepareStatement(sql1);
                        ps.setString(1, String.valueOf(j + 1));
                        ps.setString(2, dane1[j]);
                        ps.setString(3, dane2[j]);
                        ps.setString(4, dane3[j]);
                        ps.setString(5, dane4[j]);
                        ps.setString(6, dane5[j]);
                        ps.setString(7, dane6[j]);
                        ps.setString(8, dane7[j]);
                        ps.setString(9, dane8[j]);
                        ps.setString(10, dane9[j]);
                        ps.setString(11, dane10[j]);
                        ps.setString(12, dane11[j]);
                        ps.setString(13, dane12[j]);
                        ps.setString(14, dane13[j]);

                        ps.executeUpdate();


                    } catch (SQLException e) {
                        // showToast("brak połączenia z internetem" + e);
                    }
                    j++;
                }
                try {
                    if (connection != null)
                        connection.commit();
                    connection.close();
                } catch (SQLException se) {
                    //     showToast("brak połączenia z internetem" +se);
                }
            }

        }
    }


    private void pobieranie_danych_sqlight() {
        z = 0;

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from '" + user_spr + "'", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(0));
                if (zm != null) {
                    dane1[z] = String.valueOf(c.getString(1));
                    dane2[z] = String.valueOf(c.getString(2));
                    dane3[z] = String.valueOf(c.getString(3));
                    dane4[z] = String.valueOf(c.getString(4));
                    dane5[z] = String.valueOf(c.getString(5));
                    dane6[z] = String.valueOf(c.getString(6));
                    dane7[z] = String.valueOf(c.getString(7));
                    dane8[z] = String.valueOf(c.getString(8));
                    dane9[z] = String.valueOf(c.getString(9));
                    dane10[z] = String.valueOf(c.getString(10));
                    dane11[z] = String.valueOf(c.getString(11));
                    dane12[z] = String.valueOf(c.getString(12));
                    dane13[z] = String.valueOf(c.getString(13));
                    z++;
                }
            }
            sampleDB.close();
        } catch (Exception e) {

        }

    }

    public void pobieranie_danych_mysql() {
        m = 0;
        connect();
        if (connection != null) {

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            String sql = ("select * from " + user_spr + "");

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
                    String zm = rs.getString("Id");
                    if (zm != null) {
                        sdane1[m] = rs.getString("Kiedy");
                        sdane2[m] = rs.getString("Gatunek");
                        sdane3[m] = rs.getString("Odmiana");
                        sdane4[m] = rs.getString("Data_Godzina");
                        sdane5[m] = rs.getString("Powierzchnia");
                        sdane6[m] = rs.getString("Nr_Działki");
                        sdane7[m] = rs.getString("Preparat");
                        sdane8[m] = rs.getString("Dawka");
                        sdane9[m] = rs.getString("Substancja_czynna");
                        sdane10[m] = rs.getString("Temperatura");
                        sdane11[m] = rs.getString("Faza_rozwoju");
                        sdane12[m] = rs.getString("Przyczyny_zabiegu");
                        sdane13[m] = rs.getString("Uwagi");
                        m++;
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
                //   showToast("brak polaczenia z internetem");
            }

        }

    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Włączanie reklam
        //mAdView = (AdView) findViewById(R.id.reklama);
        // AdRequest adRequest = new AdRequest.Builder()
        //          .build();
        //  mAdView.loadAd(adRequest);

        //sprawdzanie czy uzytkownik zapisuje haslo

        // Here, thisActivity is the current activity

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        potwierdz = (Button) findViewById(R.id.button);
        wyczysc = (Button) findViewById(R.id.button2);

        login = (EditText) findViewById(R.id.editText);
        haslo = (EditText) findViewById(R.id.editText2);

        konto = (TextView) findViewById(R.id.textView2);
        przypomnienie = (TextView) findViewById(R.id.textView4);

        znacznik = (CheckBox) findViewById(R.id.checkBox);
        aktualizacja = (CheckBox) findViewById(R.id.checkBox4);

        //    spinner = (ProgressBar)findViewById(R.id.progressBar1);
/*
        if ( !isNetworkAvailable() ) { // loading offline
            showToast("nie");
            Log.i("blad","tak");
        }else
        {
            showToast("tak");
            Log.i("blad","nie");
        }

      */

        verifyStoragePermissions(MainActivity.this);


        //sprawdzanie połączenia do bazy danych
        connect();
        //  InsertLoginDataMysql();

        if (polaczenie == 1) {

            zapamietanie_ustawienia();
        }

        //odczytywanie czy uzytkownik zapamietal haslo czy nie
        checkbox();

        if (dane_status != null) {
            Log.i("bbb", "dupa" + dane_status);
            znacznik.setChecked(true);
            login.setText(dane_user);
            haslo.setText(dane_pass);
        } else {
            Log.i("bbb", "dupa1");
            nie_pamietanie_hasla();
        }


        potwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connect();
                user_spr = login.getText().toString();
                pass_spr = haslo.getText().toString();
                if (polaczenie == 0) {
                   // showToast("polaczenie");
                    Read_Login_SqlLigt();
                    try {
                        Hash();
                        if (hash1.equals(pass)) {

                        //sprawdza czy uzytkonik uzupelnil wszystkie pola
                        Warunek_do_przejścia = 1;

                        //tworzenie tabeli uzytkownik
                        ToDataBaseSqllight();

                        //wylogowywanie wszystkich urzytkownikow
                        cleanstatususer_sqllight();

                        //tworzenie i czyszczenie tabeli edit
                        InsertLight();

                        if (znacznik.isChecked()) {
                            // showToast("hasło zapamiętane");
                            zapamietanie_hasla();
                            Log.i("siema", "jest");
                        } else  {
                            nie_pamietanie_hasla();
                        }
                        if (aktualizacja.isChecked()) {
                            aktualizacja_danych();
                        }

                        //ustawia status=1 w tabeli uzytkownik na dwoch bazach
                        UpdateSqlLight();
                        Intent c = new Intent(MainActivity.this, Glowne_menu.class);
                        startActivity(c);


                        }

                        if (TextUtils.isEmpty(login.getText().toString()) & TextUtils.isEmpty(haslo.getText().toString())) {

                            showToast("Uzupełnij wszystkie pola");
                        } else if (Warunek_do_przejścia == 0) {
                            showToast("Błędny login lub hasło");
                        }
                    } catch (Exception e) {
                        showToast("błąd wczytywania");
                    }
                    //   spinner.setVisibility(View.INVISIBLE);
                }

                if (polaczenie == 1) {
                    Read_Login_MySql();
                    try {

                        if (pass_spr.equals(pass)) {

                            Warunek_do_przejścia = 1;

                            email_sql = email;

                            //tworzenie tabeli uzytkownik
                            TodataBaseMysql();
                            ToDataBaseSqllight();

                            //wylogowywanie wszystkich urzytkowników
                            cleanstatususer_sqllight();

                            //tworzenie i czyszczenie tabeli edit
                            InsertLight();

                            if (znacznik.isChecked()) {
                                // showToast("hasło zapamiętane");
                                zapamietanie_hasla();
                                Log.i("siema", "jest");
                            } else {
                                nie_pamietanie_hasla();
                            }

                            if (aktualizacja.isChecked()) {
                                aktualizacja_danych();

                            }

                            //sprawdza czy uzytkownik został poprawnie dodany
                            insertuser();
                            //ustawia status=1 w tabeli uzytkownik na dwoch bazach
                            UpdateMySql();
                            UpdateSqlLight();
                            // showToast("dupa");
                            //   spinner.setVisibility(View.INVISIBLE);
                            Intent c = new Intent(MainActivity.this, Glowne_menu.class);
                            startActivity(c);

                        }

                        if (TextUtils.isEmpty(login.getText().toString()) & TextUtils.isEmpty(haslo.getText().toString())) {

                            showToast("Uzupełnij wszystkie pola");
                        } else if (Warunek_do_przejścia == 0) {
                            showToast("Błędny login lub hasło");
                        }
                    } catch (Exception e) {
                        showToast("błąd wczytywania");
                    }
                    //  spinner.setVisibility(View.INVISIBLE);

                }
            }

        });

        wyczysc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  login.setText("");
                  haslo.setText("");
            }

        });


        konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
                if (polaczenie == 1) {
                    Intent c = new Intent(MainActivity.this, Przyp_pass.class);
                    startActivity(c);
                } else {
                    showToast("Musisz mieć podłączenie do internetu, aby wykonać tą operację");
                }
            }
        });

        przypomnienie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
                if (polaczenie == 1) {
                    Intent c = new Intent(MainActivity.this, Nowe_konto.class);
                    startActivity(c);
                } else {
                    showToast("Musisz mieć podłączenie do internetu, aby wykonać tą operację ");
                }


            }
        });
    }
/*

        @Override
        public void onPause () {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        @Override
        public void onResume () {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        @Override
        public void onDestroy () {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }
*/

        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            //  getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
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
                connect();
                if (polaczenie == 1) {
                    Intent c = new Intent(MainActivity.this, Ustawienia.class);
                    startActivity(c);
                } else {
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
                if (polaczenie == 1) {
                    Intent c = new Intent(MainActivity.this, SendHelp.class);
                    startActivity(c);
                } else {
                    showToast("Musisz mieć podłączenie do internetu, aby przejsc do napisz do nas ");
                }


            } else if (id == R.id.nav_share) {

                connect();
                if (polaczenie == 1) {
                    //pierwszy link
                    Uri uri = Uri.parse(link1); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    showToast("Musisz mieć podłączenie do internetu, aby przejsc do strony ");
                }

            } else if (id == R.id.nav_send) {

                connect();
                if (polaczenie == 1) {
                    //drugi link
                    Uri uri = Uri.parse(link2); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    showToast("Musisz mieć podłączenie do internetu, aby przejsc do strony ");
                }

            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

