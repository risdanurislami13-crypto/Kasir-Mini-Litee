package com.example.kasirmini.models;

public class Pengeluaran {
    private String id;
    private String nama;
    private double jumlah;
    private String tanggal;
    private long timestamp;
    private String kategori;
    private String keterangan;

    public Pengeluaran() {
    }

    public Pengeluaran(String id, String nama, double jumlah, String tanggal, long timestamp, String kategori, String keterangan) {
        this.id = id;
        this.nama = nama;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.timestamp = timestamp;
        this.kategori = kategori;
        this.keterangan = keterangan;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}