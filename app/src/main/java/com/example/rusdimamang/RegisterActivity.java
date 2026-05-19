package com.example.rusdimamang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText eUsername, ePassword;
    Button btnDaftar;

    String URL = "http://10.0.2.2/phpnativebulia/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eUsername = findViewById(R.id.eUsername);
        ePassword = findViewById(R.id.ePassword);
        btnDaftar = findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(view -> {
            String username = eUsername.getText().toString().trim();
            String password = ePassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harus diisi semua, tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, URL,
                    response -> {
                        try {
                            JSONObject objek = new JSONObject(response);

                            Toast.makeText(this, objek.getString("message"), Toast.LENGTH_SHORT).show();

                            if (objek.getString("status").equals("success")) {
                                finish();
                            }

                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });
    }
}