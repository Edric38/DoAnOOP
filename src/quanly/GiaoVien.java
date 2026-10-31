package quanly;

import java.time.LocalDate;

/**
 * KẾ THỪA: GiaoVien là một NguoiDung, có thêm thuộc tính chuyên môn.
 * Override các phương thức trừu tượng getVaiTro(), hienThi(), toFileString().
 */
public class GiaoVien extends NguoiDung {

    public static final String KY_HIEU_LOAI = "GIAOVIEN";
    public static final String VAI_TRO = "Giáo viên";
    public static final String TIEN_TO = "GV";

    // THUỘC TÍNH STATIC: bộ đếm sinh mã giáo viên
    private static int boDem = 0;

    private String chuyenMon;

    public GiaoVien() {
        super();
        this.chuyenMon = "";
    }

    public GiaoVien(String ma, String hoTen, LocalDate ngaySinh, String email, String chuyenMon) {
        super(ma, hoTen, ngaySinh, email); // gọi constructor lớp cha
        this.chuyenMon = chuyenMon;
        capNhatBoDem(ma);
    }

    // PHƯƠNG THỨC STATIC: sinh mã kế tiếp, ví dụ GV003
    public static String sinhMa() {
        return NhapLieu.taoMa(TIEN_TO, boDem + 1);
    }

    public static void capNhatBoDem(String ma) {
        int so = NhapLieu.maThanhSo(ma, TIEN_TO);
        if (so > boDem) {
            boDem = so;
        }
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    @Override // ĐA HÌNH: override phương thức trừu tượng
    public String getVaiTro() {
        return VAI_TRO;
    }

    @Override
    public void hienThi() {
        inDong("Chuyên môn: " + chuyenMon);
    }

    @Override
    public String toFileString() {
        return KY_HIEU_LOAI + "|" + thongTinChungFile() + "|" + chuyenMon;
    }
}
