package com.example.rusdimamang;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    ListView listProduk;
    Button btnTambah, btnLogout;

    ArrayList<Produk> list = new ArrayList<>();

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
                        list.clear();

                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            list.add(new Produk(
                                    obj.getString("id"),
                                    obj.getString("nama"),
                                    obj.getString("harga"),
                                    obj.getString("stok")
                            ));
                        }

                        listProduk.setAdapter(new ProdukAdapter());

                    } catch (Exception e) {
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error: " + error.toString(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    class ProdukAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(HomeActivity.this)
                        .inflate(R.layout.item_produk, parent, false);
            }

            TextView txtProduk = view.findViewById(R.id.txtProduk);
            ImageButton btnEdit = view.findViewById(R.id.btnEdit);
            ImageButton btnHapus = view.findViewById(R.id.btnHapus);

            Produk p = list.get(position);

            txtProduk.setText(
                    p.getNama() +
                            "\nRp " + p.getHarga() +
                            "\nStok: " + p.getStok()
            );

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, EditActivity.class);

                intent.putExtra("id", p.getId());
                intent.putExtra("nama", p.getNama());
                intent.putExtra("harga", p.getHarga());
                intent.putExtra("stok", p.getStok());

                startActivity(intent);
            });

            btnHapus.setOnClickListener(v -> {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Hapus")
                        .setMessage("Yakin ingin dihapus?")
                        .setPositiveButton("Yakin", (dialog, which) -> {
                            hapusData(p.getId());
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            });

            return view;
        }
    }

    private void hapusData(String id) {

        String urlHapus = "http://10.0.2.2/phpnativebulia/hapus.php";

        StringRequest request = new StringRequest(Request.Method.POST, urlHapus,
                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    loadData();
                },
                error -> Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}