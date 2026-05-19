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

import java.util.HashMap;
import java.util.Map;

public class TambahActivty extends AppCompatActivity {
    EditText nama, harga, stok;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_activty);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        nama = findViewById(R.id.nama);
        harga = findViewById(R.id.harga);
        stok = findViewById(R.id.stok);
        btnSimpan = findViewById(R.id.btnSimpan);

        String URL = "http://10.0.2.2/";

        btnSimpan.setOnClickListener(view -> {

            String n = nama.getText().toString().trim();
            String h = harga.getText().toString().trim();
            String s = stok.getText().toString().trim();

            if (n.isEmpty() || h.isEmpty() || s.isEmpty()) {
                Toast.makeText(
                        this,
                        "Semua field harus diisi",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    URL,

                    response -> {

                        Toast.makeText(
                                this,
                                "RESP: " + response,
                                Toast.LENGTH_SHORT
                        ).show();

                        if (response.contains("success")) {
                            Toast.makeText(
                                    this,
                                    "Berhasil ditambahkan",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Gagal ditambahkan",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    },

                    error -> {

                        Toast.makeText(
                                this,
                                "Error: " + error.toString(),
                                Toast.LENGTH_SHORT
                        ).show();

                        error.printStackTrace();

                    }

            ) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("nama_produk", n);
                    params.put("harga", h);
                    params.put("stok", s);

                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);

        });

    }

}