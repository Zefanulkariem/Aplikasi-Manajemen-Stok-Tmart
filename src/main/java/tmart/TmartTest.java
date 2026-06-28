package tmart;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import enums.KategoriRetur;
import enums.StatusExpired;
import enums.StatusPemesanan;
import exception.InputTidakValidException;
import exception.PemesananNotFoundException;
import exception.PemesananTidakBisaDibatalkanException;
import exception.ProdukExpiredException;
import exception.ProdukNotFoundException;
import exception.StokTidakCukupException;
import laporan.ItemRetur;
import laporan.LaporanRetur;
import model.ItemKeranjang;
import model.Karyawan;
import model.KatalogSupplier;
import model.Keranjang;
import model.Produk;
import model.StokToko;
import transaksi.HasilScanBarcode;
import transaksi.ItemPemesanan;
import transaksi.Pemesanan;
import transaksi.ScanBarcode;

public class TmartTest {

    static Scanner in = new Scanner(System.in);

    // =========================================================
    // METHOD VALIDASI INPUT
    // =========================================================

    public static String inputTeks(String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = in.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("[ERROR] Input tidak boleh kosong.");
        }
    }

    public static int inputAngka(String pesan, int minimal, int maksimal) {
        while (true) {
            try {
                System.out.print(pesan);
                String raw = in.nextLine().trim();

                // Lempar exception jika bukan angka
                int angka;
                try {
                    angka = Integer.parseInt(raw);
                } catch (NumberFormatException e) {
                    throw new InputTidakValidException(
                        "[ERROR] \"" + raw + "\" bukan angka. Masukkan angka antara "
                        + minimal + " sampai " + maksimal + "."
                    );
                }

                if (angka < minimal || angka > maksimal) {
                    throw new InputTidakValidException(
                        "[ERROR] Angka harus antara " + minimal + " sampai " + maksimal + "."
                    );
                }

                return angka;

            } catch (InputTidakValidException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String inputYaTidak(String pesan) {
        while (true) {
            System.out.print(pesan);
            String jawaban = in.nextLine().trim();
            if (jawaban.equalsIgnoreCase("ya") || jawaban.equalsIgnoreCase("tidak")) {
                return jawaban;
            }
            System.out.println("[ERROR] Masukkan hanya 'ya' atau 'tidak'.");
        }
    }

    public static LocalDate inputTanggal(String pesan) {
        while (true) {
            try {
                System.out.print(pesan);
                return LocalDate.parse(in.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("[ERROR] Format tanggal harus yyyy-mm-dd. Contoh: 2026-06-20");
            }
        }
    }

    public static void next() {
        System.out.println("\nTekan enter untuk melanjutkan..........");
        in.nextLine();
    }

    // =========================================================
    // METHOD MENU
    // =========================================================

    public static void menuLogin() {
        System.out.println("==============================================");
        System.out.println("          Selamat Datang di TMart!");
        System.out.println(" Aplikasi manajemen toko untuk karyawan TMart.");
        System.out.println("==============================================");
        System.out.println("1. Login dengan akun yang sudah ada");
        System.out.println("2. Keluar");
        System.out.println("==============================================");
    }

    public static void menuKaryawan() {
        System.out.println("\n==============================================");
        System.out.println("              MENU KARYAWAN");
        System.out.println("==============================================");
        System.out.println("1. Lihat Semua Produk");
        System.out.println("2. Lihat Keranjang");
        System.out.println("3. Lihat Riwayat Pemesanan");
        System.out.println("4. Pindai Kode Barcode Penerimaan");
        System.out.println("5. Lihat Stok Toko dan Retur Barang");
        System.out.println("6. Batalkan Pemesanan");
        System.out.println("7. Profil");
        System.out.println("8. Lihat Barang Kurang");
        System.out.println("9. Lihat Barang Retur");
        System.out.println("0. Keluar");
        System.out.println("==============================================");
    }

    // =========================================================
    // METHOD PENCARIAN
    // =========================================================

    /**
     * Mencari pemesanan berdasarkan nomor nota.
     * Melempar PemesananNotFoundException jika tidak ditemukan.
     */
    public static Pemesanan cariPemesanan(List<Pemesanan> daftarPemesanan, String nomorNota) {
        for (Pemesanan pemesanan : daftarPemesanan) {
            if (pemesanan.getNomorNota().equalsIgnoreCase(nomorNota)) {
                return pemesanan;
            }
        }
        throw new PemesananNotFoundException(
            "[ERROR] Nomor nota \"" + nomorNota + "\" tidak ditemukan."
        );
    }

    /**
     * Mencari item pemesanan berdasarkan kode barcode.
     * Melempar ProdukNotFoundException jika tidak ditemukan.
     */
    public static ItemPemesanan cariItemBarcode(Pemesanan pemesanan, String barcode) {
        for (ItemPemesanan item : pemesanan.getItems()) {
            if (item.getKodeBarcode().equalsIgnoreCase(barcode)) {
                return item;
            }
        }
        throw new ProdukNotFoundException(
            "[ERROR] Barcode \"" + barcode + "\" tidak ada pada nota ini."
        );
    }

    /**
     * Membuat kunci unik untuk melacak penerimaan per item per nota.
     * Format: nomorNota|idProduk
     */
    public static String buatKunciPenerimaan(Pemesanan pemesanan, ItemPemesanan item) {
        return pemesanan.getNomorNota() + "|" + item.getIdProduk();
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        // DATA KARYAWAN
        Karyawan karyawan = new Karyawan(
            "T001", "Budi", "Budi01@gmail.com",
            "budi01", "admin123", "0811-0000-9999",
            "Bandung", "TMart"
        );

        // DATA PRODUK SUPPLIER
        KatalogSupplier katalogSupplier = new KatalogSupplier("Supplier");
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
        Keranjang keranjang = new Keranjang(karyawan.getId());

        // DATA STOK TOKO
        StokToko stokToko = new StokToko("T-Mart");
        stokToko.tambahProduk(new Produk("G001", "GBA001", "Nissin Sagu Keju", "Makanan", 14000, 80, LocalDate.of(2027, 12, 20)));

        /*
         * Menyimpan jumlah barang yang sudah diterima per item per nota.
         * Kunci: nomorNota|idProduk
         */
        Map<String, Integer> jumlahDiterimaPerItem = new HashMap<>();

        /*
         * Barang kurang menggunakan Map agar dapat diperbarui
         * ketika barang susulan datang.
         */
        Map<String, String> daftarBarangKurang = new HashMap<>();

        boolean aplikasiAktif = true;

        while (aplikasiAktif) {

            menuLogin();
            int pilihLogin = inputAngka("Silakan pilih menu: ", 1, 2);

            if (pilihLogin == 2) {
                System.out.println("Terima kasih sudah menggunakan T-Mart!");
                break;
            }

            String username = inputTeks("Masukkan Username: ");
            String password = inputTeks("Masukkan Password: ");

            if (!karyawan.login(username, password)) {
                continue;
            }

            boolean login = true;

            while (login) {

                menuKaryawan();
                int menu = inputAngka("Silakan pilih menu: ", 0, 9);

                switch (menu) {

                    // CASE 1: PRODUK SUPPLIER DAN KERANJANG
                    case 1: {
                        boolean tambahProduk = true;
                        while (tambahProduk) {
                            System.out.println("\n========== DAFTAR PRODUK SUPPLIER ==========");
                            for (Produk produk : katalogSupplier.getSemuaProduk()) {
                                System.out.println(produk);
                            }

                            String jawab = inputYaTidak("\nIngin menambahkan produk ke keranjang? (ya/tidak): ");
                            if (jawab.equalsIgnoreCase("tidak")) {
                                tambahProduk = false;
                                continue;
                            }

                            try {
                                String idProduk = inputTeks("Masukkan ID Produk: ");
                                Produk produk = katalogSupplier.getProdukById(idProduk);

                                if (produk == null) {
                                    throw new ProdukNotFoundException(
                                        "[ERROR] Produk dengan ID \"" + idProduk + "\" tidak ditemukan."
                                    );
                                }

                                int jumlahDiKeranjang = 0;
                                for (ItemKeranjang item : keranjang.getItems()) {
                                    if (item.getIdProduk().equalsIgnoreCase(produk.getId())) {
                                        jumlahDiKeranjang = item.getJumlah();
                                        break;
                                    }
                                }

                                int stokTersedia = produk.getStokSaatIni() - jumlahDiKeranjang;

                                System.out.println("Stok supplier          : " + produk.getStokSaatIni());
                                System.out.println("Sudah di keranjang     : " + jumlahDiKeranjang);
                                System.out.println("Sisa bisa ditambahkan  : " + stokTersedia);

                                if (stokTersedia <= 0) {
                                    throw new StokTidakCukupException(
                                        "[ERROR] Seluruh stok sudah dimasukkan ke keranjang."
                                    );
                                }

                                int jumlah = inputAngka("Masukkan jumlah produk: ", 1, stokTersedia);

                                // tambahItem sekarang cek expired di dalam Keranjang
                                keranjang.tambahItem(produk, jumlah);

                                System.out.println("\n[SUKSES] Produk " + produk.getNamaProduk()
                                    + " sebanyak " + jumlah + " berhasil ditambahkan.");

                            } catch (ProdukNotFoundException | StokTidakCukupException | ProdukExpiredException e) {
                                System.out.println(e.getMessage());
                            }

                            String tambahLagi = inputYaTidak("Tambahkan produk lain? (ya/tidak): ");
                            tambahProduk = tambahLagi.equalsIgnoreCase("ya");
                        }
                        next();
                        break;
                    }

                    // CASE 2: KERANJANG DAN PEMESANAN
                    case 2: {
                        if (keranjang.getItems().isEmpty()) {
                            System.out.println("[INFO] Keranjang masih kosong.");
                            next();
                            break;
                        }

                        System.out.println("\n=========== ISI KERANJANG ===========");
                        int nomor = 1;
                        for (ItemKeranjang item : keranjang.getItems()) {
                            System.out.println("\n------------------------------");
                            System.out.println("     Item ke-" + nomor);
                            System.out.println("------------------------------");
                            System.out.println(item);
                            nomor++;
                        }

                        System.out.println("\n1. Pesan satu barang");
                        System.out.println("2. Pesan semua barang");
                        System.out.println("3. Hapus barang");
                        System.out.println("0. Kembali");

                        int pilihanKeranjang = inputAngka("Pilih menu keranjang: ", 0, 3);

                        if (pilihanKeranjang == 0) break;

                        if (pilihanKeranjang == 3) {
                            int nomorItem = inputAngka("Masukkan nomor item: ", 1, keranjang.getItems().size());
                            ItemKeranjang item = keranjang.getItems().get(nomorItem - 1);
                            keranjang.hapusItem(item.getIdProduk());
                            System.out.println("[SUKSES] Item berhasil dihapus.");
                            next();
                            break;
                        }

                        String alamat = inputTeks("Masukkan alamat pengiriman: ");

                        String idPemesanan = "S" + String.format("%03d", karyawan.getDaftarPemesanan().size() + 1);
                        String nomorNota   = "NOTA" + String.format("%03d", karyawan.getDaftarPemesanan().size() + 1);

                        Pemesanan pemesanan = new Pemesanan(idPemesanan, nomorNota, LocalDate.now(), alamat);

                        int nomorItemDipilih = -1;

                        if (pilihanKeranjang == 1) {
                            nomorItemDipilih = inputAngka("Masukkan nomor item: ", 1, keranjang.getItems().size());
                            ItemKeranjang item = keranjang.getItems().get(nomorItemDipilih - 1);
                            pemesanan.tambahItem(new ItemPemesanan(
                                item.getIdProduk(), item.getNamaProduk(),
                                item.getJumlah(), item.getHargaSatuan(), item.getKodeBarcode()
                            ));
                        } else {
                            // Pakai buatDariKeranjang() — method sudah ada di Pemesanan
                            pemesanan.buatDariKeranjang(keranjang);
                        }

                        if (!pemesanan.validate()) {
                            System.out.println("[ERROR] Pemesanan tidak valid.");
                            next();
                            break;
                        }

                        try {
                            // cekKetersediaan sekarang sekalian mengurangi stok supplier
                            pemesanan.cekKetersediaan(katalogSupplier);
                        } catch (StokTidakCukupException e) {
                            System.out.println(e.getMessage());
                            System.out.println("[INFO] Barang tetap berada di keranjang.");
                            next();
                            break;
                        }

                        // Simpan pemesanan ke karyawan
                        karyawan.tambahPemesanan(pemesanan);

                        if (pilihanKeranjang == 1) {
                            keranjang.getItems().remove(nomorItemDipilih - 1);
                        } else {
                            keranjang.kosongkan();
                        }

                        System.out.println("\n[SUKSES] Pemesanan berhasil dibuat.");
                        System.out.println("Nomor Nota : " + pemesanan.getNomorNota());
                        System.out.println("Status     : " + pemesanan.getStatusPemesanan());
                        System.out.println("[INFO] Stok supplier sudah berkurang.");

                        next();
                        break;
                    }

                    // CASE 3: RIWAYAT PEMESANAN
                    case 3: {
                        System.out.println("\n========== RIWAYAT PEMESANAN ==========");

                        if (karyawan.getDaftarPemesanan().isEmpty()) {
                            System.out.println("[INFO] Belum ada pemesanan.");
                        } else {
                            int nomorHistory = 1;
                            for (Pemesanan p : karyawan.getDaftarPemesanan()) {
                                System.out.println("\n------------------------------");
                                System.out.println("     Pemesanan ke-" + nomorHistory);
                                System.out.println("------------------------------");
                                System.out.println(p);
                                System.out.println("Detail Item:");
                                for (ItemPemesanan item : p.getItems()) {
                                    System.out.println(item);
                                }
                                nomorHistory++;
                            }
                        }
                        next();
                        break;
                    }

                    // CASE 4: PENERIMAAN BARANG (SCAN BARCODE)
                    case 4: {
                        if (karyawan.getDaftarPemesanan().isEmpty()) {
                            System.out.println("[ERROR] Belum ada pemesanan.");
                            next();
                            break;
                        }

                        boolean penerimaanAktif = true;

                        while (penerimaanAktif) {
                            System.out.println("\n=========== PENERIMAAN BARANG ===========");

                            for (Pemesanan p : karyawan.getDaftarPemesanan()) {
                                System.out.println("\nNota   : " + p.getNomorNota());
                                System.out.println("Status : " + p.getStatusPemesanan());

                                for (ItemPemesanan item : p.getItems()) {
                                    String kunci      = buatKunciPenerimaan(p, item);
                                    int sudahDiterima = jumlahDiterimaPerItem.getOrDefault(kunci, 0);
                                    int sisa          = item.getJumlahPesan() - sudahDiterima;

                                    System.out.println("------------------------------");
                                    System.out.println("ID Produk        : " + item.getIdProduk());
                                    System.out.println("Kode Barcode     : " + item.getKodeBarcode());
                                    System.out.println("Nama Produk      : " + item.getNamaProduk());
                                    System.out.println("Jumlah Pesan     : " + item.getJumlahPesan());
                                    System.out.println("Sudah Diterima   : " + sudahDiterima);
                                    System.out.println("Sisa             : " + sisa);
                                }
                            }

                            String inputNota = inputTeks("\nMasukkan nomor nota (ketik 0 untuk kembali): ");
                            if (inputNota.equals("0")) {
                                penerimaanAktif = false;
                                continue;
                            }

                            try {
                                Pemesanan pesananAktif = cariPemesanan(karyawan.getDaftarPemesanan(), inputNota);
                                StatusPemesanan statusPesanan = pesananAktif.getStatusPemesanan();

                                if (statusPesanan == StatusPemesanan.DITERIMA) {
                                    System.out.println("[INFO] Pesanan sudah diterima lengkap.");
                                    continue;
                                }
                                if (statusPesanan == StatusPemesanan.DIBATALKAN) {
                                    System.out.println("[ERROR] Pesanan sudah dibatalkan.");
                                    continue;
                                }
                                if (statusPesanan != StatusPemesanan.DIKIRIM
                                        && statusPesanan != StatusPemesanan.DIPROSES) {
                                    System.out.println("[ERROR] Pesanan belum dikirim.");
                                    continue;
                                }

                                String barcode = inputTeks("Masukkan kode barcode: ");
                                ItemPemesanan itemDipesan = cariItemBarcode(pesananAktif, barcode);

                                String kunci             = buatKunciPenerimaan(pesananAktif, itemDipesan);
                                int diterimaSebelumnya   = jumlahDiterimaPerItem.getOrDefault(kunci, 0);
                                int sisaBelumDiterima    = itemDipesan.getJumlahPesan() - diterimaSebelumnya;

                                if (sisaBelumDiterima <= 0) {
                                    System.out.println("[INFO] Produk ini sudah diterima lengkap.");
                                    continue;
                                }

                                System.out.println("Jumlah Pesan        : " + itemDipesan.getJumlahPesan());
                                System.out.println("Diterima Sebelumnya : " + diterimaSebelumnya);
                                System.out.println("Sisa Belum Diterima : " + sisaBelumDiterima);

                                int jumlahDiterima  = inputAngka("Masukkan jumlah diterima: ", 1, sisaBelumDiterima);
                                LocalDate tglExpired = inputTanggal("Masukkan tanggal expired (yyyy-mm-dd): ");

                                ScanBarcode sesiScan = new ScanBarcode("SCN" + System.currentTimeMillis(), pesananAktif);
                                HasilScanBarcode hasilScan = sesiScan.inputProduk(barcode, jumlahDiterima, tglExpired, katalogSupplier);

                                if (hasilScan == null) {
                                    throw new ProdukNotFoundException("[ERROR] Produk gagal dipindai.");
                                }

                                int totalSesudahDiterima = diterimaSebelumnya + jumlahDiterima;
                                int jumlahKurang         = itemDipesan.getJumlahPesan() - totalSesudahDiterima;

                                System.out.println("\nNama Produk      : " + itemDipesan.getNamaProduk());
                                System.out.println("Jumlah Diterima  : " + jumlahDiterima);
                                System.out.println("Total Diterima   : " + totalSesudahDiterima);
                                System.out.println("Jumlah Kurang    : " + jumlahKurang);

                                String konfirmasi = inputYaTidak("Konfirmasi penerimaan? (ya/tidak): ");
                                if (konfirmasi.equalsIgnoreCase("tidak")) {
                                    System.out.println("[INFO] Penerimaan dibatalkan.");
                                    continue;
                                }

                                // Update stok toko via ScanBarcode
                                sesiScan.updateStokOtomatis(stokToko, katalogSupplier);
                                jumlahDiterimaPerItem.put(kunci, totalSesudahDiterima);

                                if (jumlahKurang > 0) {
                                    daftarBarangKurang.put(kunci,
                                        "Nota             : " + pesananAktif.getNomorNota() +
                                        "\nID Produk        : " + itemDipesan.getIdProduk() +
                                        "\nKode Barcode     : " + itemDipesan.getKodeBarcode() +
                                        "\nNama Produk      : " + itemDipesan.getNamaProduk() +
                                        "\nJumlah Pesan     : " + itemDipesan.getJumlahPesan() +
                                        "\nTotal Diterima   : " + totalSesudahDiterima +
                                        "\nJumlah Kurang    : " + jumlahKurang
                                    );
                                } else {
                                    daftarBarangKurang.remove(kunci);
                                }

                                boolean semuaLengkap = pesananAktif.semuaItemSudahDiterima(jumlahDiterimaPerItem);

                                // konfirmasiPenerimaan sekarang mengubah status pemesanan
                                sesiScan.konfirmasiPenerimaan(semuaLengkap);

                                if (semuaLengkap) {
                                    System.out.println("\n[SUKSES] Seluruh barang pada nota sudah diterima.");
                                } else {
                                    System.out.println("\n[INFO] Penerimaan berhasil dicatat.");
                                    System.out.println("[INFO] Pesanan masih DIPROSES karena belum lengkap.");
                                }

                            } catch (PemesananNotFoundException | ProdukNotFoundException e) {
                                System.out.println(e.getMessage());
                            }

                            String lanjut = inputYaTidak("Pindai barang lain? (ya/tidak): ");
                            if (lanjut.equalsIgnoreCase("tidak")) penerimaanAktif = false;
                        }

                        next();
                        break;
                    }

                    // CASE 5: STOK TOKO DAN RETUR
                    case 5: {
                        boolean menuStokAktif = true;

                        while (menuStokAktif) {
                            System.out.println("\n=========== STOK TOKO ===========");

                            if (stokToko.getSemuaProduk().isEmpty()) {
                                System.out.println("[INFO] Stok toko masih kosong.");
                                menuStokAktif = false;
                                continue;
                            }

                            int nomorStok = 1;
                            for (Produk produk : stokToko.getSemuaProduk()) {
                                System.out.println("\n------------------------------");
                                System.out.println("     Produk ke-" + nomorStok);
                                System.out.println("------------------------------");
                                System.out.println("ID Produk       : " + produk.getId());
                                System.out.println("Kode Barcode    : " + produk.getKodeBarcode());
                                System.out.println("Nama Produk     : " + produk.getNamaProduk());
                                System.out.println("Kategori        : " + produk.getKategori());
                                System.out.println("Harga           : Rp " + produk.getHarga());
                                System.out.println("Stok            : " + produk.getStokSaatIni());
                                System.out.println("Tanggal Expired : " + produk.getTanggalExpired());
                                System.out.println("Status Expired  : " + produk.cekStatusExpired());
                                nomorStok++;
                            }

                            System.out.println("\nTotal Semua Stok: " + stokToko.getTotalStok());
                            System.out.println("\n1. Retur barang");
                            System.out.println("0. Kembali");

                            int pilihanStok = inputAngka("Pilih menu: ", 0, 1);
                            if (pilihanStok == 0) {
                                menuStokAktif = false;
                                continue;
                            }

                            try {
                                int nomorProdukRetur = inputAngka(
                                    "Masukkan nomor produk: ", 1, stokToko.getSemuaProduk().size()
                                );

                                Produk produkRetur = stokToko.getSemuaProduk().get(nomorProdukRetur - 1);

                                System.out.println("\nProduk Dipilih:");
                                System.out.println("Nama Produk     : " + produkRetur.getNamaProduk());
                                System.out.println("Stok            : " + produkRetur.getStokSaatIni());
                                System.out.println("Tanggal Expired : " + produkRetur.getTanggalExpired());
                                System.out.println("Status Expired  : " + produkRetur.cekStatusExpired());

                                System.out.println("\nAlasan Retur:");
                                System.out.println("1. Mendekati expired atau expired");
                                System.out.println("2. Barang tidak sesuai");
                                System.out.println("0. Batal");

                                int pilihanAlasan = inputAngka("Pilih alasan: ", 0, 2);
                                if (pilihanAlasan == 0) {
                                    System.out.println("[INFO] Retur dibatalkan.");
                                    continue;
                                }

                                String alasanRetur;
                                KategoriRetur kategoriRetur;

                                if (pilihanAlasan == 1) {
                                    StatusExpired status = produkRetur.cekStatusExpired();
                                    if (status != StatusExpired.MENDEKATI_EXPIRED && status != StatusExpired.EXPIRED) {
                                        throw new ProdukExpiredException(
                                            "[ERROR] Produk belum mendekati expired."
                                        );
                                    }
                                    alasanRetur    = status == StatusExpired.EXPIRED ? "Produk sudah expired" : "Produk mendekati expired";
                                    kategoriRetur  = KategoriRetur.MENDEKATI_EXPIRED;
                                } else {
                                    String keterangan = inputTeks("Masukkan keterangan ketidaksesuaian: ");
                                    alasanRetur   = "Barang tidak sesuai: " + keterangan;
                                    kategoriRetur = KategoriRetur.SALAH_KIRIM;
                                }

                                int jumlahRetur = inputAngka(
                                    "Masukkan jumlah retur: ", 1, produkRetur.getStokSaatIni()
                                );

                                String konfirmasiRetur = inputYaTidak("Konfirmasi retur? (ya/tidak): ");
                                if (konfirmasiRetur.equalsIgnoreCase("tidak")) {
                                    System.out.println("[INFO] Retur dibatalkan.");
                                    continue;
                                }

                                // Buat LaporanRetur dan ItemRetur
                                String idLaporan = "LR" + String.format("%03d", karyawan.getDaftarLaporanRetur().size() + 1);
                                LaporanRetur laporan = new LaporanRetur(
                                    idLaporan, LocalDate.now(), karyawan.getId(), kategoriRetur, alasanRetur
                                );
                                laporan.tambahkanItem(new ItemRetur(
                                    produkRetur.getId(), produkRetur.getNamaProduk(), jumlahRetur
                                ));

                                // kirimLaporan sekarang mengurangi stok toko
                                laporan.kirimLaporan(stokToko);

                                // Simpan laporan ke karyawan
                                karyawan.tambahLaporan(laporan);

                                if (produkRetur.getStokSaatIni() == 0) {
                                    stokToko.hapusProduk(produkRetur.getId());
                                }

                                System.out.println("\n[SUKSES] Barang berhasil diretur.");
                                System.out.println("[INFO] Stok toko berkurang " + jumlahRetur + ".");
                                System.out.println("[INFO] Total riwayat retur: " + karyawan.getDaftarLaporanRetur().size());

                            } catch (ProdukExpiredException e) {
                                System.out.println(e.getMessage());
                            }
                        }

                        next();
                        break;
                    }

                    // CASE 6: BATALKAN PEMESANAN
                    case 6: {
                        if (karyawan.getDaftarPemesanan().isEmpty()) {
                            System.out.println("[INFO] Belum ada pemesanan.");
                            next();
                            break;
                        }

                        int nomor = 1;
                        for (Pemesanan p : karyawan.getDaftarPemesanan()) {
                            System.out.println(nomor + ". " + p.getNomorNota() + " | " + p.getStatusPemesanan());
                            nomor++;
                        }

                        int pilihanBatal = inputAngka("Masukkan nomor pemesanan: ", 1, karyawan.getDaftarPemesanan().size());
                        Pemesanan pesananBatal = karyawan.getDaftarPemesanan().get(pilihanBatal - 1);
                        StatusPemesanan status = pesananBatal.getStatusPemesanan();

                        try {
                            if (status == StatusPemesanan.DIBATALKAN) {
                                throw new PemesananTidakBisaDibatalkanException(
                                    "[INFO] Pesanan sudah dibatalkan."
                                );
                            } else if (status == StatusPemesanan.DITERIMA) {
                                throw new PemesananTidakBisaDibatalkanException(
                                    "[ERROR] Pesanan sudah diterima dan tidak dapat dibatalkan."
                                );
                            } else if (status == StatusPemesanan.DIPROSES) {
                                throw new PemesananTidakBisaDibatalkanException(
                                    "[ERROR] Pesanan sudah diterima sebagian dan tidak dapat dibatalkan."
                                );
                            }

                            String konfirmasiBatal = inputYaTidak("Konfirmasi pembatalan? (ya/tidak): ");
                            if (konfirmasiBatal.equalsIgnoreCase("ya")) {
                                // batalkanPemesanan sekarang sekalian mengembalikan stok supplier
                                pesananBatal.batalkanPemesanan(katalogSupplier);
                                System.out.println("[SUKSES] Pesanan dibatalkan.");
                                System.out.println("[INFO] Stok supplier dikembalikan.");
                            }

                        } catch (PemesananTidakBisaDibatalkanException e) {
                            System.out.println(e.getMessage());
                        }

                        next();
                        break;
                    }

                    // CASE 7: PROFIL
                    case 7: {
                        System.out.println(karyawan);
                        next();
                        break;
                    }

                    // CASE 8: BARANG KURANG
                    case 8: {
                        System.out.println("\n=========== BARANG KURANG ===========");
                        if (daftarBarangKurang.isEmpty()) {
                            System.out.println("[INFO] Tidak ada barang kurang.");
                        } else {
                            int nomorKurang = 1;
                            for (String barangKurang : daftarBarangKurang.values()) {
                                System.out.println("\n------------------------------");
                                System.out.println("     Barang Kurang ke-" + nomorKurang);
                                System.out.println("------------------------------");
                                System.out.println(barangKurang);
                                nomorKurang++;
                            }
                        }
                        next();
                        break;
                    }

                    // CASE 9: RIWAYAT BARANG RETUR
                    case 9: {
                        System.out.println("\n=========== RIWAYAT BARANG RETUR ===========");
                        if (karyawan.getDaftarLaporanRetur().isEmpty()) {
                            System.out.println("[INFO] Belum ada barang yang diretur.");
                        } else {
                            int nomorRetur = 1;
                            for (LaporanRetur laporan : karyawan.getDaftarLaporanRetur()) {
                                System.out.println("\n------------------------------");
                                System.out.println("     Retur ke-" + nomorRetur);
                                System.out.println("------------------------------");
                                System.out.println(laporan.generateReport());
                                for (ItemRetur item : laporan.getItems()) {
                                    System.out.println(item);
                                }
                                nomorRetur++;
                            }
                            System.out.println("\nTotal Riwayat Retur: " + karyawan.getDaftarLaporanRetur().size());
                        }
                        next();
                        break;
                    }

                    // CASE 0: LOGOUT
                    case 0: {
                        karyawan.logout();
                        login = false;
                        System.out.println("[INFO] Logout berhasil.");
                        break;
                    }

                    default: {
                        System.out.println("[ERROR] Menu tidak tersedia.");
                    }
                }
            }
        }

        in.close();
    }
}
