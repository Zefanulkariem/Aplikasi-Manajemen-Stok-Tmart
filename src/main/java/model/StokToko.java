package model;

import java.util.ArrayList;
import java.util.List;

import base.KatalogBase;

public class StokToko extends KatalogBase {

    private String namaToko;

    public StokToko(String namaToko) {
        super();
        this.namaToko = namaToko;
    }

    public String getNamaToko() {
        return namaToko;
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

    public void updateStokProduk(String id, int jumlah) {
        Produk produk = getProdukById(id);
        if (produk != null) {
            produk.updateStok(jumlah);
        }
    }

    public void updateTanggalExpired(String id, java.time.LocalDate tgl) {
        Produk produk = getProdukById(id);
        if (produk != null) {
            produk.setTanggalExpired(tgl);
        }
    }

    public List<Produk> getProdukExpired() {
        List<Produk> hasil = new ArrayList<>();
        for (Produk produk : daftarProduk) {
            if (produk.cekStatusExpired() != enums.StatusExpired.AKTIF
                    && produk.cekStatusExpired() != enums.StatusExpired.DITARIK) {
                hasil.add(produk);
            }
        }
        return hasil;
    }

    public int getTotalStok() {
        int total = 0;
        for (Produk produk : daftarProduk) {
            total += produk.getStokSaatIni();
        }
        return total;
    }

//    @Override
//    public boolean validate() {
//        return namaToko != null && !namaToko.isEmpty();
//    }
    
    

    @Override
    public String toString() {
        return "StokToko{" +
               "namaToko: '" + namaToko + '\'' +
               ", jumlahProduk: " + daftarProduk.size() +
               ", totalStok: " + getTotalStok() +
               '}';
    }
}
