package quanly;

import java.time.LocalDate;

/**
 * LỚP TRỪU TƯỢNG (abstract): khái niệm chung "người dùng" của hệ thống thi.
 * Không tạo đối tượng NguoiDung trực tiếp, chỉ tạo qua lớp con.
 * KẾ THỪA: ThiSinh extends NguoiDung, GiaoVien extends NguoiDung.
 */
public abstract class NguoiDung {

    // Định dạng một dòng khi in bảng, dùng chung cho các lớp con
    protected static final String DINH_DANG_DONG = "%-7s %-10s %-24s %-11s %-28s %s%n";

    protected String ma;
    protected String hoTen;
    protected LocalDate ngaySinh;
    protected String email;

    public NguoiDung() {
        this("", "", LocalDate.of(2000, 1, 1), "");
    }

    public NguoiDung(String ma, String hoTen, LocalDate ngaySinh, String email) {
        this.ma = ma;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.email = email;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ===== PHƯƠNG THỨC TRỪU TƯỢNG: lớp con BẮT BUỘC override =====

    /** Tên vai trò: "Thí sinh" hoặc "Giáo viên". */
    public abstract String getVaiTro();

    /** In một dòng thông tin ra bảng. */
    public abstract void hienThi();

    /** Chuỗi một dòng để ghi file, bắt đầu bằng ký hiệu loại (THISINH|, GIAOVIEN|). */
    public abstract String toFileString();

    /** Phần dữ liệu chung khi ghi file, lớp con nối thêm phần riêng. */
    protected String thongTinChungFile() {
        return ma + "|" + hoTen + "|" + NhapLieu.dinhDangNgay(ngaySinh) + "|" + email;
    }

    /** In một dòng bảng; getVaiTro() được gọi ĐA HÌNH theo đối tượng thực tế. */
    protected void inDong(String thongTinRieng) {
        System.out.printf(DINH_DANG_DONG, ma, getVaiTro(), hoTen,
                NhapLieu.dinhDangNgay(ngaySinh), email, thongTinRieng);
    }

    /** STATIC: in tiêu đề bảng người dùng. */
    public static void inTieuDe() {
        System.out.printf(DINH_DANG_DONG, "Mã", "Vai trò", "Họ tên", "Ngày sinh", "Email", "Thông tin riêng");
        System.out.println("-".repeat(110));
    }
}
