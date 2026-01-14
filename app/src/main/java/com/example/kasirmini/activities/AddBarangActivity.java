package com.example.kasirmini.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kasirmini.R;
import com.example.kasirmini.models.Barang;
import com.example.kasirmini.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBarangActivity extends AppCompatActivity {
    private EditText etNama, etHarga, etStok, etKategori, etKeterangan;
    private Button btnSimpan;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("barang").child(userId);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etStok = findViewById(R.id.etStok);
        etKategori = findViewById(R.id.etKategori);
        etKeterangan = findViewById(R.id.etKeterangan);
        btnSimpan = findViewById(R.id.btnSimpan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Barang");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupListeners() {
        btnSimpan.setOnClickListener(v -> saveBarang());
    }

    private void saveBarang() {
        String nama = etNama.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String kategori = etKategori.getText().toString().trim();
        String keterangan = etKeterangan.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama barang diperlukan");
            return;
        }

        if (TextUtils.isEmpty(hargaStr)) {
            etHarga.setError("Harga diperlukan");
            return;
        }

        if (TextUtils.isEmpty(stokStr)) {
            etStok.setError("Stok diperlukan");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);

            String id = mDatabase.push().getKey();

            Barang barang = new Barang(id, nama, harga, stok, kategori, keterangan);

            if (id != null) {
                mDatabase.child(id).setValue(barang)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            NotificationHelper.showNotification(this, "Barang Ditambahkan", "Barang " + nama + " berhasil ditambahkan");
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Gagal menambahkan barang", Toast.LENGTH_SHORT).show();
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