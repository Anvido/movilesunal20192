package com.example.zip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner dptos_spinner;
    private ArrayList<String> dptos;
    private ArrayAdapter<String> dptos_adapter;
    private Spinner mpos_spinner;
    private ArrayList<String> mpos;
    private ArrayAdapter<String> mpos_adapter;
    private TextView zona_postal;
    private ListView list;
    private ArrayList<String> cod_postal;
    private ArrayAdapter<String> cod_adapter;
    private Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dptos = new ArrayList<>();
        mpos = new ArrayList<>();
        cod_postal = new ArrayList<>();

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        String url = "https://www.datos.gov.co/resource/8gxe-gztc.json?$select=distinct%20nombre_departamento&$order=nombre_departamento%20ASC";

        dptos_spinner = (Spinner) findViewById(R.id.dptos);
        mpos_spinner = (Spinner) findViewById(R.id.mpos);
        zona_postal = findViewById(R.id.zona_postal);
        list = findViewById(R.id.list);

        JsonArrayRequest departamentos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("REQ", "nice");

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                dptos.add(tmp.getString("nombre_departamento"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        dptos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dptos);
                        dptos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dptos_spinner.setAdapter(dptos_adapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("REQ", "bad");
                    }
                });

        dptos_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mpos.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                Log.i("Spin", tmp);
                String url = "https://www.datos.gov.co/resource/8gxe-gztc.json?$select=distinct%20nombre_municipio&nombre_departamento="+ tmp + "&$order=nombre_municipio%20ASC";

                JsonArrayRequest municipios = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Log.i("REQ", "nice");
                                Log.i("REQ", "" + response.length());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        mpos.add(tmp.getString("nombre_municipio"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                mpos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mpos);
                                mpos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mpos_spinner.setAdapter(mpos_adapter);

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });

                // Add the request to the RequestQueue.
                queue.add(municipios);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mpos_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                cod_postal.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);

                Log.i("Spin", tmp);
                String url = "https://www.datos.gov.co/resource/8gxe-gztc.json?nombre_municipio=" + tmp;

                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Log.i("REQ", "nice");
                                Log.i("REQ", "" + response.length());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        String tmp2 = "Cod: " + tmp.getString("codigo_postal") + "\n";
                                        tmp2 += "Tipo: " + tmp.getString("tipo") + "\n";
                                        tmp2 += "Barrios: " + tmp.getString("barrios_contenidos_en_el_codigo_postal") + "\n";
                                        tmp2 += "Veredas: " + tmp.getString("veredas_contenidas_en_el_codigo_postal");
                                        cod_postal.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                cod_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cod_postal);
                                list.setAdapter(cod_adapter);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });

                // Add the request to the RequestQueue.
                queue.add(codes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(departamentos);

    }
}
