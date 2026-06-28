package model;

import java.util.ArrayList;
import java.util.List;

import base.KatalogBase;

public class KatalogSupplier extends KatalogBase {

    private String namaSupplier;

    public KatalogSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

//    @Override
//    public List<Produk> cariProduk(String keyword) {
//        List<Produk> hasil = new ArrayList<>();
//        for (Produk produk : daftarProduk) {
//            if (produk.getNamaProduk().toLowerCase().contains(keyword.toLowerCase())) {
//                hasil.add(produk);
//            }
//        }
//        return hasil;
//    }

    @Override
    public Produk getProdukById(String id) {
        for (Produk produk : daftarProduk) {
            if (produk.getId().equals(id)) {
                return produk;
            }
        }
        return null;
    }

    public void kurangiStokSupplier(String id, int jumlah) {
        Produk produk = getProdukById(id);
        if (produk != null) {
            produk.updateStok(-jumlah);
        }
    }

//    @Override
//    public boolean validate() {
//        return namaSupplier != null && !namaSupplier.isEmpty() && daftarProduk != null;
//    }

    @Override
    public String toString() {
        return "KatalogSupplier{" +
               "namaSupplier: '" + namaSupplier + '\'' +
               ", jumlahProduk: " + daftarProduk.size() +
               '}';
    }
}
