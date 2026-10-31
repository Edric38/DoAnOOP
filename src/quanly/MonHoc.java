package quanly;

/**
 * Thực thể môn học. Câu hỏi và đề thi đều thuộc về một môn học (qua mã môn).
 */
public class MonHoc {

    public static final String KY_HIEU_LOAI = "MONHOC";
    public static final String TIEN_TO = "MH";
    private static final String DINH_DANG_DONG = "%-7s %-40s %s%n";

    // THUỘC TÍNH STATIC: bộ đếm sinh mã môn học
    private static int boDem = 0;

    private String ma;
    private String tenMon;
    private int soTinChi;

    public MonHoc() {
        this("", "", 1);
    }

    public MonHoc(String ma, String tenMon, int soTinChi) {
        this.ma = ma;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
        capNhatBoDem(ma);
    }

    // PHƯƠNG THỨC STATIC: sinh mã kế tiếp, ví dụ MH004
    public static String sinhMa() {
        return NhapLieu.taoMa(TIEN_TO, boDem + 1);
    }

    public static void capNhatBoDem(String ma) {
        int so = NhapLieu.maThanhSo(ma, TIEN_TO);
        if (so > boDem) {
            boDem = so;
        }
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public void hienThi() {
        System.out.printf(DINH_DANG_DONG, ma, tenMon, soTinChi);
    }

    public String toFileString() {
        return KY_HIEU_LOAI + "|" + ma + "|" + tenMon + "|" + soTinChi;
    }

    public static void inTieuDe() {
        System.out.printf(DINH_DANG_DONG, "Mã", "Tên môn học", "Số tín chỉ");
        System.out.println("-".repeat(60));
    }
}
