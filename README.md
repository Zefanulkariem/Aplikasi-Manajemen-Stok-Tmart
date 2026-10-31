# 🛒 T-Mart: Aplikasi Manajemen Stok

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![GUI](https://img.shields.io/badge/GUI-Java%20Swing-blue)
![OOP](https://img.shields.io/badge/Paradigma-OOP-green)

## 🚀 Gambaran Proyek Kelompok

**T-Mart** adalah aplikasi manajemen stok untuk toko ritel yang dibangun dengan **Java** dan menerapkan prinsip **Pemrograman Berorientasi Objek (OOP)**. Aplikasi ini membantu karyawan toko mengelola alur barang dari awal sampai akhir: memilih produk dari katalog supplier, memesannya, menerima barang dengan pemindaian barcode, memantau stok toko, sampai meretur barang yang bermasalah.

Aplikasi punya dua antarmuka: **GUI berbasis Java Swing** dan **mode konsol (CLI)**.

## 🌟 Fitur

- 🔐 **Login karyawan**: akses aplikasi dengan username dan password.
- 📦 **Katalog supplier**: lihat daftar produk supplier lengkap dengan harga, stok, dan tanggal expired.
- 🛍️ **Keranjang pemesanan**: tambah dan hapus produk. Jumlah yang dipesan dibatasi sesuai stok supplier.
- ⏰ **Pengecekan expired otomatis**: produk yang sudah expired ditolak masuk keranjang, dan produk yang tersisa **≤ 90 hari** sebelum expired diberi peringatan.
- 🧾 **Pemesanan dan riwayat**: pesanan diproses dengan nomor nota, status, dan total harga, dan seluruh riwayatnya bisa dilihat. Status pesanan: `MENUNGGU` → `DIPROSES` → `DIKIRIM` → `DITERIMA` / `DIBATALKAN`.
- 📷 **Penerimaan barang via scan barcode**: barang yang datang dicocokkan dengan pesanan, dan stok toko bertambah otomatis. Produk baru otomatis didaftarkan ke stok toko.
- 📊 **Stok toko**: pantau stok barang yang tersedia di toko saat ini.
- ↩️ **Retur barang**: kirim laporan retur dengan kategori dan alasan, dan stok toko berkurang otomatis.
- 🚫 **Pembatalan pemesanan** *(mode konsol)*: stok supplier dikembalikan, dan pesanan yang sudah diterima tidak bisa dibatalkan.
- 📋 **Barang kurang dan riwayat retur** *(mode konsol)*: lacak barang yang belum diterima lengkap dan arsip laporan retur.
- 👤 **Profil karyawan**: lihat data karyawan yang sedang login.

## 🛠️ Teknologi

| Komponen    | Detail                                    |
|-------------|-------------------------------------------|
| Bahasa      | Java 21                                   |
| Build tool  | Apache Maven                              |
| GUI         | Java Swing (desain form dari NetBeans)    |
| Penyimpanan | In-memory (data dimuat saat aplikasi start) |
| Dependency  | Tidak ada library eksternal               |

## 🧱 Arsitektur dan Konsep OOP

```
src/main/java/
├── base/          # Abstract class: BaseEntity, KatalogBase
├── interfaces/    # Interface: ISearchable, INotifiable
├── enums/         # StatusExpired, StatusPemesanan, KategoriRetur
├── exception/     # Custom exception (ProdukExpired, StokTidakCukup, dll.)
├── model/         # Produk, Karyawan, Keranjang, ItemKeranjang, StokToko, KatalogSupplier
├── transaksi/     # Pemesanan, ItemPemesanan, ScanBarcode, HasilScanBarcode
├── laporan/       # LaporanRetur, ItemRetur
├── tmart/         # DataInitiate (data awal) dan TmartTest (mode konsol)
└── rencananyaGUI/ # Seluruh tampilan GUI Swing dan titik masuk mainGUI
```

Konsep OOP yang diterapkan:

- **Inheritance**: `Produk`, `Karyawan`, `Keranjang`, `Pemesanan`, dan `LaporanRetur` mewarisi `BaseEntity`. `StokToko` dan `KatalogSupplier` mewarisi `KatalogBase`.
- **Abstraction**: `BaseEntity` dan `KatalogBase` adalah abstract class, dan `ISearchable` adalah interface.
- **Polymorphism**: setiap entitas mengimplementasikan `validate()` dan `toString()` dengan caranya sendiri.
- **Encapsulation**: atribut `private`/`protected` yang diakses lewat getter dan method.
- **Exception handling**: exception khusus seperti `ProdukExpiredException`, `StokTidakCukupException`, `ProdukNotFoundException`, `PemesananNotFoundException`, `PemesananTidakBisaDibatalkanException`, dan `InputTidakValidException`.

## 🚀 Cara Menjalankan

### Prasyarat

- **JDK 21** atau lebih baru
- (Opsional) Apache Maven atau IDE seperti NetBeans / IntelliJ IDEA

### 1. Clone repository

```bash
git clone https://github.com/Zefanulkariem/Aplikasi-Manajemen-Stok-Tmart.git
cd Aplikasi-Manajemen-Stok-Tmart
```

### 2. Kompilasi

```bash
# Linux / macOS
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
```

```powershell
# Windows (PowerShell)
mkdir out
javac -d out (Get-ChildItem -Path src/main/java -Recurse -Filter *.java).FullName
```

### 3. Jalankan

**Mode GUI** (disarankan):

```bash
java -cp out rencananyaGUI.mainGUI
```

**Mode konsol (CLI)**:

```bash
java -cp out tmart.TmartTest
```

> 💡 Bisa juga dijalankan lewat IDE: buka proyek sebagai proyek Maven, lalu jalankan class `rencananyaGUI.mainGUI` (GUI) atau `tmart.TmartTest` (konsol).

## 📝 Cara Penggunaan

1. Jalankan aplikasi dan **login** memakai akun demo di bawah.
2. Dari **Dashboard**, pilih menu yang dibutuhkan: Produk, Keranjang, Riwayat, Penerimaan, Stok, Retur, atau Profil.
3. Buka **Produk**, masukkan ID produk dan jumlah, lalu klik **Tambah** untuk memasukkannya ke keranjang.
4. Buka **Keranjang**, tinjau isinya, lalu klik **Pesan** untuk membuat pemesanan ke supplier.
5. Saat barang tiba, buka **Penerimaan** dan masukkan data barcode serta jumlah yang diterima. Stok toko akan diperbarui otomatis.
6. Pantau persediaan di menu **Stok**. Kalau ada barang yang rusak atau mendekati expired, gunakan menu **Retur**.

### 🔑 Akun Demo

| Username | Password   |
|----------|------------|
| `budi01` | `admin123` |

> ⚠️ **Catatan:** aplikasi memakai data awal (*dummy*) dari `DataInitiate.java` dan belum terhubung ke database, jadi semua perubahan hilang saat aplikasi ditutup. Sebagian produk demo punya tanggal expired di tahun 2026, sehingga bisa langsung ditolak sistem jika tanggal expirednya sudah lewat. Ubah tanggalnya di `DataInitiate.java` untuk mencoba alur lengkap.

## 🗺️ Rencana Pengembangan

- [ ] Integrasi database (MySQL / SQLite) agar data tersimpan permanen
- [ ] Hash password dan sistem peran (admin / kasir)
- [ ] Fitur pencarian produk
- [ ] Laporan stok dan penjualan yang bisa diekspor

## 🤝 Kontribusi

Kontribusi sangat terbuka! 🚀

1. Fork repository ini
2. Buat branch fitur (`git checkout -b fitur/nama-fitur`)
3. Commit perubahanmu (`git commit -m "Tambah fitur ..."`)
4. Push ke branch (`git push origin fitur/nama-fitur`)
5. Buka Pull Request

---

Dibuat dengan ❤️ oleh Kelompok DPBO Tercinta
Terima kasih sudah menggunakan **T-Mart**! 🛒
