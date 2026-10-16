package com.mycompany.laundryitemm;

import com.mycompany.laundryitemm.CuciExpress;
import com.mycompany.laundryitemm.CuciKering;
import com.mycompany.laundryitemm.CuciReguler;
import com.mycompany.laundryitemm.CuciSetrika;
import com.mycompany.laundryitemm.DapatDiantar;
import com.mycompany.laundryitemm.DapatDinilai;
import com.mycompany.laundryitemm.Pesanan;

import java.util.Scanner;

/**
 * SMART LAUNDRY - Tugas Gabungan LKP 2-7
 */
public class LaundryItemm {
    // ARRAY bertipe SUPERCLASS (upcasting): menampung semua jenis subclass
    private static Pesanan[] daftar = new Pesanan[100];
    private static int jumlah = 0;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        isiDataAwal();

        int pilihan;
        do {
            tampilkanMenu();
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1: tambahOrder(); break;
                case 2: tampilkanSemua(); break;
                case 3: menuCari(); break;
                case 4: tampilkanTotalPendapatan(); break;
                case 5: simulasiProses(); break;
                case 6: menuLayananTambahan(); break;
                case 7: menuStruk(); break;
                case 0: System.out.println("\nTerima kasih telah menggunakan Smart Laundry!"); break;
                default: System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (pilihan != 0);
    }

    // ================= DATA AWAL (6 objek, mencakup SEMUA subclass) =================
    private static void isiDataAwal() {
        // UPCASTING: objek subclass disimpan di referensi/array superclass
        daftar[jumlah++] = new CuciReguler("Budi", "Kaos", 3.5);
        daftar[jumlah++] = new CuciKering("Siti", "Jeans", 4.0, 60, 45);
        daftar[jumlah++] = new CuciSetrika("Andi", "Kemeja", 2.5, "Lavender", true);
        daftar[jumlah++] = new CuciSetrika("Dewi", "Seragam", 5.0, "Mawar", false);
        daftar[jumlah++] = new CuciKering("Rina", "Handuk", 3.0, 70, 60);
        daftar[jumlah++] = new CuciExpress("Tono", "Jas", 2.0, 6);
    }

    // ================= MENU =================
    private static void tampilkanMenu() {
        System.out.println("\n=====================================");
        System.out.println("        SMART LAUNDRY SYSTEM");
        System.out.println("=====================================");
        System.out.println("1. Tambah Order Baru");
        System.out.println("2. Tampilkan Semua Order");
        System.out.println("3. Cari Order");
        System.out.println("4. Total Pendapatan");
        System.out.println("5. Simulasi Proses Order (Dynamic Binding)");
        System.out.println("6. Layanan Tambahan (Antar / Rating)");
        System.out.println("7. Cetak Struk (Overloading)");
        System.out.println("0. Keluar");
        System.out.println("-------------------------------------");
    }

    // ================= 1. TAMBAH ORDER =================
    private static void tambahOrder() {
        if (jumlah >= daftar.length) {
            System.out.println("Kapasitas penuh!");
            return;
        }

        System.out.println("\n--- Pilih Jenis Layanan ---");
        System.out.println("1. Cuci Reguler");
        System.out.println("2. Cuci Kering");
        System.out.println("3. Cuci Setrika");
        System.out.println("4. Cuci Express");
        int jenis = bacaInt("Pilih jenis (1-4): ");

        if (jenis < 1 || jenis > 4) {
            System.out.println("Jenis layanan tidak valid!");
            return;
        }

        try {
            String nama = bacaString("Nama pelanggan : ");
            String pakaian = bacaString("Jenis pakaian  : ");
            double berat = bacaDouble("Berat (kg)     : ");

            Pesanan baru;   // referensi superclass
            switch (jenis) {
                case 2:
                    int suhu = bacaInt("Suhu pengering (40-90 C)   : ");
                    int durasi = bacaInt("Durasi pengering (10-120 mnt): ");
                    baru = new CuciKering(nama, pakaian, berat, suhu, durasi);
                    break;
                case 3:
                    String pewangi = bacaString("Jenis pewangi : ");
                    String lipat = bacaString("Dilipat rapi? (y/n): ");
                    baru = new CuciSetrika(nama, pakaian, berat, pewangi, lipat.equalsIgnoreCase("y"));
                    break;
                case 4:
                    int target = bacaInt("Target selesai (3-12 jam): ");
                    baru = new CuciExpress(nama, pakaian, berat, target);
                    break;
                default:
                    baru = new CuciReguler(nama, pakaian, berat);
            }

            daftar[jumlah++] = baru;
            System.out.println("Order berhasil ditambahkan! ID Order: " + baru.getIdOrder());
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal menambah order: " + e.getMessage());
        }
    }

