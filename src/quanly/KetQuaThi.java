package quanly;

import java.time.LocalDate;

/**
 * Thực thể kết quả thi của một thí sinh với một đề thi.
 * Dòng file: KETQUA|ma|maThiSinh|maDeThi|diem|soCauDung|tongSoCau|ngayThi
 */
public class KetQuaThi {

    public static final String KY_HIEU_LOAI = "KETQUA";
    public static final String TIEN_TO = "KQ";
    private static final String DINH_DANG_DONG = "%-7s %-10s %-8s %6s %10s   %-11s %s%n";

    // THUỘC TÍNH STATIC: bộ đếm sinh mã kết quả
    private static int boDem = 0;

    private String ma;
    private String maThiSinh;
    private String maDeThi;
    private double diem;
    private int soCauDung;
    private int tongSoCau;
    private LocalDate ngayThi;

    public KetQuaThi() {
        this("", "", "", 0.0, 0, 0, LocalDate.now());
    }

    public KetQuaThi(String ma, String maThiSinh, String maDeThi, double diem,
                     int soCauDung, int tongSoCau, LocalDate ngayThi) {
        this.ma = ma;
        this.maThiSinh = maThiSinh;
        this.maDeThi = maDeThi;
        this.diem = diem;
        this.soCauDung = soCauDung;
        this.tongSoCau = tongSoCau;
        this.ngayThi = ngayThi;
        capNhatBoDem(ma);
    }

    // PHƯƠNG THỨC STATIC: sinh mã kế tiếp, ví dụ KQ004
    public static String sinhMa() {
        return NhapLieu.taoMa(TIEN_TO, boDem + 1);
    }

    public static void capNhatBoDem(String ma) {
        int so = NhapLieu.maThanhSo(ma, TIEN_TO);
        if (so > boDem) {
            boDem = so;
        }
    }

    // PHƯƠNG THỨC STATIC: xếp loại theo thang điểm 10
    public static String xepLoai(double diem) {
        if (diem >= 8.0) {
            return "Giỏi";
        }
        if (diem >= 6.5) {
            return "Khá";
        }
        if (diem >= 5.0) {
            return "Trung bình";
        }
        return "Yếu";
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getMaThiSinh() {
        return maThiSinh;
    }

    public void setMaThiSinh(String maThiSinh) {
        this.maThiSinh = maThiSinh;
    }

    public String getMaDeThi() {
        return maDeThi;
    }

    public void setMaDeThi(String maDeThi) {
        this.maDeThi = maDeThi;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        this.soCauDung = soCauDung;
    }

    public int getTongSoCau() {
        return tongSoCau;
    }

    public void setTongSoCau(int tongSoCau) {
        this.tongSoCau = tongSoCau;
    }

    public LocalDate getNgayThi() {
        return ngayThi;
    }

    public void setNgayThi(LocalDate ngayThi) {
        this.ngayThi = ngayThi;
    }

    public void hienThi() {
        System.out.printf(DINH_DANG_DONG, ma, maThiSinh, maDeThi, String.format("%.2f", diem),
                soCauDung + "/" + tongSoCau, xepLoai(diem), NhapLieu.dinhDangNgay(ngayThi));
    }

    public String toFileString() {
        return KY_HIEU_LOAI + "|" + ma + "|" + maThiSinh + "|" + maDeThi + "|" + diem + "|"
                + soCauDung + "|" + tongSoCau + "|" + NhapLieu.dinhDangNgay(ngayThi);
    }

    public static void inTieuDe() {
        System.out.printf(DINH_DANG_DONG, "Mã", "Thí sinh", "Đề thi", "Điểm", "Đúng/Tổng", "Xếp loại", "Ngày thi");
        System.out.println("-".repeat(75));
    }
}
