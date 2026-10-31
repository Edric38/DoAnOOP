package quanly;

/**
 * Bộ cộng dồn số liệu điểm của MỘT nhóm kết quả thi (một đề, một môn, một thí sinh, một lớp, một tháng...).
 * Mỗi lần gọi them(diem) sẽ cập nhật: số lượt, tổng, cao nhất, thấp nhất, số bài đạt và số bài theo xếp loại.
 * Lớp ThongKe tạo nhiều đối tượng ThongKeDiem (mỗi nhóm một đối tượng) rồi in ra bảng.
 */
public class ThongKeDiem {

    // THUỘC TÍNH STATIC: ngưỡng điểm đạt dùng chung
    public static final double DIEM_DAT = 5.0;

    // THUỘC TÍNH STATIC: thứ tự 4 mức xếp loại, trùng với KetQuaThi.xepLoai()
    public static final String[] CAC_XEP_LOAI = {"Giỏi", "Khá", "Trung bình", "Yếu"};

    private String tenNhom;
    private int soLuot;
    private double tongDiem;
    private double tongBinhPhuong;
    private double diemCaoNhat;
    private double diemThapNhat;
    private int soDat;
    private int[] soTheoXepLoai;

    public ThongKeDiem() {
        this("");
    }

    public ThongKeDiem(String tenNhom) {
        this.tenNhom = tenNhom;
        this.soLuot = 0;
        this.tongDiem = 0;
        this.tongBinhPhuong = 0;
        this.diemCaoNhat = 0;
        this.diemThapNhat = 0;
        this.soDat = 0;
        this.soTheoXepLoai = new int[CAC_XEP_LOAI.length];
    }

    /** Cộng thêm một điểm vào nhóm. */
    public void them(double diem) {
        if (soLuot == 0) {
            diemCaoNhat = diem;
            diemThapNhat = diem;
        } else {
            diemCaoNhat = Math.max(diemCaoNhat, diem);
            diemThapNhat = Math.min(diemThapNhat, diem);
        }
        soLuot++;
        tongDiem += diem;
        tongBinhPhuong += diem * diem;
        if (diem >= DIEM_DAT) {
            soDat++;
        }
        soTheoXepLoai[viTriXepLoai(KetQuaThi.xepLoai(diem))]++;
    }

    /** Cộng thêm một kết quả thi. */
    public void them(KetQuaThi kq) {
        them(kq.getDiem());
    }

    /** STATIC: vị trí của một xếp loại trong mảng CAC_XEP_LOAI. */
    public static int viTriXepLoai(String xepLoai) {
        for (int i = 0; i < CAC_XEP_LOAI.length; i++) {
            if (CAC_XEP_LOAI[i].equals(xepLoai)) {
                return i;
            }
        }
        return CAC_XEP_LOAI.length - 1;
    }

    public String getTenNhom() {
        return tenNhom;
    }

    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }

    public int getSoLuot() {
        return soLuot;
    }

    public double getTongDiem() {
        return tongDiem;
    }

    public double getTrungBinh() {
        return soLuot == 0 ? 0 : tongDiem / soLuot;
    }

    /** Độ lệch chuẩn (cho biết điểm phân tán nhiều hay ít quanh điểm trung bình). */
    public double getDoLechChuan() {
        if (soLuot == 0) {
            return 0;
        }
        double tb = getTrungBinh();
        double phuongSai = tongBinhPhuong / soLuot - tb * tb;
        return Math.sqrt(Math.max(0, phuongSai));
    }

    public double getDiemCaoNhat() {
        return diemCaoNhat;
    }

    public double getDiemThapNhat() {
        return diemThapNhat;
    }

    public int getSoDat() {
        return soDat;
    }

    public int getSoKhongDat() {
        return soLuot - soDat;
    }

    /** Tỉ lệ đạt theo phần trăm (0-100). */
    public double getTiLeDat() {
        return soLuot == 0 ? 0 : soDat * 100.0 / soLuot;
    }

    public int getSoTheoXepLoai(String xepLoai) {
        return soTheoXepLoai[viTriXepLoai(xepLoai)];
    }

    public boolean coDuLieu() {
        return soLuot > 0;
    }
}
