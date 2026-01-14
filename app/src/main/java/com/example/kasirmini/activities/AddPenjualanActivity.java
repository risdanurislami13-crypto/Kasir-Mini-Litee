package com.example.kasirmini.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kasirmini.R;
import com.example.kasirmini.models.Penjualan;
import com.example.kasirmini.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPenjualanActivity extends AppCompatActivity {
    private EditText etNamaBarang, etHarga, etJumlah, etKeterangan;
    private Button btnSimpan;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penjualan);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("penjualan").child(userId);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNamaBarang = findViewById(R.id.etNamaBarang);
        etHarga = findViewById(R.id.etHarga);
        etJumlah = findViewById(R.id.etJumlah);
        etKeterangan = findViewById(R.id.etKeterangan);
        btnSimpan = findViewById(R.id.btnSimpan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Penjualan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupListeners() {
        btnSimpan.setOnClickListener(v -> savePenjualan());
    }

    private void savePenjualan() {
        String namaBarang = etNamaBarang.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String jumlahStr = etJumlah.getText().toString().trim();
        String keterangan = etKeterangan.getText().toString().trim();

        if (TextUtils.isEmpty(namaBarang)) {
            etNamaBarang.setError("Nama barang diperlukan");
            return;
        }

        if (TextUtils.isEmpty(hargaStr)) {
            etHarga.setError("Harga diperlukan");
            return;
        }

        if (TextUtils.isEmpty(jumlahStr)) {
            etJumlah.setError("Jumlah diperlukan");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            int jumlah = Integer.parseInt(jumlahStr);
            double total = harga * jumlah;

            String id = mDatabase.push().getKey();
            String tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            long timestamp = System.currentTimeMillis();

            Penjualan penjualan = new Penjualan(id, namaBarang, harga, jumlah, total, tanggal, timestamp, keterangan);

            if (id != null) {
                mDatabase.child(id).setValue(penjualan)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Penjualan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            NotificationHelper.showNotification(this, "Penjualan Ditambahkan", "Penjualan " + namaBarang + " berhasil ditambahkan");
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Gagal menambahkan penjualan", Toast.LENGTH_SHORT).show();
                        });
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}