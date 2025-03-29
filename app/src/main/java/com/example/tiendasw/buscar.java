package com.example.tiendasw;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class buscar extends AppCompatActivity {

    private EditText editTextIdBuscar, editTextTallaBuscar, editTextPrecioBuscar;
    private Spinner spinnerTipoBuscar;
    private RadioGroup radioGroupGeneroBuscar;
    private RadioButton radioMasculinoBuscar, radioFemeninoBuscar, radioUnisexBuscar;
    private Button buttonBuscar, buttonActualizar, buttonEliminar;
    private RequestQueue requestQueue;
    private ArrayAdapter<CharSequence> adapter;

    private static final String URL_BASE = "http://192.168.1.22/Tienda/app/services/service-producto.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        inicializarComponentes();
        configurarEventos();

        requestQueue = Volley.newRequestQueue(this);

        // Adaptador para el spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.product_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoBuscar.setAdapter(adapter);
    }

    private void inicializarComponentes() {
        editTextIdBuscar = findViewById(R.id.editTextIdBuscar);
        editTextTallaBuscar = findViewById(R.id.editTextTallaBuscar);
        editTextPrecioBuscar = findViewById(R.id.editTextPrecioBuscar);
        spinnerTipoBuscar = findViewById(R.id.spinnerTipoBuscar);
        radioGroupGeneroBuscar = findViewById(R.id.radioGroupGeneroBuscar);
        radioMasculinoBuscar = findViewById(R.id.radioMasculinoBuscar);
        radioFemeninoBuscar = findViewById(R.id.radioFemeninoBuscar);
        radioUnisexBuscar = findViewById(R.id.radioUnisexBuscar);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        buttonActualizar = findViewById(R.id.buttonActualizar);
        buttonEliminar = findViewById(R.id.buttonEliminar);
    }

    private void configurarEventos() {
        buttonBuscar.setOnClickListener(v -> buscarProducto());
        buttonActualizar.setOnClickListener(v -> actualizarProducto());
        buttonEliminar.setOnClickListener(v -> eliminarProducto());
    }

    private void buscarProducto() {
        String id = editTextIdBuscar.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el ID del producto", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = URL_BASE + "?id=" + id;

        Log.d("Volley", "Solicitud enviada a: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Toast.makeText(this, "Respuesta: " + response.toString(), Toast.LENGTH_LONG).show();

                        if (response.getBoolean("success")) {
                            JSONObject producto = response.getJSONObject("producto");

                            String talla = producto.optString("talla", "");
                            String precio = producto.optString("precio", "");
                            String tipo = producto.optString("tipo", "");
                            String genero = producto.optString("genero", "");

                            Log.d("Buscar", "Talla: " + talla);
                            Log.d("Buscar", "Precio: " + precio);
                            Log.d("Buscar", "Tipo: " + tipo);
                            Log.d("Buscar", "Género: " + genero);

                            // Actualizar los campos con los datos obtenidos
                            if (!talla.isEmpty()) {
                                editTextTallaBuscar.setText(talla); // Actualizar Talla
                            } else {
                                editTextTallaBuscar.setText("");
                            }

                            if (!precio.isEmpty()) {
                                editTextPrecioBuscar.setText(precio); // Actualizar Precio
                            } else {
                                editTextPrecioBuscar.setText("");
                            }

                            // Actualizar Spinner (Tipo de Producto)
                            int posTipo = adapter.getPosition(tipo);
                            if (posTipo >= 0) {
                                spinnerTipoBuscar.setSelection(posTipo); // Establecer el tipo seleccionado
                            }

                            // Actualizar RadioButton (Género)
                            if (genero.equalsIgnoreCase("Masculino")) {
                                radioMasculinoBuscar.setChecked(true); // Género Masculino
                            } else if (genero.equalsIgnoreCase("Femenino")) {
                                radioFemeninoBuscar.setChecked(true); // Género Femenino
                            } else if (genero.equalsIgnoreCase("Unisex")) {
                                radioUnisexBuscar.setChecked(true); // Género Unisex
                            }

                            Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show();

                        } else {
                            String mensaje = response.optString("message", "Error al buscar el producto.");
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }

    private void actualizarProducto() {
        // Implementar la lógica para actualizar el producto
    }

    private void eliminarProducto() {
        // Implementar la lógica para eliminar el producto
    }
}
