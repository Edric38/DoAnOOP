package quanly;

import java.util.ArrayList;
import java.util.List;

/**
 * KẾ THỪA: lớp danh sách người dùng, extends DanhSach<NguoiDung>.
 * ĐA HÌNH: một ArrayList<NguoiDung> chứa lẫn ThiSinh và GiaoVien;
 * gọi nd.hienThi(), nd.toFileString(), nd.getVaiTro() sẽ chạy đúng phiên bản của lớp con.
 * Tìm kiếm: theo mã, theo tên, theo vai trò, theo email.
 */
public class DanhSachNguoiDung extends DanhSach<NguoiDung> {

    public static final String TEN_FILE = "nguoidung.txt";

    public DanhSachNguoiDung() {
        super(THU_MUC_MAC_DINH + "/" + TEN_FILE);
    }

    public DanhSachNguoiDung(String duongDanFile) {
        super(duongDanFile);
    }

    @Override
    protected String layMa(NguoiDung doiTuong) {
        return doiTuong.getMa();
    }

    /** Dựa vào ký hiệu loại đầu dòng để tạo đúng lớp con. */
    @Override
    protected NguoiDung phanTichDong(String dong) {
        String[] t = tachTruong(dong, 6);
        String loai = t[0].toUpperCase();
        String ma = t[1].toUpperCase();
        if (!NhapLieu.kiemTraEmail(t[4])) {
            throw new IllegalArgumentException("email không hợp lệ");
        }
        if (loai.equals(ThiSinh.KY_HIEU_LOAI)) {
            return new ThiSinh(ma, t[2], NhapLieu.docNgay(t[3]), t[4], t[5]);
        }
        if (loai.equals(GiaoVien.KY_HIEU_LOAI)) {
            return new GiaoVien(ma, t[2], NhapLieu.docNgay(t[3]), t[4], t[5]);
        }
        throw new IllegalArgumentException("loại người dùng không hợp lệ: " + t[0]);
    }

    @Override
    protected String chuyenThanhDong(NguoiDung doiTuong) {
        return doiTuong.toFileString(); // ĐA HÌNH
    }

    @Override
    protected void hienThiPhanTu(NguoiDung doiTuong) {
        doiTuong.hienThi(); // ĐA HÌNH
    }

    @Override
    protected void inTieuDe() {
        NguoiDung.inTieuDe();
    }

    public List<NguoiDung> timTheoTen(String tuKhoa) {
        List<NguoiDung> ketQua = new ArrayList<>();
        for (NguoiDung nd : danhSach) {
            if (NhapLieu.chuaTuKhoa(nd.getHoTen(), tuKhoa)) {
                ketQua.add(nd);
            }
        }
        return ketQua;
    }

    /** Tìm theo vai trò: gọi getVaiTro() ĐA HÌNH. */
    public List<NguoiDung> timTheoVaiTro(String vaiTro) {
        List<NguoiDung> ketQua = new ArrayList<>();
        for (NguoiDung nd : danhSach) {
            if (nd.getVaiTro().equalsIgnoreCase(vaiTro)) {
                ketQua.add(nd);
            }
        }
        return ketQua;
    }

    public List<NguoiDung> timTheoEmail(String tuKhoa) {
        List<NguoiDung> ketQua = new ArrayList<>();
        for (NguoiDung nd : danhSach) {
            if (nd.getEmail().toLowerCase().contains(tuKhoa.toLowerCase().trim())) {
                ketQua.add(nd);
            }
        }
        return ketQua;
    }

    /** Lấy riêng các thí sinh (dùng cho buổi thi). */
    public List<ThiSinh> layDanhSachThiSinh() {
        List<ThiSinh> ketQua = new ArrayList<>();
        for (NguoiDung nd : danhSach) {
            if (nd instanceof ThiSinh) {
                ketQua.add((ThiSinh) nd);
            }
        }
        return ketQua;
    }
}
