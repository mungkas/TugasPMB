package com.gmail.pamungkas.tugasmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pamungkas on 02/07/18.
 */

public class Parser extends AsyncTask<Void,Integer,Integer> {

    Context c;
    ListView lv;
    String data;

    ArrayList<String> mhs; // diisi nama-nama mhs oleh parse()
    ProgressDialog pd;

    public Parser(Context c, String data, ListView lv) {
        this.c = c;         // MainActivity
        this.data = data;   // data hasil download
        this.lv = lv;       // listview
        mhs = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Parser");
        pd.setMessage("Parsing ....Please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();  // lakukan parsing
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer == 1) { // jika berhasil parsing, hasil parsing dalam mhs
            //ADAPTER
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1,
                            mhs);

            //ADAPTER TO LISTVIEW
            lv.setAdapter(adapter);

            //LISTENER, klik sebuah item akan menampilkan nama dlm Snackbar
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Snackbar.make(view,mhs.get(position),
                            Snackbar.LENGTH_SHORT).show();;
                }
            });

        }else {
            Toast.makeText(c,"Unable to Parse",Toast.LENGTH_SHORT).show();
        }

        pd.dismiss();
    }

    //PARSE RECEIVED DATA
    private int parse() {
        try  {
            // konversi data JSON string ke array
            JSONArray ja = new JSONArray(data);

            //ciptakan objek JSONObject jo utk memegang sebuah item
            JSONObject jo = null;

            mhs.clear();  // kosongkan isi ArrayList

            //LOOP THRU ARRAY
            for(int i=0;i<ja.length();i++) {
                jo = ja.getJSONObject(i);  // ambbil sebuah objek JSON

                //ambil nama
                String nama = jo.getString("nama");

                //tambahkan ke arraylist “mhs”
                mhs.add(nama);
            }
            return 1; // return value = 1, berarti berhasil parsing
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0; // return value = 0, berarti gagal parsing
    }
}
