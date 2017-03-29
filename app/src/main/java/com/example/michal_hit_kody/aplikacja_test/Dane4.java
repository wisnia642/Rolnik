package com.example.michal_hit_kody.aplikacja_test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

public class Dane4 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button dalej;
    Button clean;
    Button wstecz;
    Bundle applesData;

    private PopupWindow mpopup;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    AutoCompleteTextView dane;

    String[] id = new String[15];

    String hash1,data,zm,s,p,uzytkownik,link1,link2;
    int x=0,Warunek_do_przejścia=0,polaczenie=0,z=0;

    private static final String SAMPLE_DB_NAME = "Baza";
    private static final String SAMPLE_TABLE_NAME = "Karta";

    String email_msg = "";
    String subject = "";
    String message = "";
    String attechement_msg = "";

    private void email_SqlLigt() {
        x = 0;


        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from users where Uzytkownik='"+uzytkownik+"'", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(0));
                if (zm != null) {
                    email_msg = String.valueOf(c.getString(3));
                    //   showToast(link_glowny);
                }
            }
            sampleDB.close();
        } catch (Exception e) {

        }

    }

    private void sendemail1() {
        //Getting content for email
        email_SqlLigt();

        //Creating SendMail object
        SendEmail sm = new SendEmail(this, email_msg, subject, message, attechement_msg,uzytkownik);

        //Executing sendmail to send email
        sm.execute();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
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

    public void UpdateSql1wyloguj() {

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("UPDATE users SET Stan=('0') WHERE Uzytkownik='" + uzytkownik + "'");
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


            String sql1 = "UPDATE users SET Stan = ('0') WHERE Uzytkownik = '" + uzytkownik + "'";

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


    public void createpdf()
    {

        float left = -70;
        float right = -70;
        float top = 25;
        float bottom = 25;

        Document doc = new Document(PageSize.A4.rotate(),left, right, top, bottom);

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        Cursor title = sampleDB.rawQuery("SELECT Uzytkownik FROM users where Uzytkownik='"+uzytkownik+"'",null);

        title.moveToFirst();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, uzytkownik);
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file+".pdf");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                PdfWriter.getInstance(doc, fOut);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            //open the document
            doc.open();


            Paragraph p3 = new Paragraph();
            p3.add("Tabela z danymi");
            p3.setAlignment(Paragraph.ALIGN_CENTER);





            doc.add(p3);


            Paragraph p4 = new Paragraph();
            p4.add(" ");
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            try
            {
                doc.add(p4);
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }



            PdfPTable table = new PdfPTable(13);
            table.addCell("Lp.");
            table.addCell("Gatunek");
            table.addCell("Odmiana");
            table.addCell("Data i Godzina");
            table.addCell("Powierzchnia");
            table.addCell("Numer działki");
            table.addCell("Preparat");
            table.addCell("Dawka");
            table.addCell("Substancja czynna");
            table.addCell("Temperatura");
            table.addCell("Faza rozwoju");
            table.addCell("Przyczyny zabiegu");
            table.addCell("Uwagi");

            try {
                Cursor c = sampleDB.rawQuery("select * from '"+uzytkownik+"'", null);
                while (c.moveToNext()) {
                    String zm = String.valueOf(c.getString(1));
                    if (zm != null) {
                        table.addCell(String.valueOf(c.getString(0)));
                        table.addCell(String.valueOf(c.getString(2)));
                        table.addCell(String.valueOf(c.getString(3)));
                        table.addCell(String.valueOf(c.getString(4)));
                        table.addCell(String.valueOf(c.getString(5)));
                        table.addCell(String.valueOf(c.getString(6)));
                        table.addCell(String.valueOf(c.getString(7)));
                        table.addCell(String.valueOf(c.getString(8)));
                        table.addCell(String.valueOf(c.getString(9)));
                        table.addCell(String.valueOf(c.getString(10)));
                        table.addCell(String.valueOf(c.getString(11)));
                        table.addCell(String.valueOf(c.getString(12)));
                        table.addCell(String.valueOf(c.getString(13)));
                    }


                    sampleDB.close();

                    // table.setTotalWidth(PageSize.A4.getWidth());
                    //   table.setLockedWidth(true);
                }

            }catch (Exception e) {
                showToast(""+e);
            }

            try
            {
                doc.add(table);
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }
            doc.addCreationDate();

            doc.close();

            Toast toast = Toast.makeText(getApplicationContext(), "PDF Created", Toast.LENGTH_SHORT);
            toast.show();


        }catch (Exception e)
        {
            showToast(""+e);
        }
    }

    public void pdf1()
    {
        ArrayList arList=null;
        ArrayList al=null;

        //File dbFile= new File(getDatabasePath("database_name").toString());
        File dbFile=getDatabasePath("database_name");
        String yes= dbFile.getAbsolutePath();

        String inFilePath = Environment.getExternalStorageDirectory().toString()+"/Download/"+uzytkownik+".csv";
        String outFilePath = Environment.getExternalStorageDirectory().toString()+"/Download/"+uzytkownik+".xls";
        String thisLine;
        int count=0;

        try {

            FileInputStream fis = new FileInputStream(inFilePath);
            DataInputStream myInput = new DataInputStream(fis);
            int i=0;
            arList = new ArrayList();
            while ((thisLine = myInput.readLine()) != null)
            {
                al = new ArrayList();
                String strar[] = thisLine.split(",");
                for(int j=0;j<strar.length;j++)
                {
                    al.add(strar[j]);
                }
                arList.add(al);
                System.out.println();
                i++;
            }} catch (Exception e) {
            System.out.println("shit");
        }

        try
        {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<ardata.size();p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(data);
                    }

                    // cell.setCellValue(ardata.get(p).toString());
                }
                System.out.println();
            }
            FileOutputStream fileOut = new FileOutputStream(outFilePath);
            hwb.write(fileOut);
            fileOut.close();
            showToast("Your excel file has been generated");
        } catch ( Exception ex ) {

            showToast(""+ex);
        }
        //main method ends
        //  return true;
    }



    public void exportDataBaseIntoCSV(){


        //  CredentialDb db = new CredentialDb(context);//here CredentialDb is my database. you can create your db object.
        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "/Download");

        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, uzytkownik+".csv");

        try
        {
            file.createNewFile();


            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            // SQLiteDatabase sql_db = sampleDB.getReadableDatabase();//here create a method ,and return SQLiteDatabaseObject.getReadableDatabase();

            Cursor curCSV = sampleDB.rawQuery("SELECT * FROM '"+uzytkownik+"'",null);
            // Cursor curCSV = sql_db.rawQuery("SELECT * FROM "+CredentialDb.TABLE_NAME,null);

            String arrStr1[] = {"Lp.","Gatunek","Odmiana","Data i Godzina","Powierzchnia","Numer działki","Preparat","Dawka","Substancja czynna","Temperatura",
                    "Faza rozwoju","Przyczyny zabiegu","Uwagi"};
            csvWrite.writeNext(arrStr1);

            while(curCSV.moveToNext())
            {
                //Which column you want to export you can add over here...
                String arrStr[] ={curCSV.getString(0), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5),
                        curCSV.getString(6),curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12),
                        curCSV.getString(13)};
             //   showToast(String.valueOf(arrStr[0]));
                csvWrite.writeNext(arrStr);
            }

            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Error:", sqlEx.getMessage(), sqlEx);
        }
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

    public void updatemysql()
    {
        connect();
        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


            String sql1 ="update "+uzytkownik+" set Powierzchnia='"+data+"' where id='"+p+"'";

            try {
                st.executeUpdate(sql1);
            } catch (SQLException e1) {
                // e1.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                Log.i("blad","cos jest nie tak"+se);
            }
        }
    }


    public void InsertLoginDataSqligt() {

        try {

            data = dane.getText().toString();
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null); //%032x44047210810492668751
            sampleDB.execSQL("update edit set Powierzchnia='"+data+"' where Id='1' ");
            sampleDB.close();
        } catch (Exception e) {
            //    showToast(""+e);
        }
    }

    public void UpdateLoginDataSqligt() {

        try {

            data = dane.getText().toString();
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null); //%032x44047210810492668751
            sampleDB.execSQL("update '"+uzytkownik+"' set Powierzchnia='"+data+"' where id='"+p+"' ");
            sampleDB.close();
        } catch (Exception e) {
            //    showToast(""+e);
        }
    }

    private void Read_Login_SqlLigt() {
        x = 0;

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from edit", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(1));
                if (zm != null) {
                    uzytkownik = String.valueOf(c.getString(13));

                }
            }
            sampleDB.close();
        } catch (Exception e) {

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
     *
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
        setContentView(R.layout.activity_dane4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dalej = (Button) findViewById(R.id.button3);
        clean = (Button) findViewById(R.id.button5);
        wstecz = (Button) findViewById(R.id.button4);
        dane = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        s = getIntent().getStringExtra("EXTRA_SESSION_ID");
        p = getIntent().getStringExtra("EXTRA_SESSION");
        dane.setText(s);

        ustawienia_SqlLigt();
        Read_Login_SqlLigt();

        verifyStoragePermissions(Dane4.this);

        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(s!=null || p!=null)
                {
                    if(p!=null)
                    {
                        //aktualizuje dane w tabeli uzytkownik sqlight
                        UpdateLoginDataSqligt();

                        connect();
                        if(polaczenie==1) {
                            //aktualizuje dane w tabeli uzytkownik mysql
                            updatemysql();
                        }

                    }else {

                        //wprowadanie danych do bazy edit
                        InsertLoginDataSqligt();
                    }
                    Intent c = new Intent(Dane4.this, Koniec.class);
                    c.putExtra("EXTRA_SESSION", p);
                    startActivity(c);

                }else
                {
                    //wprowadanie danych do bazy
                    InsertLoginDataSqligt();

                    // przejście do następnego layout'u
                    Intent c = new Intent(Dane4.this, Dane5.class);
                    startActivity(c);
                }
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dane.setText("");
            }
        });

        wstecz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(s!=null)
                {
                    Intent intent = new Intent(getBaseContext(), Koniec.class);
                    intent.putExtra("EXTRA_SESSION", p);
                    startActivity(intent);

                }else {

                    Intent c = new Intent(Dane4.this, Dane3.class);
                    //blokowanie cofnięcia
                    //  c.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(c);
                }
            }
        });

        /*
        dane.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 3) {
                    editable.append('.');
                }
            }
        });
        */
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
        // getMenuInflater().inflate(R.menu.dane1, menu);
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
            UpdateSql1wyloguj();

            Intent c = new Intent(Dane4.this, MainActivity.class);
            startActivity(c);

        } else if (id == R.id.nav_gallery) {
            // podglad dodanych rekordow

            Intent c = new Intent(Dane4.this, Podglad.class);
            startActivity(c);


        } else if (id == R.id.nav_slideshow) {
            // edytowanie hasla lub emaila
            connect();
            if(polaczenie == 1) {
                Intent c = new Intent(Dane4.this, Ustawienia.class);
                startActivity(c);
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do ustawien ");
            }


        } else if (id == R.id.nav_manage) {
            //zapis do PDF
            connect();
            if(polaczenie == 1) {

                View popUpView = getLayoutInflater().inflate(R.layout.mpoup, null);
                // inflating popup layout
                mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                //Creation of popup
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                Button download = (Button) popUpView.findViewById(R.id.button13);
                Button send_email = (Button) popUpView.findViewById(R.id.button60);
                TextView tekst = (TextView) popUpView.findViewById(R.id.textView146);
                tekst.setText("Zapis danych do PDF");

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //zapis pliku do pdf
                        createpdf();

                        showToast("Plik pdf. został zapisany na karcie SD");

                        mpopup.dismiss();
                    }
                });

                send_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Wysyłanie emaila z załącznikiem
                        createpdf();
                        attechement_msg="PDF";
                        sendemail1();
                        mpopup.dismiss();
                    }
                });

            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do PDF ");
            }

        } else if (id == R.id.nev_manage1) {
            //zapis do EXCEL
            connect();
            if(polaczenie == 1) {
                View popUpView = getLayoutInflater().inflate(R.layout.mpoup, null);
                // inflating popup layout
                mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                //Creation of popup
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                Button download = (Button) popUpView.findViewById(R.id.button13);
                Button send_email = (Button) popUpView.findViewById(R.id.button60);
                TextView tekst = (TextView) popUpView.findViewById(R.id.textView146);
                tekst.setText("Zapis danych do XLS");

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //zapis pliku do csc
                        exportDataBaseIntoCSV();

                        //zapis do pliku xls
                        pdf1();

                        showToast("Plik xls. został zapisany na karcie SD");

                        mpopup.dismiss();
                    }
                });

                send_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Wysyłanie emaila z załącznikiem

                        //zapis pliku do csc
                        exportDataBaseIntoCSV();
                        //zapis do pliku xls
                        pdf1();
                        attechement_msg="XLS";
                        sendemail1();
                        mpopup.dismiss();
                    }
                });
            }else
            {
                showToast("Musisz mieć podłączenie do internetu, aby przejsc do EXCEL ");
            }


        } else if (id == R.id.nav_help) {

            //napisanie emaila do pomocy
            connect();
            if(polaczenie == 1) {
                Intent c = new Intent(Dane4.this, SendHelp.class);
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