import java.util.Scanner;
// import model.StokToko;
// import model.Karyawan;
// import model.KatalogSupplier;
// import model.Produk;
// import base.BaseEntity;

public class tmart{  
    
    public static void start(){
        System.out.println("====================================\n");
    }

    public static void out(){
        System.out.println("\n====================================");
    }
    
    public static void menuLogin(){
        System.out.println("1. Login dengan akun yang sudah ada");
        System.out.println("2. Keluar");
    }

    public static void menuKaryawan(){
        System.out.println("\n=== MENU KARYAWAN ===");
        System.out.println("1. Lihat Produk Supplier");
        System.out.println("2. Tambah Produk ke Keranjang");
        System.out.println("3. Lihat Keranjang");
        System.out.println("4. Buat Pemesanan");
        System.out.println("5. Scan Barcode Penerimaan");
        System.out.println("6. Lihat Stok Toko");
        System.out.println("9. Keluar");
    }


    public static void pilihMenu(){
        System.out.print("Silahakan Pilih menu>> ");
    }

    public static void main(String[] args) {
        
        // Karyawan karyawan1 = new Karyawan("T001", "Budi", "Budi01@gmail.com", "budi01","admin123","0811-0000-9999","Bandung","TMart");

        // Produk produk1 = new Produk("P001", "SS1", "Susu", "Minuman", 10000, 50, LocalDate.of(2026, 6, 30));
        // Produk produk2 = new Produk("P002", "SR2", "Sereal", "Makanan", 50000, 30, LocalDate.of(2026, 6, 20));
        
        // KatalogSupplier katalogSupplier = new KatalogSupplier("Supplier");
        // katalogSupplier.tambahProduk(produk1);
        // katalogSupplier.tambahProduk(produk2);

        // StokToko stokToko = new StokToko("Stok Toko");
        // StokToko.tambahStok(produk1.getId(), produk1.getJumlahStok());
        // StokToko.tambahStok(produk2.getId(), produk2.getJumlahStok());

        Scanner in = new Scanner(System.in);
        start();
        menuLogin();
        out();
        pilihMenu();
        int masuk =  0; 
        
        while(masuk != 9){
            masuk = in.nextInt();
            if(masuk == 1){
                System.out.println("Selamat Datang!");
                start();
                menuKaryawan();
                out();
                pilihMenu();
                int i = in.nextInt();
                while(i != 0){
                    if (i == 1) {
                        System.out.println("Informasi Produk");
                    }else if (i == 2){

                    }else if (i == 3){
                        
                    }else if (i == 4){

                    }else if (i == 5){

                    }else if (i == 6){
                        
                    }else if (i == 9){
                        break;
                    }else{
                        System.out.println("Masukan menu yang benar");
                    }
                    
                }
            }else if(masuk == 2){
                System.out.println("Berhasil Logout");
                break;
            }else{
                System.out.println("Harap isi dengan benar!");
            }

        }
    }
}