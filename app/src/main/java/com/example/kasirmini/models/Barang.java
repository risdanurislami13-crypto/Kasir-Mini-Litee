package com.example.kasirmini.models;

public class Barang {
    private String id;
    private String nama;
    private double harga;
    private int stok;
    private String kategori;
    private String keterangan;

    public Barang() {
    }

    public Barang(String id, String nama, double harga, int stok, String kategori, String keterangan) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
        this.keterangan = keterangan;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}