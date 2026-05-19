package com.example.rusdimamang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView listProduk;
    ArrayAdapter<String> adapter;

    Button btnTambah, btnLogout;

    String URL = "http://10.0.2.2/phpnativebulia/tampil.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listProduk = findViewById(R.id.listProduk);
        btnTambah = findViewById(R.id.btnTambah);
        btnLogout = findViewById(R.id.btnLogout);

        btnTambah.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, TambahActivty.class));
        });

        btnLogout.setOnClickListener(view -> {
            getSharedPreferences("login", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        String clean = response.trim();

                        if (!clean.startsWith("[")) {
                            Toast.makeText(this, "Response bukan JSON Array", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray array = new JSONArray(clean);
                        ArrayList<String> tampil = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject objek = array.getJSONObject(i);

                            String nama = objek.getString("nama");
                            String harga = objek.getString("harga");
                            String stok = objek.getString("stok");

                            tampil.add(nama + " | Rp " + harga + " | Stok: " + stok);
                        }

                        adapter = new ArrayAdapter<>(
                                HomeActivity.this,
                                android.R.layout.simple_list_item_1,
                                tampil
                        );

                        listProduk.setAdapter(adapter);

                    } catch (Exception e) {
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error: " + error.toString(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}