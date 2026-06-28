package model;

import java.time.LocalDate;

public class ItemKeranjang {

    private String idProduk;
    private String kodeBarcode;
    private String namaProduk;
    private int jumlah;
    private double hargaSatuan;
    private LocalDate tanggalExpired;

    public ItemKeranjang(String idProduk, String namaProduk, int jumlah,
                         double hargaSatuan, LocalDate tanggalExpired, String kodeBarcode) {
        this.idProduk      = idProduk;
        this.namaProduk    = namaProduk;
        this.jumlah        = jumlah;
        this.hargaSatuan   = hargaSatuan;
        this.tanggalExpired = tanggalExpired;
        this.kodeBarcode   = kodeBarcode;
    }

    public String getIdProduk()    { return idProduk; }
    public String getNamaProduk()  { return namaProduk; }
    public int getJumlah()         { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }
    public String getKodeBarcode() { return kodeBarcode; }
    public LocalDate getTanggalExpired() { return tanggalExpired; }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getSubTotal() {
        return jumlah * hargaSatuan;
    }

    @Override
    public String toString() {
        return "ID Produk    : " + idProduk +
               "\nNama Produk  : " + namaProduk +
               "\nJumlah       : " + jumlah +
               "\nKode Barcode : " + kodeBarcode +
               "\nHarga Satuan : Rp " + hargaSatuan +
               "\nTotal Harga  : Rp " + getSubTotal() +
               "\nTgl Expired  : " + tanggalExpired +
               "\n------------------------------";
    }
}
