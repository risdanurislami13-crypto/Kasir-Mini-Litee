package com.example.kasirmini.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kasirmini.R;
import com.example.kasirmini.activities.AddPengeluaranActivity;
import com.example.kasirmini.adapters.PengeluaranAdapter;
import com.example.kasirmini.models.Pengeluaran;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PengeluaranFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private TextView tvEmpty;
    private PengeluaranAdapter adapter;
    private List<Pengeluaran> pengeluaranList;
    private DatabaseReference mDatabase;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengeluaran, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pengeluaran").child(userId);

        initViews(view);
        setupRecyclerView();
        loadData();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAdd = view.findViewById(R.id.fabAdd);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPengeluaranActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        pengeluaranList = new ArrayList<>();
        adapter = new PengeluaranAdapter(getContext(), pengeluaranList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pengeluaranList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Pengeluaran pengeluaran = data.getValue(Pengeluaran.class);
                    if (pengeluaran != null) {
                        pengeluaranList.add(pengeluaran);
                    }
                }
                Collections.reverse(pengeluaranList);
                adapter.notifyDataSetChanged();

                if (pengeluaranList.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}