package transaksi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import base.BaseEntity;
import enums.StatusPemesanan;
import model.KatalogSupplier;
import model.Produk;
import model.StokToko;

public class ScanBarcode extends BaseEntity {

    private Pemesanan pemesanan;
    private List<HasilScanBarcode> hasilScan;
    private boolean sudahDikonfirmasi;

    public ScanBarcode(String id, Pemesanan pemesanan) {
        super(id);
        this.pemesanan         = pemesanan;
        this.hasilScan         = new ArrayList<>();
        this.sudahDikonfirmasi = false;
    }

    /**
     * Memindai produk berdasarkan barcode.
     * Mencocokkan dengan item pada pemesanan dan mencatat hasilnya.
     * Mengembalikan null jika barcode tidak ditemukan di katalog supplier.
     */
    public HasilScanBarcode inputProduk(String barcode, int jmlDiterima,
                                        LocalDate tglExpired, KatalogSupplier katalog) {
        Produk p = null;
        for (Produk produk : katalog.getSemuaProduk()) {
            if (produk.getKodeBarcode().equals(barcode)) {
                p = produk;
                break;
            }
        }

        if (p == null) return null;

        boolean sesuai = false;
        for (ItemPemesanan ip : pemesanan.getItems()) {
            if (ip.getIdProduk().equals(p.getId())) {
                sesuai = (jmlDiterima >= ip.getJumlahPesan());
            }
        }

        HasilScanBarcode hasil = new HasilScanBarcode(
            p.getId(), p.getNamaProduk(), jmlDiterima, tglExpired, sesuai
        );
        hasilScan.add(hasil);
        return hasil;
    }

    /**
     * Mengkonfirmasi penerimaan barang dan mengubah status pemesanan.
     */
    public void konfirmasiPenerimaan(boolean semuaLengkap) {
        if (sudahDikonfirmasi) return;

        this.sudahDikonfirmasi = true;

        if (semuaLengkap) {
            pemesanan.setStatusPemesanan(StatusPemesanan.DITERIMA);
        } else {
            pemesanan.setStatusPemesanan(StatusPemesanan.DIPROSES);
        }
    }

    /**
     * Memperbarui stok toko berdasarkan hasil scan.
     * Jika produk sudah ada di toko, stoknya ditambah.
     * Jika belum ada, produk baru ditambahkan ke stok toko.
     */
    public void updateStokOtomatis(StokToko stokToko, KatalogSupplier katalogSupplier) {
        for (HasilScanBarcode h : hasilScan) {
            Produk produk = stokToko.getProdukById(h.getIdProduk());

            if (produk != null) {
                produk.updateStok(h.getJumlahDiterima());
            } else {
                Produk produkSupplier = katalogSupplier.getProdukById(h.getIdProduk());
                if (produkSupplier != null) {
                    stokToko.tambahProduk(new Produk(
                        produkSupplier.getId(),
                        produkSupplier.getKodeBarcode(),
                        produkSupplier.getNamaProduk(),
                        produkSupplier.getKategori(),
                        produkSupplier.getHarga(),
                        h.getJumlahDiterima(),
                        h.getTanggalExpiredInput()
                    ));
                }
            }
        }
    }

    @Override
    public boolean validate() {
        return pemesanan != null && hasilScan != null;
    }

    @Override
    public String toString() {
        return "ScanBarcode{" +
               "id: '" + getId() + '\'' +
               ", jumlahScan: " + hasilScan.size() +
               ", sudahDikonfirmasi: " + sudahDikonfirmasi +
               '}';
    }
}
