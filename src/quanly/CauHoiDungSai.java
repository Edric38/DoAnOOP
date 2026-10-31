package quanly;

/**
 * KẾ THỪA: câu hỏi Đúng/Sai, extends trực tiếp CauHoi (không có danh sách lựa chọn).
 * ĐA HÌNH: override kiemTraDapAn() so sánh giá trị boolean.
 * Dòng file: DUNGSAI|ma|noiDung|maMonHoc|doKho|D  (D = Đúng, S = Sai)
 */
public class CauHoiDungSai extends CauHoi {

    public static final String KY_HIEU_LOAI = "DUNGSAI";

    private boolean dapAnDung;

    public CauHoiDungSai() {
        super();
        this.dapAnDung = true;
    }

    public CauHoiDungSai(String ma, String noiDung, String maMonHoc, int doKho, boolean dapAnDung) {
        super(ma, noiDung, maMonHoc, doKho); // gọi constructor lớp cha
        this.dapAnDung = dapAnDung;
    }

    /** STATIC: đọc "D"/"Đúng" -> true, "S"/"Sai" -> false, còn lại -> null. */
    public static Boolean docTraLoi(String traLoi) {
        if (traLoi == null) {
            return null;
        }
        String s = NhapLieu.boDau(traLoi).trim().toUpperCase();
        if (s.equals("D") || s.equals("DUNG")) {
            return Boolean.TRUE;
        }
        if (s.equals("S") || s.equals("SAI")) {
            return Boolean.FALSE;
        }
        return null;
    }

    public boolean isDapAnDung() {
        return dapAnDung;
    }

    public void setDapAnDung(boolean dapAnDung) {
        this.dapAnDung = dapAnDung;
    }

    @Override
    public String getLoai() {
        return "Đúng/Sai";
    }

    @Override
    public String getKyHieuLoai() {
        return KY_HIEU_LOAI;
    }

    @Override
    public boolean laTraLoiHopLe(String traLoi) {
        return docTraLoi(traLoi) != null;
    }

    @Override // ĐA HÌNH: cách kiểm tra riêng của câu Đúng/Sai
    public boolean kiemTraDapAn(String traLoi) {
        Boolean giaTri = docTraLoi(traLoi);
        return giaTri != null && giaTri == dapAnDung;
    }

    @Override
    public String getHuongDanTraLoi() {
        return "Nhập D (Đúng) hoặc S (Sai)";
    }

    @Override
    public String getDapAnDungHienThi() {
        return dapAnDung ? "Đúng" : "Sai";
    }

    @Override
    public void hienThiKhiThi(int soThuTu) {
        System.out.println("Câu " + soThuTu + " (" + getLoai() + "): " + noiDung);
        System.out.println("      D. Đúng      S. Sai");
    }

    @Override
    public void hienThi() {
        inPhanChung();
        System.out.println("    => Đáp án đúng: " + getDapAnDungHienThi());
    }

    @Override
    public String toFileString() {
        return thongTinChungFile() + "|" + (dapAnDung ? "D" : "S");
    }
}
