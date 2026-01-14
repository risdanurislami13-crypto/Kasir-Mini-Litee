package com.example.kasirmini.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.kasirmini.R;
import com.example.kasirmini.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView tvWelcome, tvTotalPenjualan, tvTotalPengeluaran, tvLaba;
    private CardView cardLaporan, cardPengaturan;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;

    private double totalPenjualan = 0;
    private double totalPengeluaran = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return view;
        }

        userId = currentUser.getUid();

        initViews(view);
        loadData();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvTotalPenjualan = view.findViewById(R.id.tvTotalPenjualan);
        tvTotalPengeluaran = view.findViewById(R.id.tvTotalPengeluaran);
        tvLaba = view.findViewById(R.id.tvLaba);

        cardLaporan = view.findViewById(R.id.cardLaporan);


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            tvWelcome.setText("Selamat Datang, " +
                    (email != null ? email.split("@")[0] : "User"));
        }
    }

    private void loadData() {
        // ===== TOTAL PENJUALAN =====
        mDatabase.child("penjualan").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        totalPenjualan = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            double total = parseTotalValue(data);
                            totalPenjualan += total;
                        }
                        tvTotalPenjualan.setText(formatRupiah(totalPenjualan));
                        updateLaba();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),
                                "Gagal memuat penjualan",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // ===== TOTAL PENGELUARAN =====
        mDatabase.child("pengeluaran").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        totalPengeluaran = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            double total = parseTotalValue(data);
                            totalPengeluaran += total;
                        }
                        tvTotalPengeluaran.setText(formatRupiah(totalPengeluaran));
                        updateLaba();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(),
                                "Gagal memuat pengeluaran",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Parse value total dari DataSnapshot.
     * Mendukung Number (Double/Long) dan String.
     */
    private double parseTotalValue(DataSnapshot data) {
        double total = 0;

        // Coba ambil sebagai Double
        Double totalDouble = data.child("total").getValue(Double.class);
        if (totalDouble != null) {
            total = totalDouble;
        } else {
            // Jika tersimpan sebagai Long
            Long totalLong = data.child("total").getValue(Long.class);
            if (totalLong != null) {
                total = totalLong.doubleValue();
            } else {
                // Jika tersimpan sebagai String
                String totalStr = data.child("total").getValue(String.class);
                if (totalStr != null) {
                    try {
                        total = Double.parseDouble(totalStr);
                    } catch (NumberFormatException e) {
                        total = 0;
                    }
                }
            }
        }

        return total;
    }

    private void updateLaba() {
        double laba = totalPenjualan - totalPengeluaran;
        tvLaba.setText(formatRupiah(laba));
    }

    private String formatRupiah(double value) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        return format.format(value);
    }

    private void setupListeners() {
        if (cardLaporan != null) {
            cardLaporan.setOnClickListener(v ->
                    Toast.makeText(getContext(),
                            "Menu Laporan", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardPengaturan != null) {
            cardPengaturan.setOnClickListener(v ->
                    Toast.makeText(getContext(),
                            "Menu Pengaturan", Toast.LENGTH_SHORT).show()
            );
        }
    }
}
