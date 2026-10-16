package com.mycompany.laundryitemm;

/**
 * SUPERCLASS (ABSTRACT): Pesanan
 * Mewakili satu order laundry. Tidak bisa di-instansiasi langsung (Modul 7),
 * harus lewat salah satu subclass: CuciReguler, CuciKering, CuciSetrika, CuciExpress.
 */
public abstract class Pesanan {

    // STATIC: pencatat total objek yang berhasil dibuat (Modul 4)
    private static int totalOrder = 0;
    private static final double HARGA_PER_KG = 5000;

    // ENCAPSULATION: semua field private
    private int idOrder;
    private String namaPelanggan;
    private String jenisPakaian;
    private double beratKg;

    public Pesanan(String namaPelanggan, String jenisPakaian, double beratKg) {
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