package com.example.tiendasw;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class login extends AppCompatActivity {

    EditText editTextUsuario, editTextContraseña;
    Button buttonLogin, buttonRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        // Botón para iniciar sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = editTextUsuario.getText().toString().trim();
                String contraseña = editTextContraseña.getText().toString().trim();

                if (!usuario.isEmpty() && !contraseña.isEmpty()) {
                    iniciarSesion(usuario, contraseña);
                } else {
                    Toast.makeText(login.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón para ir al formulario de registro
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, register.class));
            }
        });
    }

    // Método para iniciar sesión
    private void iniciarSesion(String usuario, String contraseña) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.137.1/Tienda/app/Test/login.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Crear JSON con los datos de inicio de sesión
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("usuario", usuario);
                    jsonParam.put("password", contraseña);

                    // Enviar datos
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonParam.toString().getBytes("UTF-8"));
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Leer respuesta
                        InputStream is = conn.getInputStream();
                        Scanner scanner = new Scanner(is);
                        StringBuilder response = new StringBuilder();
                        while (scanner.hasNext()) {
                            response.append(scanner.nextLine());
                        }
                        scanner.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    Toast.makeText(login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(login.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(login.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
    }
}
