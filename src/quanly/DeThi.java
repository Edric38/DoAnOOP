package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * Thực thể đề thi: thuộc một môn học, có thời gian làm bài và danh sách MÃ câu hỏi.
 * Dòng file: DETHI|ma|tenDe|maMonHoc|thoiGianLamBai|CH001,CH002,...
 */
public class DeThi {

    public static final String KY_HIEU_LOAI = "DETHI";
    public static final String TIEN_TO = "DT";
    private static final String DINH_DANG_DONG = "%-7s %-32s %-8s %-9s %-7s %s%n";

    // THUỘC TÍNH STATIC: bộ đếm sinh mã đề thi
    private static int boDem = 0;

    private String ma;
    private String tenDe;
    private String maMonHoc;
    private int thoiGianLamBai; // phút
    private ArrayList<String> danhSachMaCauHoi;

    public DeThi() {
        this("", "", "", 15, new ArrayList<>());
    }

    public DeThi(String ma, String tenDe, String maMonHoc, int thoiGianLamBai, List<String> danhSachMaCauHoi) {
        this.ma = ma;
        this.tenDe = tenDe;
        this.maMonHoc = maMonHoc;
        this.thoiGianLamBai = thoiGianLamBai;
        this.danhSachMaCauHoi = new ArrayList<>(danhSachMaCauHoi);
        capNhatBoDem(ma);
    }

    // PHƯƠNG THỨC STATIC: sinh mã kế tiếp, ví dụ DT003
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

    public String getTenDe() {
        return tenDe;
    }

    public void setTenDe(String tenDe) {
        this.tenDe = tenDe;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public int getThoiGianLamBai() {
        return thoiGianLamBai;
    }

    public void setThoiGianLamBai(int thoiGianLamBai) {
        this.thoiGianLamBai = thoiGianLamBai;
    }

    public ArrayList<String> getDanhSachMaCauHoi() {
        return new ArrayList<>(danhSachMaCauHoi);
    }

    public void setDanhSachMaCauHoi(List<String> danhSachMaCauHoi) {
        this.danhSachMaCauHoi = new ArrayList<>(danhSachMaCauHoi);
    }

    public int getSoCauHoi() {
        return danhSachMaCauHoi.size();
    }

    public boolean coCauHoi(String maCauHoi) {
        for (String m : danhSachMaCauHoi) {
            if (m.equalsIgnoreCase(maCauHoi)) {
                return true;
            }
        }
        return false;
    }

    /** Gỡ một câu hỏi khỏi đề (dùng khi câu hỏi bị xóa). */
    public boolean xoaCauHoi(String maCauHoi) {
        return danhSachMaCauHoi.removeIf(m -> m.equalsIgnoreCase(maCauHoi));
    }

    public void hienThi() {
        System.out.printf(DINH_DANG_DONG, ma, tenDe, maMonHoc, thoiGianLamBai + " phút",
                getSoCauHoi() + " câu", String.join(",", danhSachMaCauHoi));
    }

    public String toFileString() {
        return KY_HIEU_LOAI + "|" + ma + "|" + tenDe + "|" + maMonHoc + "|" + thoiGianLamBai + "|"
                + String.join(",", danhSachMaCauHoi);
    }

    public static void inTieuDe() {
        System.out.printf(DINH_DANG_DONG, "Mã", "Tên đề", "Môn", "Thời gian", "Số câu", "Mã câu hỏi");
        System.out.println("-".repeat(110));
    }
}
