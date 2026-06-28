package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import base.BaseEntity;
import enums.StatusExpired;

public class Produk extends BaseEntity {

    private String kodeBarcode;
    private String namaProduk;
    private String kategori;
    private double harga;
    private int stokSaatIni;
    private LocalDate tanggalExpired;
    private StatusExpired statusExpired;

    public Produk(String id, String kodeBarcode, String namaProduk,
                  String kategori, double harga, int stok, LocalDate tanggalExpired) {
        super(id);
        this.kodeBarcode    = kodeBarcode;
        this.namaProduk     = namaProduk;
        this.kategori       = kategori;
        this.harga          = harga;
        this.stokSaatIni    = stok;
        this.tanggalExpired = tanggalExpired;
        this.statusExpired  = StatusExpired.AKTIF;
    }

    public String getKodeBarcode()       { return kodeBarcode; }
    public String getNamaProduk()        { return namaProduk; }
    public String getKategori()          { return kategori; }
    public double getHarga()             { return harga; }
    public int getStokSaatIni()          { return stokSaatIni; }
    public LocalDate getTanggalExpired() { return tanggalExpired; }

    public void setTanggalExpired(LocalDate tanggalExpired) {
        this.tanggalExpired = tanggalExpired;
    }

    public void updateStok(int jumlah) {
        this.stokSaatIni += jumlah;
        if (this.stokSaatIni < 0) {
            this.stokSaatIni = 0;
        }
    }

    public StatusExpired cekStatusExpired() {
        if (statusExpired == StatusExpired.DITARIK) {
            return statusExpired;
        }

        long hariSisa = ChronoUnit.DAYS.between(LocalDate.now(), tanggalExpired);

        if (hariSisa <= 0) {
            statusExpired = StatusExpired.EXPIRED;
        } else if (hariSisa <= 90) {
            statusExpired = StatusExpired.MENDEKATI_EXPIRED;
        } else {
            statusExpired = StatusExpired.AKTIF;
        }

        return statusExpired;
    }

    @Override
    public boolean validate() {
        return kodeBarcode    != null && !kodeBarcode.isEmpty()
            && namaProduk     != null && !namaProduk.isEmpty()
            && harga          > 0
            && stokSaatIni    >= 0
            && tanggalExpired != null;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-15s | %-10s | Harga: Rp%-7.0f | Stok: %-3d | Exp: %-10s | Status: %s",
            getId(),
            namaProduk,
            kategori,
            harga,
            stokSaatIni,
            tanggalExpired,
            cekStatusExpired());
    }
}
