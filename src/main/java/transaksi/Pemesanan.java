package transaksi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.BaseEntity;
import enums.StatusPemesanan;
import exception.StokTidakCukupException;
import model.ItemKeranjang;
import model.KatalogSupplier;
import model.Keranjang;
import model.Produk;

public class Pemesanan extends BaseEntity {

    private String nomorNota;
    private LocalDate tanggal;
    private String alamatPengiriman;
    private StatusPemesanan statusPemesanan;
    private List<ItemPemesanan> items;

    public Pemesanan(String id, String nomorNota, LocalDate tanggal, String alamatPengiriman) {
        super(id);
        this.nomorNota        = nomorNota;
        this.tanggal          = tanggal;
        this.alamatPengiriman = alamatPengiriman;
        this.statusPemesanan  = StatusPemesanan.MENUNGGU;
        this.items            = new ArrayList<>();
    }

    public String getNomorNota()               { return nomorNota; }
    public LocalDate getTanggal()              { return tanggal; }
    public StatusPemesanan getStatusPemesanan(){ return statusPemesanan; }
    public List<ItemPemesanan> getItems()      { return items; }

    public void setStatusPemesanan(StatusPemesanan statusPemesanan) {
        this.statusPemesanan = statusPemesanan;
    }

    public void tambahItem(ItemPemesanan item) {
        items.add(item);
    }

    /**
     * Mengisi item pemesanan dari keranjang belanja.
     */
    public void buatDariKeranjang(Keranjang keranjang) {
        for (ItemKeranjang itemKeranjang : keranjang.getItems()) {
            tambahItem(new ItemPemesanan(
                itemKeranjang.getIdProduk(),
                itemKeranjang.getNamaProduk(),
                itemKeranjang.getJumlah(),
                itemKeranjang.getHargaSatuan(),
                itemKeranjang.getKodeBarcode()
            ));
        }
    }

    /**
     * Mengecek ketersediaan stok supplier untuk semua item.
     * Jika stok cukup, status berubah menjadi DIKIRIM dan stok supplier dikurangi.
     * Jika stok tidak cukup, melempar StokTidakCukupException.
     */
    public void cekKetersediaan(KatalogSupplier katalog) {
        for (ItemPemesanan item : items) {
            Produk produk = katalog.getProdukById(item.getIdProduk());
            if (produk == null || produk.getStokSaatIni() < item.getJumlahPesan()) {
                throw new StokTidakCukupException(
                    "[ERROR] Stok supplier tidak mencukupi untuk produk: " + item.getNamaProduk()
                );
            }
        }

        // Semua stok cukup — kurangi stok supplier dan ubah status
        for (ItemPemesanan item : items) {
            katalog.kurangiStokSupplier(item.getIdProduk(), item.getJumlahPesan());
        }
        this.statusPemesanan = StatusPemesanan.DIKIRIM;
    }

    /**
     * Membatalkan pemesanan dan mengembalikan stok supplier.
     */
    public void batalkanPemesanan(KatalogSupplier katalog) {
        this.statusPemesanan = StatusPemesanan.DIBATALKAN;
        for (ItemPemesanan item : items) {
            Produk produk = katalog.getProdukById(item.getIdProduk());
            if (produk != null) {
                produk.updateStok(item.getJumlahPesan());
            }
        }
    }

    /**
     * Mengecek apakah semua item dalam pemesanan sudah diterima lengkap.
     */
    public boolean semuaItemSudahDiterima(Map<String, Integer> jumlahDiterima) {
        for (ItemPemesanan item : items) {
            String kunci = nomorNota + "|" + item.getIdProduk();
            int totalDiterima = jumlahDiterima.getOrDefault(kunci, 0);
            if (totalDiterima < item.getJumlahPesan()) {
                return false;
            }
        }
        return true;
    }

    public String hitungTotalPemesanan() {
        double total = 0;
        for (ItemPemesanan item : items) {
            total += item.getSubtotal();
        }
        return "Rp " + total;
    }

    @Override
    public boolean validate() {
        return nomorNota        != null && !nomorNota.isEmpty()
            && tanggal          != null
            && alamatPengiriman != null && !alamatPengiriman.isEmpty()
            && items            != null;
    }

    @Override
    public String toString() {
        return "ID            : " + getId() +
               "\nNomor Nota    : " + nomorNota +
               "\nTanggal       : " + tanggal +
               "\nAlamat        : " + alamatPengiriman +
               "\nStatus        : " + statusPemesanan +
               "\nTotal         : " + hitungTotalPemesanan();
    }
}
