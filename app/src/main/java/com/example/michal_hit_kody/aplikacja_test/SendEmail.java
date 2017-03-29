package com.example.michal_hit_kody.aplikacja_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static android.content.Context.MODE_PRIVATE;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendEmail extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    String link1,link2,link_glowny,email1,email2,email_pass,email0,user,login;
    String smtp_host,socetfactory_port,socetfactory_class,smtp_auth,smtp_port;

    private static final String SAMPLE_DB_NAME = "Baza";
    private static final String SAMPLE_TABLE_NAME = "Karta";

    //Information to send email
    private String email;
    private String subject;
    private String message;
    private String attechment_msg;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    int x=0,polaczenie=0;
    String file,fileName;



    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;


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
            Log.i("aaa",String.valueOf(e));
            polaczenie=0;
            return;
        }

        try {
            connection = DriverManager.getConnection(url,user,pass);
        } catch (SQLException e) {
            Log.i("aaa",String.valueOf(e));
            polaczenie=0;
            return;
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
                    String zm = rs.getString("Email2");
                    if (zm != null) {
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
                //showToast("brak polaczenia z internetem");
            }

        }

    }



    //Class Constructor
    public SendEmail(Context context, String email, String subject, String message, String attechment_msg,String login){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.attechment_msg = attechment_msg;
        this.login = login;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Wysyłanie wiadomości","Proszę czekać...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Wiadomość wysłana",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //pobieranie ustawien z bazy danych
        selectmysqlustawienia();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", smtp_host); //"smtp.gmail.com"
        props.put("mail.smtp.socketFactory.port", socetfactory_port); //"465"
        props.put("mail.smtp.socketFactory.class", socetfactory_class); //"javax.net.ssl.SSLSocketFactory"
        props.put("mail.smtp.auth", smtp_auth); //"true"
        props.put("mail.smtp.port", smtp_port); //"465"

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email2, email_pass);
                    }
                });

        try {

            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(email2));

            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); //"m.wisniewski@hit-kody.com.pl"

            //Adding subject
            mm.setSubject(subject);
            mm.setText(message);

            if(attechment_msg.equals("XLS") || attechment_msg.equals("PDF"))
            {
                Log.i("blad","siema_dupa");

                //Adding message attatchment
                MimeBodyPart messageBodyPart = new MimeBodyPart();

                Multipart multipart = new MimeMultipart();

                messageBodyPart = new MimeBodyPart();

                if(attechment_msg.equals("XLS")) {
                     file = Environment.getExternalStorageDirectory().toString() + "/Download/" + login + ".xls";
                     fileName = login + ".xls";
                }

                if(attechment_msg.equals("PDF")) {
                     file = Environment.getExternalStorageDirectory().toString() + "/Download/" + login + ".pdf";
                     fileName = login + ".pdf";
                }

                DataSource source = new FileDataSource(file);
                //  messageBodyPart.setText("your text");
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);

                multipart.addBodyPart(messageBodyPart);

                mm.setContent(multipart);
            }


            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}