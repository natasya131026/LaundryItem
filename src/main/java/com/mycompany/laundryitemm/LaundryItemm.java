package com.mycompany.laundryitemm;

import java.util.Scanner;

// ============================================================
// SMART LAUNDRY - Tugas Gabungan LKP 2-7
// Semua class & interface digabung dalam satu file (LaundryItemm.java).
// Catatan: hanya class LaundryItemm yang boleh public di dalam file ini.
//
// Tambahan Modul 6 (Polymorphism):
//   - Subclass baru: CuciExpress (total 4 subclass)
//   - Array bertipe Superclass (upcasting), dynamic binding saat looping
//   - Method berparameter Superclass: Main.prosesOrder(LaundryItemm)
//   - Compile-time polymorphism: overloading hitungBiaya(), cariOrder(), cetakStruk()
//
// Tambahan Modul 7 (Abstraction):
//   - LaundryItemm menjadi abstract class + abstract method
//   - 2 interface baru: DapatDiantar dan DapatDinilai
//   - CuciSetrika implements 2 interface sekaligus (multiple implements)
//   - Pemanggilan method interface diamankan dengan instanceof
// ============================================================

// ==================== INTERFACE (Modul 7) ====================

/** Kontrak: layanan yang bisa diantar ke rumah pelanggan. */
interface DapatDiantar {
    void antarKeRumah();
}

/** Kontrak: layanan yang bisa diberi rating oleh pelanggan. */
interface DapatDinilai {
    void beriRating(int rating);
}

/**
 * SUPERCLASS (ABSTRACT): LaundryItemm
 * Mewakili satu order laundry. Tidak bisa di-instansiasi langsung (Modul 7),
 * harus lewat salah satu subclass: CuciReguler, CuciKering, CuciSetrika, CuciExpress.
 */
public abstract class LaundryItemm {

    // Titik masuk program (agar tombol Run NetBeans langsung jalan dari file ini)
    public static void main(String[] args) {
        Main.main(args);
    }

    // STATIC: pencatat total objek yang berhasil dibuat (Modul 4)
    private static int totalOrder = 0;
    private static final double HARGA_PER_KG = 5000;

    // ENCAPSULATION: semua field private
    private int idOrder;
    private String namaPelanggan;
    private String jenisPakaian;
    private double beratKg;

    public LaundryItemm(String namaPelanggan, String jenisPakaian, double beratKg) {
        setNamaPelanggan(namaPelanggan);
        setJenisPakaian(jenisPakaian);
        setBeratKg(beratKg);
        totalOrder++;
        this.idOrder = totalOrder;
    }

    // ===== GETTER =====
    public int getIdOrder() { return idOrder; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getJenisPakaian() { return jenisPakaian; }
    public double getBeratKg() { return beratKg; }
    public static int getTotalOrder() { return totalOrder; }
    protected static double getHargaPerKg() { return HARGA_PER_KG; }

    // ===== SETTER dengan VALIDASI =====
    public void setNamaPelanggan(String namaPelanggan) {
        if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong!");
        }
        this.namaPelanggan = namaPelanggan.trim();
    }

    public void setJenisPakaian(String jenisPakaian) {
        if (jenisPakaian == null || jenisPakaian.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis pakaian tidak boleh kosong!");
        }
        this.jenisPakaian = jenisPakaian.trim();
    }

    public void setBeratKg(double beratKg) {
        if (beratKg <= 0 || beratKg > 50) {
            throw new IllegalArgumentException("Berat harus lebih dari 0 dan maksimal 50 kg!");
        }
        this.beratKg = beratKg;
    }

    // ===== ABSTRACT METHOD (Modul 7): WAJIB di-override semua subclass =====
    /** Menjalankan proses khusus layanan ini (mencetak keterangan ke layar). */
    public abstract void prosesLayanan();

    /** Estimasi lama pengerjaan dalam jam. */
    public abstract int getEstimasiJam();

    // ===== METHOD YANG DI-OVERRIDE SUBCLASS (Modul 5) =====
    public String getJenisLayanan() {
        return "Cuci Reguler";
    }

    public String getDetail() {
        return "-";
    }

    public double hitungBiaya() {
        return beratKg * HARGA_PER_KG;
    }

    /** METHOD OVERLOADING / compile-time polymorphism (Modul 4 & 6) */
    public double hitungBiaya(double diskonPersen) {
        if (diskonPersen < 0 || diskonPersen > 100) {
            throw new IllegalArgumentException("Diskon harus 0 - 100 persen!");
        }
        return hitungBiaya() * (1 - diskonPersen / 100);
    }

