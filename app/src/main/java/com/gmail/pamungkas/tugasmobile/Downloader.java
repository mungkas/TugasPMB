package com.gmail.pamungkas.tugasmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pamungkas on 02/07/18.
 */

public class Downloader extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;

    ProgressDialog pd;

    public Downloader(Context c, String address, ListView lv) {
        this.c = c;
        this.address = address;
        this.lv = lv;
    }

    //Saat proses baca data berlangsung akan muncuk persan
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Baca Data");
        pd.setMessage("Sedang membaca Data...tunggu yaaa..");
        pd.show();
    }

    //menjalankan sebagai backgroudn proses
    @Override
    protected String doInBackground(Void... params) {
        String data = downloadData(); // download data dari server
        return data;
    }


    // parameter s adalah string data hasil download
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.dismiss(); // hilangkan dialog

        //jika s DATA ADA, maka jalankan Parser- pengurai JSON menjadi array
//jika s kosong akan muncul  “Data tidak bisa diunduh”
        if(s != null)
        {
            //lakukan parsing
            Parser p = new Parser(c,s,lv);
            p.execute();
        }else {
            Toast.makeText(c,"Data tidak bisa diunduh",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // download data dari server
    // return value berupa data hasil download
    // jika tidak berhasil, return value = null
    private String downloadData() {
        //connect and get a stream
        InputStream is=null;
        String line =null;

        try {
            URL url=new URL(address);  // ciptakan objek URL

            // buka koneksi dengan url.openConnection(),
            // hasil berupa HttpURLConnection
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            // ciptakan input stream
            is = new BufferedInputStream(con.getInputStream());

            // ciptakan objek buffer reader berdasar input stream
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(is));

            StringBuffer sb = new StringBuffer();


            if(br != null) {
                // menghasilkan (membaca) baris teks yg ada pada BufferReader
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                    //System.out.println("linenya = "+line);  // hanya sbg test
                }
            }else {
                return null;
            }

            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