    // ================= 2. TAMPILKAN SEMUA (dynamic binding) =================
    private static void tampilkanSemua() {
        if (jumlah == 0) {
            System.out.println("Belum ada data order.");
            return;
        }
        cetakHeader();
        for (int i = 0; i < jumlah; i++) {
            // DYNAMIC BINDING: tipe referensi Pesanan, tapi yang dijalankan
            // adalah getJenisLayanan(), getDetail(), hitungBiaya() milik wujud asli objek
            daftar[i].tampilkanInfo();
        }
        cetakGaris();
        System.out.println("Total order tercatat (static counter): " + Pesanan.getTotalOrder());
    }

    // ================= 3. PENCARIAN (overloading) =================
    private static void menuCari() {
        System.out.println("\n1. Cari berdasarkan Nama");
        System.out.println("2. Cari berdasarkan ID Order");
        int mode = bacaInt("Pilih: ");

        if (mode == 1) {
            cariOrder(bacaString("Masukkan nama pelanggan: "));   // memanggil versi String
        } else if (mode == 2) {
            cariOrder(bacaInt("Masukkan ID order: "));            // memanggil versi int
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    // OVERLOADING #1: parameter String
    private static void cariOrder(String nama) {
        boolean ketemu = false;
        for (int i = 0; i < jumlah; i++) {
            if (daftar[i].getNamaPelanggan().toLowerCase().contains(nama.toLowerCase())) {
                if (!ketemu) cetakHeader();
                daftar[i].tampilkanInfo();
                ketemu = true;
            }
        }
        if (ketemu) cetakGaris(); else System.out.println("Order atas nama \"" + nama + "\" tidak ditemukan.");
    }

    // OVERLOADING #2: parameter int
    private static void cariOrder(int id) {
        for (int i = 0; i < jumlah; i++) {
            if (daftar[i].getIdOrder() == id) {
                cetakHeader();
                daftar[i].tampilkanInfo();
                cetakGaris();
                return;
            }
        }
        System.out.println("Order dengan ID " + id + " tidak ditemukan.");
    }

    // ================= 4. TOTAL PENDAPATAN =================
    private static void tampilkanTotalPendapatan() {
        double total = 0;
        for (int i = 0; i < jumlah; i++) {
            total += daftar[i].hitungBiaya();
        }
        System.out.printf("%nTotal pendapatan dari %d order: Rp %,.0f%n", jumlah, total);
    }

    // ================= 5. SIMULASI PROSES (Modul 6: upcasting parameter) =================
    private static void simulasiProses() {
        System.out.println("\n--- Simulasi Proses Order ---");
        System.out.println("1. Proses SEMUA order (looping array superclass)");
        System.out.println("2. Proses satu order berdasarkan ID");
        int mode = bacaInt("Pilih: ");

        if (mode == 1) {
            for (int i = 0; i < jumlah; i++) {
                prosesOrder(daftar[i]);
            }
        } else if (mode == 2) {
            Pesanan item = cariById(bacaInt("Masukkan ID order: "));
            if (item == null) {
                System.out.println("Order tidak ditemukan.");
            } else {
                prosesOrder(item);
            }
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    /**
     * Parameter bertipe SUPERCLASS: menerima objek subclass mana pun (UPCASTING).
     * item.prosesLayanan() dipilih saat RUNTIME sesuai wujud asli objek (DYNAMIC BINDING).
     */
    private static void prosesOrder(Pesanan item) {
        System.out.println("\nOrder #" + item.getIdOrder() + " - " + item.getJenisLayanan());
        item.prosesLayanan();   // abstract method yang sudah di-override
        System.out.printf("   Estimasi selesai: %d jam | Biaya: Rp %,.0f%n",
                item.getEstimasiJam(), item.hitungBiaya());
    }

    // ================= 6. LAYANAN TAMBAHAN (Modul 7: interface + instanceof) =================
    private static void menuLayananTambahan() {
        System.out.println("\n--- Layanan Tambahan ---");
        System.out.println("1. Antar ke rumah");
        System.out.println("2. Beri rating");
        int mode = bacaInt("Pilih: ");
        if (mode != 1 && mode != 2) {
            System.out.println("Pilihan tidak valid.");
            return;
        }

        Pesanan item = cariById(bacaInt("Masukkan ID order: "));
        if (item == null) {
            System.out.println("Order tidak ditemukan.");
            return;
        }

        try {
            if (mode == 1) {
                // INSTANCEOF: cek dulu apakah objek menyetujui kontrak DapatDiantar
                if (item instanceof DapatDiantar) {
                    DapatDiantar pengantar = (DapatDiantar) item;   // downcasting
                    pengantar.antarKeRumah();
                } else {
                    System.out.println("Layanan " + item.getJenisLayanan()
                            + " tidak mendukung antar ke rumah (ambil sendiri di outlet).");
                }
            } else {
                if (item instanceof DapatDinilai) {
                    int nilai = bacaInt("Rating (1-5): ");
                    ((DapatDinilai) item).beriRating(nilai);
                } else {
                    System.out.println("Layanan " + item.getJenisLayanan()
                            + " belum mendukung pemberian rating.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal: " + e.getMessage());
        }
    }

    // ================= 7. STRUK (overloading cetakStruk) =================
    private static void menuStruk() {
        Pesanan item = cariById(bacaInt("Masukkan ID order: "));
        if (item == null) {
            System.out.println("Order tidak ditemukan.");
            return;
        }
        String pakaiDiskon = bacaString("Pakai diskon? (y/n): ");
        if (pakaiDiskon.equalsIgnoreCase("y")) {
            double diskon = bacaDouble("Diskon (%): ");
            try {
                cetakStruk(item, diskon);
            } catch (IllegalArgumentException e) {
                System.out.println("Gagal: " + e.getMessage());
            }
        } else {
            cetakStruk(item);
        }
    }

    // OVERLOADING (compile-time polymorphism) #1: tanpa diskon
    private static void cetakStruk(Pesanan item) {
        System.out.println("\n========== STRUK #" + item.getIdOrder() + " ==========");
        System.out.println("Pelanggan : " + item.getNamaPelanggan());
        System.out.println("Layanan   : " + item.getJenisLayanan());
        System.out.printf("Berat     : %.1f kg%n", item.getBeratKg());
        System.out.printf("Total     : Rp %,.0f%n", item.hitungBiaya());
        System.out.println("=================================");
    }

    // OVERLOADING (compile-time polymorphism) #2: dengan diskon
    private static void cetakStruk(Pesanan item, double diskonPersen) {
        System.out.println("\n========== STRUK #" + item.getIdOrder() + " ==========");
        System.out.println("Pelanggan : " + item.getNamaPelanggan());
        System.out.println("Layanan   : " + item.getJenisLayanan());
        System.out.printf("Berat     : %.1f kg%n", item.getBeratKg());
        System.out.printf("Subtotal  : Rp %,.0f%n", item.hitungBiaya());
        System.out.printf("Diskon    : %.0f%%%n", diskonPersen);
        System.out.printf("Total     : Rp %,.0f%n", item.hitungBiaya(diskonPersen));
        System.out.println("=================================");
    }

    // ================= HELPER =================
    private static Pesanan cariById(int id) {
        for (int i = 0; i < jumlah; i++) {
            if (daftar[i].getIdOrder() == id) return daftar[i];
        }
        return null;
    }

    private static void cetakHeader() {
        cetakGaris();
        System.out.printf("| %-3s | %-12s | %-16s | %-10s | %6s | %-12s | %-28s |%n",
                "ID", "Pelanggan", "Layanan", "Pakaian", "Berat", "Biaya", "Detail");
        cetakGaris();
    }

    private static void cetakGaris() {
        System.out.println("+-----+--------------+------------------+------------+--------+--------------+------------------------------+");
    }

    // ================= HELPER INPUT (aman dari input salah) =================
    private static String bacaString(String pesan) {
        System.out.print(pesan);
        return input.nextLine();
    }

    private static int bacaInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat!");
            }
        }
    }

    private static double bacaDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                return Double.parseDouble(input.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }
}