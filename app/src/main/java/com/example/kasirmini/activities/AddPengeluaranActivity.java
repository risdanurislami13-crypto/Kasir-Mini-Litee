package com.example.kasirmini.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kasirmini.R;
import com.example.kasirmini.models.Pengeluaran;
import com.example.kasirmini.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPengeluaranActivity extends AppCompatActivity {
    private EditText etNama, etJumlah, etKategori, etKeterangan;
    private Button btnSimpan;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pengeluaran);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pengeluaran").child(userId);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNama = findViewById(R.id.etNama);
        etJumlah = findViewById(R.id.etJumlah);
        etKategori = findViewById(R.id.etKategori);
        etKeterangan = findViewById(R.id.etKeterangan);
        btnSimpan = findViewById(R.id.btnSimpan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Pengeluaran");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupListeners() {
        btnSimpan.setOnClickListener(v -> savePengeluaran());
    }

    private void savePengeluaran() {
        String nama = etNama.getText().toString().trim();
        String jumlahStr = etJumlah.getText().toString().trim();
        String kategori = etKategori.getText().toString().trim();
        String keterangan = etKeterangan.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama pengeluaran diperlukan");
            return;
        }

        if (TextUtils.isEmpty(jumlahStr)) {
            etJumlah.setError("Jumlah diperlukan");
            return;
        }

        try {
            double jumlah = Double.parseDouble(jumlahStr);

            String id = mDatabase.push().getKey();
            String tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            long timestamp = System.currentTimeMillis();

            Pengeluaran pengeluaran = new Pengeluaran(id, nama, jumlah, tanggal, timestamp, kategori, keterangan);

            if (id != null) {
                mDatabase.child(id).setValue(pengeluaran)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Pengeluaran berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            NotificationHelper.showNotification(this, "Pengeluaran Ditambahkan", "Pengeluaran " + nama + " berhasil ditambahkan");
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Gagal menambahkan pengeluaran", Toast.LENGTH_SHORT).show();
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