package interfaces;

import java.util.List;

import model.Produk;

public interface ISearchable {
//    public List<Produk> cariProduk(String keyword);
    public Produk getProdukById(String id);
}
