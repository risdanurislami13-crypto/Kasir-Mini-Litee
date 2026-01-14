package com.example.kasirmini.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.kasirmini.R;
import com.example.kasirmini.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PengaturanFragment extends Fragment {
    private TextView tvEmail, tvUserId;
    private Button btnLogout;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);

        mAuth = FirebaseAuth.getInstance();

        initViews(view);
        displayUserInfo();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        tvEmail = view.findViewById(R.id.tvEmail);
        tvUserId = view.findViewById(R.id.tvUserId);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void displayUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvEmail.setText("Email: " + user.getEmail());
            tvUserId.setText("User ID: " + user.getUid());
        }
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> logout())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Berhasil logout", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}