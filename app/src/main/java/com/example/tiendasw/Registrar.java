package com.example.tiendasw;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    private Spinner spinnerTipo;
    private EditText editTextTalla, editTextPrecio;
    private RadioGroup radioGroupGenero;
    private Button buttonRegistrar;
    private String URL = "http://192.168.1.22/Tienda/app/services/service-producto.php"; // URL de tu API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // Referencias a los elementos de la interfaz
        spinnerTipo = findViewById(R.id.spinnerTipo);
        editTextTalla = findViewById(R.id.editTextTalla);
        editTextPrecio = findViewById(R.id.editTextPrecio);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        // ✅ Cargar datos en el Spinner desde strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.product_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // ✅ Acción al presionar el botón Registrar
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarProducto();
            }
        });
    }

    // ✅ Método para registrar producto mediante POST
    private void registrarProducto() {
        String tipo = spinnerTipo.getSelectedItem().toString();
        String talla = editTextTalla.getText().toString().trim();
        String precio = editTextPrecio.getText().toString().trim();

        // Verificar el género seleccionado
        String genero = "";
        int checkedRadioButtonId = radioGroupGenero.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radioMasculino) {
            genero = "M";
        } else if (checkedRadioButtonId == R.id.radioFemenino) {
            genero = "F";
        } else if (checkedRadioButtonId == R.id.radioUnisex) {
            genero = "U";
        }

        // ✅ Validar que todos los campos estén completos
        if (tipo.isEmpty() || talla.isEmpty() || precio.isEmpty() || genero.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Crear objeto JSON con los datos
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tipo", tipo);
            jsonObject.put("genero", genero);
            jsonObject.put("talla", talla);
            jsonObject.put("precio", precio);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ✅ Crear la solicitud POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("msg");
                            if (status.equals("success")) {
                                Toast.makeText(Registrar.this, msg, Toast.LENGTH_SHORT).show();
                                limpiarCampos(); // Limpiar campos tras registro exitoso
                            } else {
                                Toast.makeText(Registrar.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Registrar.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registrar.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // ✅ Añadir la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    // ✅ Método para limpiar campos
    private void limpiarCampos() {
        spinnerTipo.setSelection(0);
        editTextTalla.setText("");
        editTextPrecio.setText("");
        radioGroupGenero.clearCheck();
    }
}
