package com.example.rusdimamang;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.toolbox.StringRequest;

public class EditActivity extends AppCompatActivity {
    EditText nama, harga, stok;
    Button btnUpdate;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nama=findViewById(R.id.nama);
        harga=findViewById(R.id.harga);
        stok=findViewById(R.id.stok);
        btnUpdate=findViewById(R.id.btnUpdate);

        id = getIntent().getStringExtra("id");

        nama.setText(getIntent().getStringExtra("nama"));
        harga.setText(getIntent().getStringExtra("harga"));
        stok.setText(getIntent().getStringExtra("stok"));

        btnUpdate.setOnclickList(view -> {
            StringRequest request = new StringRequest(
                    Request.Method.POST,"http://10.0.2.2/phpnativebulia/edit.php",

                    response->{
                        Toast.makeText(this, response,Toast.LENGTH_SHORT).show();
                        finish();
                    }
            )
        })
    }
}