package model;

import java.util.ArrayList;
import java.util.List;

import base.BaseEntity;
import enums.StatusExpired;
import exception.ProdukExpiredException;

public class Keranjang extends BaseEntity {

    private String idKaryawan;
    private List<ItemKeranjang> items;

    public Keranjang(String idKaryawan) {
        super("KRJ-" + System.currentTimeMillis());
        this.idKaryawan = idKaryawan;
        this.items = new ArrayList<>();
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    /**
     * Menambahkan produk ke keranjang.
     * Melempar ProdukExpiredException jika produk sudah expired.
     * Menampilkan peringatan jika produk mendekati expired.
     * Jika produk sudah ada di keranjang, jumlahnya akan ditambah.
     */
    public void tambahItem(Produk produk, int qty) {
        StatusExpired status = produk.cekStatusExpired();

        if (status == StatusExpired.EXPIRED) {
            throw new ProdukExpiredException(
                "[ERROR] Produk " + produk.getNamaProduk() +
                " sudah expired dan tidak bisa ditambahkan ke keranjang."
            );
        }

        if (status == StatusExpired.MENDEKATI_EXPIRED) {
            System.out.println("[WARNING] Produk " + produk.getNamaProduk() +
                               " mendekati expired.");
        }

        for (ItemKeranjang item : items) {
            if (item.getIdProduk().equals(produk.getId())) {
                item.setJumlah(item.getJumlah() + qty);
                return;
            }
        }

        items.add(new ItemKeranjang(
            produk.getId(),
            produk.getNamaProduk(),
            qty,
            produk.getHarga(),
            produk.getTanggalExpired(),
            produk.getKodeBarcode()
        ));
    }

    public void hapusItem(String idProduk) {
        items.removeIf(item -> item.getIdProduk().equals(idProduk));
    }

    public void kosongkan() {
        items.clear();
    }

    public double hitungTotal() {
        double total = 0;
        for (ItemKeranjang item : items) {
            total += item.getSubTotal();
        }
        return total;
    }

    @Override
    public boolean validate() {
        return idKaryawan != null && !idKaryawan.isEmpty() && items != null;
    }

    @Override
    public String toString() {
        return "Keranjang{" +
               "idKaryawan: '" + idKaryawan + '\'' +
               ", jumlahItem: " + items.size() +
               ", total: Rp " + hitungTotal() +
               '}';
    }
}
