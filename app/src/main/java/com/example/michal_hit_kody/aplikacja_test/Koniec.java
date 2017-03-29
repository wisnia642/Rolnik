package com.example.michal_hit_kody.aplikacja_test;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVWriter;

public class Koniec extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button wstecz;
    Button Zakoncz;
    Button usun;
    ListView lista;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    private PopupWindow mpopup;

    String email_msg = "";
    String subject = "";
    String message = "";
    String attechement_msg = "";

    int x=0,y=0,Warunek_do_przejścia=0,polaczenie=0,z=2;

    String dane0,dane1,dane2,dane3,dane4,dane5,dane6,dane7,dane8,dane9,dane10,dane11,dane12;
    String uzytkownik,kiedy,pozycja,link1,link2,login;

    private static final String SAMPLE_DB_NAME = "Baza";
    private static final String SAMPLE_TABLE_NAME = "Karta";

    private ArrayAdapter<String> adapter;

    private String TAG = Koniec.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    Calendar myCalendar;
    private SimpleDateFormat sdf;
    DatePickerDialog.OnDateSetListener date;

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

    private void updateLabel() {

       // String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        // textView is the TextView view that should display it
      //  kiedy=currentDateTimeString;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        kiedy = sdf.format(new Date());
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    private void ToDataBaseSqllight() {

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS '"+uzytkownik+"' (Id VARCHAR,Kiedy VARCHAR,Gatunek VARCHAR,Odmiana VARCHAR,Data_Godzina VARCHAR,Powierzchnia VARCHAR,Nr_Działki VARCHAR," +
                    "Preparat VARCHAR,Dawka VARCHAR,Substancja_czynna VARCHAR,Temperatura VARCHAR,Faza_rozwoju VARCHAR,Przyczyny_zabiegu VARCHAR,Uwagi VARCHAR);");
        } catch (Exception e) {
        }
    }

    private void TodataBaseMysql()
    {

        connect();
        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();

            }
            String sql = "CREATE TABLE IF NOT EXISTS "+uzytkownik+"(`ID` VARCHAR(300) NULL DEFAULT NULL," +
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
                showToast(""+e1);
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
            //    showToast("brak połączenia z internetem");
            }
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

    private void Read_Login() {
        x = 0;


        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from edit", null);

            while (c.moveToNext()) {
               // showToast("1");
                String zm = String.valueOf(c.getString(0));
                if (zm != null) {
                    dane1 = String.valueOf(c.getString(1));
                    dane2 = String.valueOf(c.getString(2));
                    dane3 = String.valueOf(c.getString(3));
                    dane4 = String.valueOf(c.getString(4));
                    dane5 = String.valueOf(c.getString(5));
                    dane6 = String.valueOf(c.getString(6));
                    dane7 = String.valueOf(c.getString(7));
                    dane8 = String.valueOf(c.getString(8));
                    dane9 = String.valueOf(c.getString(9));
                    dane10 = String.valueOf(c.getString(10));
                    dane11 = String.valueOf(c.getString(11));
                    dane12 = String.valueOf(c.getString(12));
                    uzytkownik = String.valueOf(c.getString(13));
                    x++;
                }
            }
            sampleDB.close();
        } catch (Exception e) {

        }

    }


    private void Read_data_SqlLigt() {
        x = 0;

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from '"+uzytkownik+"' where Kiedy='"+pozycja+"'", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(1));
                if (zm != null) {
                    dane0 = String.valueOf(c.getString(1));
                    dane1 = String.valueOf(c.getString(2));
                    dane2 = String.valueOf(c.getString(3));
                    dane3 = String.valueOf(c.getString(4));
                    dane4 = String.valueOf(c.getString(5));
                    dane5 = String.valueOf(c.getString(6));
                    dane6 = String.valueOf(c.getString(7));
                    dane7 = String.valueOf(c.getString(8));
                    dane8 = String.valueOf(c.getString(9));
                    dane9 = String.valueOf(c.getString(10));
                    dane10 = String.valueOf(c.getString(11));
                    dane11 = String.valueOf(c.getString(12));
                    dane12 = String.valueOf(c.getString(13));
                    x++;
                }
            }
            sampleDB.close();
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


            String sql1 = "INSERT INTO "+uzytkownik+" " +
                    "(ID,Kiedy,Gatunek,Odmiana,Data_Godzina,Powierzchnia,Nr_Działki,Preparat,Dawka,Substancja_czynna,Temperatura,Faza_rozwoju,Przyczyny_zabiegu,Uwagi)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                ps = connection.prepareStatement(sql1);
                ps.setString(1, String.valueOf(y));
                ps.setString(2, kiedy);
                ps.setString(3, dane1);
                ps.setString(4, dane2);
                ps.setString(5, dane3);
                ps.setString(6, dane4);
                ps.setString(7, dane5);
                ps.setString(8, dane6);
                ps.setString(9, dane7);
                ps.setString(10, dane8);
                ps.setString(11, dane9);
                ps.setString(12, dane10);
                ps.setString(13, dane11);
                ps.setString(14, dane12);

                ps.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                 Log.i("polaczenie","koniec" +e);
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                Log.i("polaczenie","koniec" +se);
            }
        }
    }

    public void InsertLoginDataSqligt() {
      //  ToDataBase();

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("INSERT INTO '"+uzytkownik+"' (Id,Kiedy,Gatunek,Odmiana,Data_Godzina,Powierzchnia,Nr_Działki,Preparat,Dawka,Substancja_czynna,Temperatura,Faza_rozwoju,Przyczyny_zabiegu,Uwagi)" +
                    " VALUES ('"+String.valueOf(y)+"','"+kiedy+"','"+dane1+"','"+dane2+"','"+dane3+"','"+dane4+"','"+dane5+"','"+dane6+"'," +
                    "'"+dane7+"','"+dane8+"','"+dane9+"','"+dane10+"','"+dane11+"','"+dane12+"') ");
            sampleDB.close();
        } catch (Exception e) {
           //  showToast(""+e);
        }
    }

    private void licznik() {
        y = 1;

        SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        try {
            Cursor c = sampleDB.rawQuery("select * from '"+uzytkownik+"'", null);

            while (c.moveToNext()) {
                String zm = String.valueOf(c.getString(1));
                if (zm != null) {
                    // = String.valueOf(c.getString(1));
                    y++;
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
              //  showToast("brak połączenia z internetem");
            }
        }
    }

    public void ustawienia_SqlLigt() {
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

    public void delete_from_mysql()
    {
        connect();

        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


            String sql1 = "delete from "+ uzytkownik +" where Kiedy='"+dane0+"'";

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

    public void delete_from_sqlight()
    {
        SQLiteDatabase sampleDB1 = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

        sampleDB1.execSQL("delete from '" + uzytkownik + "' where Kiedy='"+dane0+"'");

        sampleDB1.close();
    }

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
        setContentView(R.layout.activity_koniec);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        wstecz = (Button) findViewById(R.id.button10);
        Zakoncz = (Button) findViewById(R.id.button9);
        usun = (Button) findViewById(R.id.button13);


        //pobieranie danych z sqlight
        pozycja = getIntent().getStringExtra("EXTRA_SESSION");
        uzytkownik = getIntent().getStringExtra("USER");
        if(pozycja!=null)
        {
           // showToast("0");
            //odczytywanie danych z tabeli uzytkownik
            Read_data_SqlLigt();
        }else
        {
          //  showToast("1");
            //odczytywanie danych  z tabeli edit
            Read_Login();
        }
        ustawienia_SqlLigt();

        verifyStoragePermissions(Koniec.this);

        lista = (ListView) findViewById(R.id.lista1);

        String cars[] = {"Gatunek: "+dane1,"Odmiana: "+dane2,"Data i godzina: "+dane3,"Powierzchnia: "+dane4,
                "Numer działki: "+dane5,"Preparat: "+dane6,"Dawka stężenie: "+dane7,"Substancja czynna: "+dane8,"Temperatura: "+dane9,
                "Faza rzwoju: "+dane10,"Przyczyny zabiegu: "+dane11,"Uwagi: "+dane12};

        final ArrayList<String> carL = new ArrayList<String>();
        carL.addAll( Arrays.asList(cars) );

        adapter = new ArrayAdapter<String>(this, R.layout.row, carL);

        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (pozycja != null) {}else
                {

                switch (position)
                {

                    case 0:
                        Intent intent = new Intent(getBaseContext(), Dane1.class);
                        intent.putExtra("EXTRA_SESSION_ID", dane1);
                        intent.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(getBaseContext(), Dane2.class);
                        intent2.putExtra("EXTRA_SESSION_ID", dane2);
                        intent2.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(getBaseContext(), Dane3.class);
                        intent3.putExtra("EXTRA_SESSION_ID", dane3);
                        intent3.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(getBaseContext(), Dane4.class);
                        intent4.putExtra("EXTRA_SESSION_ID", dane4);
                        intent4.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent4);
                        break;
                    case 4:
                        Intent intent5 = new Intent(getBaseContext(), Dane5.class);
                        intent5.putExtra("EXTRA_SESSION_ID", dane5);
                        intent5.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent5);
                        break;
                    case 5:
                        Intent intent6 = new Intent(getBaseContext(), Dane6.class);
                        intent6.putExtra("EXTRA_SESSION_ID", dane6);
                        intent6.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent6);
                        break;
                    case 6:
                        Intent intent7 = new Intent(getBaseContext(), Dane7.class);
                        intent7.putExtra("EXTRA_SESSION_ID", dane7);
                        intent7.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent7);
                        break;
                    case 7:
                        Intent intent8 = new Intent(getBaseContext(), Dane8.class);
                        intent8.putExtra("EXTRA_SESSION_ID", dane8);
                        intent8.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent8);
                        break;
                    case 8:
                        Intent intent9 = new Intent(getBaseContext(), Dane9.class);
                        intent9.putExtra("EXTRA_SESSION_ID", dane9);
                        intent9.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent9);
                        break;
                    case 9:
                        Intent intent10 = new Intent(getBaseContext(), Dane10.class);
                        intent10.putExtra("EXTRA_SESSION_ID", dane10);
                        intent10.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent10);
                        break;
                    case 10:
                        Intent intent11 = new Intent(getBaseContext(), Dane11.class);
                        intent11.putExtra("EXTRA_SESSION_ID", dane11);
                        intent11.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent11);
                        break;
                    case 11:
                        Intent intent12 = new Intent(getBaseContext(), Dane12.class);
                        intent12.putExtra("EXTRA_SESSION_ID", dane12);
                        intent12.putExtra("EXTRA_SESSION", pozycja);
                        startActivity(intent12);
                        break;

                    default:
                       // showToast("koniec");
                        break;
                }
            }}
        });

        wstecz.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

                if(pozycja!=null)
                {
                    Intent c = new Intent(Koniec.this, Podglad.class);
                    startActivity(c);

                }else {
                    Intent c = new Intent(Koniec.this, Dane12.class);
                    startActivity(c);
                }
            }
        });

        usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
                //usuwanie rekordu
                if(polaczenie==1)
                {
                    delete_from_mysql();
                    delete_from_sqlight();
                }else
                {
                    delete_from_sqlight();
                }

                Intent c = new Intent(Koniec.this, Glowne_menu.class);
                startActivity(c);
            }
        });

        Zakoncz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect();

                if (polaczenie == 1) {
                 //   ToDataBaseSqllight();
                 //   TodataBaseMysql();

                    if (pozycja != null) {
                        Intent c = new Intent(Koniec.this, Podglad.class);
                        c.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(c);
                    } else {
                        //sprawdzanie ilosci rekordow
                        licznik();

                        //sprawdzanie daty i godziny
                        updateLabel();

                        //Insert danych do tabeli uzytkownika sqlight
                        InsertLoginDataSqligt();

                        //insert danych do tabeli uzytkownika mysql
                        InsertLoginDataMysql();


                        Intent c = new Intent(Koniec.this, Glowne_menu.class);
                        c.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(c);

                    }
                } else if (polaczenie == 0) {

                    ToDataBaseSqllight();

                    if (pozycja != null) {
                        Intent c = new Intent(Koniec.this, Podglad.class);
                        c.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(c);
                    } else {

                        //sprawdzanie ilosci rekordow
                        licznik();

                        //sprawdzanie daty i godziny
                        updateLabel();

                        //Insert danych do tabeli uzytkownika sqlight
                        InsertLoginDataSqligt();


                        Intent c = new Intent(Koniec.this, Glowne_menu.class);
                        c.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(c);
                    }
                }
            }
        });

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
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
      //  getMenuInflater().inflate(R.menu.koniec, menu);
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
            // wylogowanie uzytkownika
             UpdateSql1wyloguj();

            Intent c = new Intent(Koniec.this, MainActivity.class);
            startActivity(c);

        } else if (id == R.id.nav_gallery) {
            // podglad dodanych rekordow
            Intent c = new Intent(Koniec.this, Podglad.class);
            startActivity(c);


        } else if (id == R.id.nav_slideshow) {
            // edytowanie hasla lub emaila
            connect();
            if(polaczenie == 1) {
                Intent c = new Intent(Koniec.this, Ustawienia.class);
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
                Intent c = new Intent(Koniec.this, SendHelp.class);
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
