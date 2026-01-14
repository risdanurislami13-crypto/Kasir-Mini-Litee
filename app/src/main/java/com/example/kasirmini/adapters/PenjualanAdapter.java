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
import com.example.kasirmini.models.Penjualan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PenjualanAdapter extends RecyclerView.Adapter<PenjualanAdapter.ViewHolder> {
    private Context context;
    private List<Penjualan> penjualanList;
    private DatabaseReference mDatabase;

    public PenjualanAdapter(Context context, List<Penjualan> penjualanList) {
        this.context = context;
        this.penjualanList = penjualanList;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("penjualan").child(userId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_penjualan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Penjualan penjualan = penjualanList.get(position);

        holder.tvNamaBarang.setText(penjualan.getNamaBarang());
        holder.tvHarga.setText(formatRupiah(penjualan.getHarga()));
        holder.tvJumlah.setText("Qty: " + penjualan.getJumlah());
        holder.tvTotal.setText(formatRupiah(penjualan.getTotal()));
        holder.tvTanggal.setText(penjualan.getTanggal());
        holder.tvKeterangan.setText(penjualan.getKeterangan());

        holder.btnDelete.setOnClickListener(v -> showDeleteDialog(penjualan, position));
    }

    @Override
    public int getItemCount() {
        return penjualanList.size();
    }

    private void showDeleteDialog(Penjualan penjualan, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Hapus Penjualan")
                .setMessage("Apakah Anda yakin ingin menghapus penjualan ini?")
                .setPositiveButton("Ya", (dialog, which) -> deletePenjualan(penjualan, position))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deletePenjualan(Penjualan penjualan, int position) {
        mDatabase.child(penjualan.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Penjualan berhasil dihapus", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Gagal menghapus penjualan", Toast.LENGTH_SHORT).show();
                });
    }

    private String formatRupiah(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount).replace("Rp", "Rp ");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang, tvHarga, tvJumlah, tvTotal, tvTanggal, tvKeterangan;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvNamaBarang = itemView.findViewById(R.id.tvNamaBarang);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}