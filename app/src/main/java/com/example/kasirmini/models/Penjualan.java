package com.example.kasirmini.models;

public class Penjualan {
    private String id;
    private String namaBarang;
    private double harga;
    private int jumlah;
    private double total;
    private String tanggal;
    private long timestamp;
    private String keterangan;

    public Penjualan() {
    }

    public Penjualan(String id, String namaBarang, double harga, int jumlah, double total, String tanggal, long timestamp, String keterangan) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.harga = harga;
        this.jumlah = jumlah;
        this.total = total;
        this.tanggal = tanggal;
        this.timestamp = timestamp;
        this.keterangan = keterangan;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}