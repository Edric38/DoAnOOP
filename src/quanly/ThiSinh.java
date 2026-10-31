package quanly;

import java.time.LocalDate;

/**
 * KẾ THỪA: ThiSinh là một NguoiDung, có thêm thuộc tính lớp.
 * Override các phương thức trừu tượng getVaiTro(), hienThi(), toFileString().
 */
public class ThiSinh extends NguoiDung {

    public static final String KY_HIEU_LOAI = "THISINH";
    public static final String VAI_TRO = "Thí sinh";
    public static final String TIEN_TO = "TS";

    // THUỘC TÍNH STATIC: bộ đếm dùng chung cho mọi thí sinh, phục vụ sinh mã tự động
    private static int boDem = 0;

    private String lop;

    public ThiSinh() {
        super();
        this.lop = "";
    }

    public ThiSinh(String ma, String hoTen, LocalDate ngaySinh, String email, String lop) {
        super(ma, hoTen, ngaySinh, email); // gọi constructor lớp cha
        this.lop = lop;
        capNhatBoDem(ma);
    }

    // PHƯƠNG THỨC STATIC: sinh mã kế tiếp, ví dụ TS006
    public static String sinhMa() {
        return NhapLieu.taoMa(TIEN_TO, boDem + 1);
    }

    // PHƯƠNG THỨC STATIC: cập nhật bộ đếm theo mã lớn nhất đã có (gọi khi đọc file / tạo mới)
    public static void capNhatBoDem(String ma) {
        int so = NhapLieu.maThanhSo(ma, TIEN_TO);
        if (so > boDem) {
            boDem = so;
        }
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    @Override // ĐA HÌNH: override phương thức trừu tượng
    public String getVaiTro() {
        return VAI_TRO;
    }

    @Override
    public void hienThi() {
        inDong("Lớp: " + lop);
    }

    @Override
    public String toFileString() {
        return KY_HIEU_LOAI + "|" + thongTinChungFile() + "|" + lop;
    }
}
