package com.example.rusdimamang;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;

    Button btnLogin, btnRegister;

    String URL = "http://10.0.2.2/phpnativebulia/login.php";

    @Override
    protected void onStart() {
        super.onStart();
        boolean isLogin=getSharedPreferences("login", MODE_PRIVATE)
                .getBoolean("isLogin", false);

        if(isLogin) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
        btnLogin.setOnClickListener(view -> {
            String username =   etUsername.getText().toString().trim();
            String password =   etPassword.getText().toString().trim();

            if(username.isEmpty()||password.isEmpty()) {
                Toast.makeText(this, "Harus diisi semuanya gaboleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            StringRequest request=new StringRequest(
                    Request.Method.POST,
                    URL,
                    response->{
                        Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status=object.getString("Status");

                            if(status.equals("success")) {
                                getSharedPreferences("login", MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("isLogin", true).putString("username", username).apply();
                                Toast.makeText(this,"Login Berhasil",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            Toast.makeText(this,"JSON Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    },
                    error-> {
                        Toast.makeText(this,"Error Koneksi"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
            ) {
                @Override
                protected Map<String,String>getParams(){
                    Map<String,String>params=new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        });
    }
}