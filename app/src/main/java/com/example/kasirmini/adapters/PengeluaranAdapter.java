package com.example.kasirmini.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kasirmini.R;
import com.example.kasirmini.models.Pengeluaran;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {
    private Context context;
    private List<Pengeluaran> pengeluaranList;
    private DatabaseReference mDatabase;

    public PengeluaranAdapter(Context context, List<Pengeluaran> pengeluaranList) {
        this.context = context;
        this.pengeluaranList = pengeluaranList;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pengeluaran").child(userId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pengeluaran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pengeluaran pengeluaran = pengeluaranList.get(position);

        holder.tvNama.setText(pengeluaran.getNama());
        holder.tvJumlah.setText(formatRupiah(pengeluaran.getJumlah()));
        holder.tvKategori.setText(pengeluaran.getKategori());
        holder.tvTanggal.setText(pengeluaran.getTanggal());
        holder.tvKeterangan.setText(pengeluaran.getKeterangan());

        holder.btnDelete.setOnClickListener(v -> showDeleteDialog(pengeluaran, position));
    }

    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }

    private void showDeleteDialog(Pengeluaran pengeluaran, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Hapus Pengeluaran")
                .setMessage("Apakah Anda yakin ingin menghapus pengeluaran ini?")
                .setPositiveButton("Ya", (dialog, which) -> deletePengeluaran(pengeluaran, position))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deletePengeluaran(Pengeluaran pengeluaran, int position) {
        mDatabase.child(pengeluaran.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Pengeluaran berhasil dihapus", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Gagal menghapus pengeluaran", Toast.LENGTH_SHORT).show();
                });
    }

    private String formatRupiah(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount).replace("Rp", "Rp ");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvJumlah, tvKategori, tvTanggal, tvKeterangan;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}