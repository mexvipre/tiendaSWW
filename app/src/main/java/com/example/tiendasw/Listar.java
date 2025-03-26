package com.example.tiendasw;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listar extends AppCompatActivity {

    private ListView listView;
    private ProductoAdapter adapter;
    private List<Producto> productoList;

    // URL de tu API
    private static final String URL = "http://192.168.137.1/Tienda/app/services/service-producto.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        listView = findViewById(R.id.listView); // Usa el ID correcto del ListView

        productoList = new ArrayList<>();
        adapter = new ProductoAdapter(this, productoList); // CORREGIDO el orden de parámetros

        listView.setAdapter(adapter);

        cargarProductos(); // Llamamos a la función para obtener los datos
    }

    private void cargarProductos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            productoList.clear(); // Limpiamos la lista para evitar datos duplicados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Producto producto = new Producto(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("tipo"),
                                        jsonObject.getString("genero"),
                                        jsonObject.getString("talla"),
                                        jsonObject.getDouble("precio")
                                );
                                productoList.add(producto);
                            }
                            adapter.notifyDataSetChanged(); // Refrescamos el adaptador
                        } catch (Exception e) {
                            Log.e("Listar", "Error al procesar JSON: " + e.getMessage());
                            Toast.makeText(Listar.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Listar", "Error de conexión: " + error.getMessage());
                        Toast.makeText(Listar.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Añadir la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
