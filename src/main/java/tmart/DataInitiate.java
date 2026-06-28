package tmart;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

import model.Karyawan;
import model.KatalogSupplier;
import model.Keranjang;
import model.StokToko;
import model.Produk;

public class DataInitiate {

    public static Karyawan karyawan;
    public static KatalogSupplier katalogSupplier;
    public static Keranjang keranjang;
    public static StokToko stokToko;

    public static Map<String, Integer> jumlahDiterimaPerItem = new HashMap<>();
    public static Map<String, String> barangKurang = new HashMap<>();

    public static void init() {

       karyawan = new Karyawan(
            "T001", "Budi", "Budi01@gmail.com",
            "budi01", "admin123", "0811-0000-9999",
            "Bandung", "TMart"
        );

        // DATA PRODUK SUPPLIER
        katalogSupplier = new KatalogSupplier("Supplier");
        katalogSupplier.tambahProduk(new Produk("P001", "SS1",  "Susu",          "Minuman", 10000, 50, LocalDate.of(2026, 12, 30)));
        katalogSupplier.tambahProduk(new Produk("P002", "SR2",  "Sereal",        "Makanan", 50000, 30, LocalDate.of(2026,  6, 20)));
        katalogSupplier.tambahProduk(new Produk("P003", "SR3",  "Malkist",        "Makanan", 30000, 30, LocalDate.of(2026,  6, 20)));
        katalogSupplier.tambahProduk(new Produk("P004", "RO4",  "Roti Tawar",    "Makanan", 18000, 40, LocalDate.of(2026,  7, 15)));
        katalogSupplier.tambahProduk(new Produk("P005", "MY5",  "Minyak Goreng", "Sembako", 28000, 25, LocalDate.of(2027,  1, 10)));
        katalogSupplier.tambahProduk(new Produk("P006", "BR6",  "Beras 5Kg",     "Sembako", 65000, 20, LocalDate.of(2027,  5, 25)));
        katalogSupplier.tambahProduk(new Produk("P007", "GL7",  "Gula Pasir",    "Sembako", 17000, 35, LocalDate.of(2027,  2, 18)));
        katalogSupplier.tambahProduk(new Produk("P008", "TE8",  "Teh Kotak",     "Minuman",  6000, 60, LocalDate.of(2026,  9, 12)));
        katalogSupplier.tambahProduk(new Produk("P009", "KP9",  "Kopi Sachet",   "Minuman",  2500,100, LocalDate.of(2027,  3,  8)));
        katalogSupplier.tambahProduk(new Produk("P010", "SN10", "Snack Kentang", "Makanan", 12000, 45, LocalDate.of(2026,  8, 30)));

        // DATA KERANJANG
        keranjang = new Keranjang(karyawan.getId());

        // DATA STOK TOKO
        stokToko = new StokToko("T-Mart");
        stokToko.tambahProduk(new Produk("G001", "GBA001", "Nissin Sagu Keju", "Makanan", 14000, 80, LocalDate.of(2027, 12, 20)));

    }
}