    public void tampilkanInfo() {
        System.out.printf("| %-3d | %-12s | %-16s | %-10s | %6.1f | Rp %,9.0f | %-28s |%n",
                idOrder, namaPelanggan, getJenisLayanan(), jenisPakaian,
                beratKg, hitungBiaya(), getDetail());
    }
}

/**
 * SUBCLASS 0: CuciReguler (layanan dasar).
 * Dulu LaundryItemm sendiri; kini jadi subclass karena superclass abstract.
 */
class CuciReguler extends LaundryItemm {

    public CuciReguler(String namaPelanggan, String jenisPakaian, double beratKg) {
        super(namaPelanggan, jenisPakaian, beratKg);
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Reguler";
    }

    @Override
    public void prosesLayanan() {
        System.out.println("[Reguler] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " dicuci biasa lalu dijemur di bawah sinar matahari.");
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Reguler: pengerjaan sekitar 48 jam.");
        return 48;
    }
}

/**
 * SUBCLASS 1: CuciKering (cuci + pengeringan mesin).
 * Implements DapatDinilai.
 */
class CuciKering extends LaundryItemm implements DapatDinilai {
    private static final double TAMBAHAN_PER_KG = 3000;

    private int suhuPengering;   // derajat Celsius
    private int durasiMenit;
    private int rating = 0;      // 0 = belum dinilai

    public CuciKering(String namaPelanggan, String jenisPakaian, double beratKg,
                      int suhuPengering, int durasiMenit) {
        super(namaPelanggan, jenisPakaian, beratKg);   // super pada constructor
        setSuhuPengering(suhuPengering);
        setDurasiMenit(durasiMenit);
    }

    public int getSuhuPengering() { return suhuPengering; }
    public int getDurasiMenit() { return durasiMenit; }
    public int getRating() { return rating; }

    public void setSuhuPengering(int suhuPengering) {
        if (suhuPengering < 40 || suhuPengering > 90) {
            throw new IllegalArgumentException("Suhu pengering harus 40 - 90 derajat Celsius!");
        }
        this.suhuPengering = suhuPengering;
    }

    public void setDurasiMenit(int durasiMenit) {
        if (durasiMenit < 10 || durasiMenit > 120) {
            throw new IllegalArgumentException("Durasi pengeringan harus 10 - 120 menit!");
        }
        this.durasiMenit = durasiMenit;
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Kering";
    }

    @Override
    public String getDetail() {
        return suhuPengering + " C, " + durasiMenit + " menit"
                + (rating > 0 ? ", rating " + rating : "");
    }

    @Override
    public double hitungBiaya() {
        return super.hitungBiaya() + (getBeratKg() * TAMBAHAN_PER_KG);
    }

    // ----- abstract method dari superclass -----
    @Override
    public void prosesLayanan() {
        System.out.println("[Kering] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " dicuci, lalu dikeringkan mesin " + suhuPengering + " C selama " + durasiMenit + " menit.");
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Kering: pengerjaan sekitar 24 jam.");
        return 24;
    }

    // ----- kontrak interface DapatDinilai -----
    @Override
    public void beriRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating harus 1 - 5!");
        }
        this.rating = rating;
        System.out.println("[Rating] Order #" + getIdOrder() + " (Cuci Kering) diberi rating "
                + rating + "/5 oleh " + getNamaPelanggan() + ".");
    }
}

/**
 * SUBCLASS 2: CuciSetrika (cuci + setrika + pewangi).
 * MULTIPLE IMPLEMENTS: DapatDiantar dan DapatDinilai sekaligus.
 */
class CuciSetrika extends LaundryItemm implements DapatDiantar, DapatDinilai {
    private static final double TAMBAHAN_PER_KG = 4000;

    private String jenisPewangi;
    private boolean lipatRapi;
    private int rating = 0;

    public CuciSetrika(String namaPelanggan, String jenisPakaian, double beratKg,
                       String jenisPewangi, boolean lipatRapi) {
        super(namaPelanggan, jenisPakaian, beratKg);
        setJenisPewangi(jenisPewangi);
        this.lipatRapi = lipatRapi;
    }

    public String getJenisPewangi() { return jenisPewangi; }
    public boolean isLipatRapi() { return lipatRapi; }
    public int getRating() { return rating; }
    public void setLipatRapi(boolean lipatRapi) { this.lipatRapi = lipatRapi; }

    public void setJenisPewangi(String jenisPewangi) {
        if (jenisPewangi == null || jenisPewangi.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis pewangi tidak boleh kosong!");
        }
        this.jenisPewangi = jenisPewangi.trim();
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Setrika";
    }

    @Override
    public String getDetail() {
        return "Pewangi " + jenisPewangi + (lipatRapi ? ", dilipat" : ", digantung")
                + (rating > 0 ? ", rating " + rating : "");
    }

