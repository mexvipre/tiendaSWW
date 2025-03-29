package com.example.tiendasw;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class register extends AppCompatActivity {

    EditText editTextNombre, editTextUsuario, editTextContraseña;
    Button buttonRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        // Botón para registrar usuario
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                String usuario = editTextUsuario.getText().toString().trim();
                String contraseña = editTextContraseña.getText().toString().trim();

                if (!nombre.isEmpty() && !usuario.isEmpty() && !contraseña.isEmpty()) {
                    registrarUsuario(nombre, usuario, contraseña);
                } else {
                    Toast.makeText(register.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para registrar usuario
    private void registrarUsuario(String nombre, String usuario, String contraseña) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.1.22/Tienda/app/Test/register.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Crear JSON con los datos del usuario
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("nombre", nombre);
                    jsonParam.put("usuario", usuario);
                    jsonParam.put("password", contraseña);

                    // Enviar datos al servidor
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonParam.toString().getBytes("UTF-8"));
                    os.close();

                    int responseCode = conn.getResponseCode();

                    // Leer respuesta del servidor
                    InputStream is = (responseCode == HttpURLConnection.HTTP_OK) ? conn.getInputStream() : conn.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    // Mostrar respuesta en Logcat
                    Log.d("Respuesta del servidor", sb.toString());

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                finish(); // Cierra la actividad después de registrar
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(register.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(register.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
    }
}
