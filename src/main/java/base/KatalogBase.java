package base;

import java.util.List;

import interfaces.ISearchable;
import model.Produk;

public abstract class KatalogBase implements ISearchable {

    protected List<Produk> daftarProduk;

    public KatalogBase() {
        daftarProduk = new java.util.ArrayList<>();
    }

    public void tambahProduk(Produk produk) {
        daftarProduk.add(produk);
    }

    public void hapusProduk(String id) {
        daftarProduk.removeIf(produk -> produk.getId().equals(id));
    }

    public List<Produk> getSemuaProduk() {
        return daftarProduk;
    }
//
//    @Override
//    public abstract List<Produk> cariProduk(String nama);

    @Override
    public abstract Produk getProdukById(String id);
}