    @Override
    public double hitungBiaya() {
        return super.hitungBiaya() + (getBeratKg() * TAMBAHAN_PER_KG);
    }

    // ----- abstract method dari superclass -----
    @Override
    public void prosesLayanan() {
        System.out.println("[Setrika] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " dicuci, disetrika, diberi pewangi " + jenisPewangi
                + (lipatRapi ? " dan dilipat rapi." : " dan digantung."));
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Setrika: pengerjaan sekitar 36 jam.");
        return 36;
    }

    // ----- kontrak interface DapatDiantar -----
    @Override
    public void antarKeRumah() {
        System.out.println("[Antar] Kurir mengantar pakaian " + getJenisPakaian()
                + " (Cuci Setrika) ke rumah " + getNamaPelanggan() + ".");
    }

    // ----- kontrak interface DapatDinilai -----
    @Override
    public void beriRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating harus 1 - 5!");
        }
        this.rating = rating;
        System.out.println("[Rating] Order #" + getIdOrder() + " (Cuci Setrika) diberi rating "
                + rating + "/5 oleh " + getNamaPelanggan() + ".");
    }
}

/**
 * SUBCLASS 3 (BARU - Modul 6): CuciExpress (selesai cepat, biaya prioritas).
 * Implements DapatDiantar.
 */
class CuciExpress extends LaundryItemm implements DapatDiantar {
    private static final double TAMBAHAN_PER_KG = 6000;

    private int targetJam;   // target selesai (jam)

    public CuciExpress(String namaPelanggan, String jenisPakaian, double beratKg, int targetJam) {
        super(namaPelanggan, jenisPakaian, beratKg);
        setTargetJam(targetJam);
    }

    public int getTargetJam() { return targetJam; }

    public void setTargetJam(int targetJam) {
        if (targetJam < 3 || targetJam > 12) {
            throw new IllegalArgumentException("Target selesai harus 3 - 12 jam!");
        }
        this.targetJam = targetJam;
    }

    @Override
    public String getJenisLayanan() {
        return "Cuci Express";
    }

    @Override
    public String getDetail() {
        return "Selesai dalam " + targetJam + " jam";
    }

    @Override
    public double hitungBiaya() {
        return super.hitungBiaya() + (getBeratKg() * TAMBAHAN_PER_KG);
    }

    // ----- abstract method dari superclass -----
    @Override
    public void prosesLayanan() {
        System.out.println("[Express] Pakaian " + getJenisPakaian() + " milik " + getNamaPelanggan()
                + " diproses PRIORITAS, ditargetkan selesai dalam " + targetJam + " jam.");
    }

    @Override
    public int getEstimasiJam() {
        System.out.println("[Estimasi] Cuci Express: target selesai " + targetJam + " jam.");
        return targetJam;
    }

    // ----- kontrak interface DapatDiantar -----
    @Override
    public void antarKeRumah() {
        System.out.println("[Antar] Kurir motor mengantar pakaian " + getJenisPakaian()
                + " (Cuci Express) langsung ke " + getNamaPelanggan() + ".");
    }
}

/**
 * SMART LAUNDRY - Tugas Gabungan LKP 2-7
 */
class Main {
    // ARRAY bertipe SUPERCLASS (upcasting): menampung semua jenis subclass
    private static LaundryItemm[] daftar = new LaundryItemm[100];
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

            LaundryItemm baru;   // referensi superclass
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
            // DYNAMIC BINDING: tipe referensi LaundryItemm, tapi yang dijalankan
            // adalah getJenisLayanan(), getDetail(), hitungBiaya() milik wujud asli objek
            daftar[i].tampilkanInfo();
        }
        cetakGaris();
        System.out.println("Total order tercatat (static counter): " + LaundryItemm.getTotalOrder());
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
            LaundryItemm item = cariById(bacaInt("Masukkan ID order: "));
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
    private static void prosesOrder(LaundryItemm item) {
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

        LaundryItemm item = cariById(bacaInt("Masukkan ID order: "));
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
        LaundryItemm item = cariById(bacaInt("Masukkan ID order: "));
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
    private static void cetakStruk(LaundryItemm item) {
        System.out.println("\n========== STRUK #" + item.getIdOrder() + " ==========");
        System.out.println("Pelanggan : " + item.getNamaPelanggan());
        System.out.println("Layanan   : " + item.getJenisLayanan());
        System.out.printf("Berat     : %.1f kg%n", item.getBeratKg());
        System.out.printf("Total     : Rp %,.0f%n", item.hitungBiaya());
        System.out.println("=================================");
    }

    // OVERLOADING (compile-time polymorphism) #2: dengan diskon
    private static void cetakStruk(LaundryItemm item, double diskonPersen) {
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
    private static LaundryItemm cariById(int id) {
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