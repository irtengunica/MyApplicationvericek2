package com.example.okul.myapplicationvericek2;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity{
    public static String URL = "http://turulay.com/isim4.php?ogrenci_adi=";//Bilgisayarýn IP adresi

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button giris_butonu=(Button)findViewById(R.id.giris_button);
        final EditText editText=(EditText)findViewById(R.id.editText1);
        giris_butonu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String isim=editText.getText().toString();
                fetchJsonTask a = new fetchJsonTask();
                a.execute(URL+isim);
            }
        });


    }
    public static String connect(String url){
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response=httpClient.execute(httpget);
            HttpEntity entity=response.getEntity();
            if(entity!=null){
                InputStream instream=entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return sb.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }




    class fetchJsonTask extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String ret = connect(params[0]);
                ret = ret.trim();
                JSONObject jsonObj = new JSONObject(ret);
                return jsonObj;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                parseJson(result);
            } else {
                TextView tv = (TextView) findViewById(R.id.textView1);
                tv.setText("Kayýt Bulunamadý");
                tv.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "Json null",
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    public void parseJson(JSONObject ogrenciJson) {
        TextView ogrenci_ad_tv = (TextView) findViewById(R.id.ogrenci_ad_tv);
        TextView ogrenci_numara_tv = (TextView) findViewById(R.id.ogrenci_numara_tv);
        TextView ogrenci_bolum_tv = (TextView) findViewById(R.id.ogrenci_bolum_tv);
        TextView ogrenci_okul_tv = (TextView) findViewById(R.id.ogrenci_okul_tv);
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("");
        System.out.println(ogrenciJson);
        try {
            ogrenci_ad_tv.setText("Öðrenci Adý :"+ ogrenciJson.getString("ogrenci_adi"));
            ogrenci_numara_tv.setText("Numara :"+ ogrenciJson.getString("ogrenci_numara"));
            ogrenci_bolum_tv.setText("Bölüm :"+ ogrenciJson.getString("ogrenci_bolum"));
            ogrenci_okul_tv.setText("Okul :"+ ogrenciJson.getString("ogrenci_okul"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
