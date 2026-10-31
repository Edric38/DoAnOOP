package quanly;

/**
 * LỚP TRỪU TƯỢNG (abstract) gốc của cây kế thừa câu hỏi:
 *   CauHoi -> CauHoiCoLuaChon -> CauHoiMotDapAn, CauHoiNhieuDapAn
 *   CauHoi -> CauHoiDungSai
 * INTERFACE: implements IChamDiem, cài đặt chamDiem() dựa trên phương thức trừu tượng kiemTraDapAn().
 * "Câu hỏi chung chung" không biết cách kiểm tra đáp án, nên CauHoi phải là abstract.
 */
public abstract class CauHoi implements IChamDiem {

    public static final String TIEN_TO = "CH";

    // THUỘC TÍNH STATIC: bộ đếm sinh mã dùng chung cho mọi loại câu hỏi
    private static int boDem = 0;

    protected String ma;
    protected String noiDung;
    protected String maMonHoc;
    protected int doKho; // 1 = Dễ, 2 = Trung bình, 3 = Khó

    public CauHoi() {
        this("", "", "", 1);
    }

    public CauHoi(String ma, String noiDung, String maMonHoc, int doKho) {
        this.ma = ma;
        this.noiDung = noiDung;
        this.maMonHoc = maMonHoc;
        this.doKho = doKho;
        capNhatBoDem(ma);
    }

    // PHƯƠNG THỨC STATIC: sinh mã kế tiếp, ví dụ CH016
    public static String sinhMa() {
        return NhapLieu.taoMa(TIEN_TO, boDem + 1);
    }

    public static void capNhatBoDem(String ma) {
        int so = NhapLieu.maThanhSo(ma, TIEN_TO);
        if (so > boDem) {
            boDem = so;
        }
    }

    // PHƯƠNG THỨC STATIC: đổi số độ khó sang chữ
    public static String tenDoKho(int doKho) {
        switch (doKho) {
            case 1:
                return "Dễ";
            case 2:
                return "Trung bình";
            case 3:
                return "Khó";
            default:
                return "Không rõ";
        }
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public int getDoKho() {
        return doKho;
    }

    public void setDoKho(int doKho) {
        this.doKho = doKho;
    }

    // ===== PHƯƠNG THỨC TRỪU TƯỢNG: mỗi loại câu hỏi tự cài đặt (ĐA HÌNH) =====

    /** Tên loại để hiển thị, ví dụ "Một đáp án". */
    public abstract String getLoai();

    /** Ký hiệu loại ở đầu dòng file: MOT, NHIEU, DUNGSAI. */
    public abstract String getKyHieuLoai();

    /** Kiểm tra câu trả lời có hoàn toàn đúng không. */
    public abstract boolean kiemTraDapAn(String traLoi);

    /** Kiểm tra câu trả lời có đúng định dạng của loại câu hỏi không (để báo nhập lại). */
    public abstract boolean laTraLoiHopLe(String traLoi);

    /** Hướng dẫn cách nhập câu trả lời. */
    public abstract String getHuongDanTraLoi();

    /** Đáp án đúng dạng chuỗi để in khi xem kết quả. */
    public abstract String getDapAnDungHienThi();

    /** In câu hỏi cho thí sinh làm bài (KHÔNG hiện đáp án). */
    public abstract void hienThiKhiThi(int soThuTu);

    /** In đầy đủ câu hỏi kèm đáp án (dành cho quản lý). */
    public abstract void hienThi();

    /** Chuỗi một dòng để ghi file. */
    public abstract String toFileString();

    // ===== CÀI ĐẶT INTERFACE IChamDiem =====

    /** Mặc định: đúng hoàn toàn được trọn điểm, sai được 0 điểm. Gọi kiemTraDapAn() ĐA HÌNH. */
    @Override
    public double chamDiem(String traLoi, double diemToiDa) {
        return kiemTraDapAn(traLoi) ? diemToiDa : 0.0;
    }

    /** Phần chung khi ghi file: LOAI|ma|noiDung|maMonHoc|doKho. */
    protected String thongTinChungFile() {
        return getKyHieuLoai() + "|" + ma + "|" + noiDung + "|" + maMonHoc + "|" + doKho;
    }

    /** In phần đầu dùng chung khi xem câu hỏi. */
    protected void inPhanChung() {
        System.out.printf("[%s] %s | Môn: %s | Độ khó: %s%n", ma, getLoai(), maMonHoc, tenDoKho(doKho));
        System.out.println("    " + noiDung);
    }
}
