package quanly;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LỚP TRỪU TƯỢNG (abstract) cho mọi "lớp mảng đối tượng", bên trong dùng ArrayList.
 * INTERFACE: implements IQuanLy<T> (CRUD) và IFileIO (đọc/ghi file).
 * Phần chung (thêm/sửa/xóa/tìm/đọc/ghi file) viết MỘT lần ở đây;
 * lớp con chỉ override các PHƯƠNG THỨC TRỪU TƯỢNG riêng: lấy mã, đọc một dòng, ghi một dòng, in một phần tử.
 * KẾ THỪA: DanhSachMonHoc, DanhSachNguoiDung, DanhSachCauHoi, DanhSachDeThi, DanhSachKetQua.
 *
 * @param <T> kiểu phần tử
 */
public abstract class DanhSach<T> implements IQuanLy<T>, IFileIO {

    // THUỘC TÍNH STATIC: thư mục dữ liệu mặc định
    public static final String THU_MUC_MAC_DINH = "data";

    protected ArrayList<T> danhSach;
    protected String duongDanFile;

    public DanhSach() {
        this("");
    }

    public DanhSach(String duongDanFile) {
        this.danhSach = new ArrayList<>();
        this.duongDanFile = duongDanFile;
    }

    // ===== PHƯƠNG THỨC TRỪU TƯỢNG: lớp con bắt buộc cài đặt =====

    /** Lấy mã của một phần tử. */
    protected abstract String layMa(T doiTuong);

    /** Chuyển một dòng file thành đối tượng; dòng sai thì ném ngoại lệ. */
    protected abstract T phanTichDong(String dong);

    /** Chuyển một đối tượng thành một dòng file. */
    protected abstract String chuyenThanhDong(T doiTuong);

    /** In một phần tử ra màn hình. */
    protected abstract void hienThiPhanTu(T doiTuong);

    /** In tiêu đề bảng. */
    protected abstract void inTieuDe();

    // ===== CÀI ĐẶT INTERFACE IQuanLy<T> =====

    private int viTri(String ma) {
        if (ma == null) {
            return -1;
        }
        for (int i = 0; i < danhSach.size(); i++) {
            if (layMa(danhSach.get(i)).equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    /** Thêm và ghi file ngay; trùng mã thì không thêm. */
    @Override
    public boolean them(T doiTuong) {
        if (doiTuong == null || viTri(layMa(doiTuong)) >= 0) {
            return false;
        }
        danhSach.add(doiTuong);
        luu();
        return true;
    }

    /** Sửa và ghi file ngay; mã của đối tượng mới phải trùng mã cũ. */
    @Override
    public boolean sua(String ma, T doiTuongMoi) {
        int i = viTri(ma);
        if (i < 0 || doiTuongMoi == null || !layMa(doiTuongMoi).equalsIgnoreCase(ma.trim())) {
            return false;
        }
        danhSach.set(i, doiTuongMoi);
        luu();
        return true;
    }

    /** Xóa và ghi file ngay. */
    @Override
    public boolean xoa(String ma) {
        int i = viTri(ma);
        if (i < 0) {
            return false;
        }
        danhSach.remove(i);
        luu();
        return true;
    }

    @Override
    public T timTheoMa(String ma) {
        int i = viTri(ma);
        return i < 0 ? null : danhSach.get(i);
    }

    @Override
    public void hienThiTatCa() {
        hienThiDanhSach(danhSach);
    }

    /** In một danh sách bất kỳ (dùng cho kết quả tìm kiếm). */
    public void hienThiDanhSach(List<T> ds) {
        if (ds.isEmpty()) {
            System.out.println("(Không có dữ liệu phù hợp)");
            return;
        }
        inTieuDe();
        for (T dt : ds) {
            hienThiPhanTu(dt);
        }
        System.out.println("Tổng cộng: " + ds.size());
    }

    public List<T> getTatCa() {
        return Collections.unmodifiableList(danhSach);
    }

    public int soLuong() {
        return danhSach.size();
    }

    public String getDuongDanFile() {
        return duongDanFile;
    }

    public void setDuongDanFile(String duongDanFile) {
        this.duongDanFile = duongDanFile;
    }

    /** Ghi danh sách ra file mặc định. */
    public boolean luu() {
        return ghiFile(duongDanFile);
    }

    /** Đọc lại danh sách từ file mặc định. */
    public int docLai() {
        return docFile(duongDanFile);
    }

    // ===== CÀI ĐẶT INTERFACE IFileIO =====

    @Override
    public int docFile(String duongDan) {
        if (duongDan == null || duongDan.isBlank()) {
            System.out.println("Lỗi: chưa cấu hình đường dẫn file.");
            return 0;
        }
        Path p = Paths.get(duongDan);
        try {
            taoFileNeuChuaCo(p);
            List<String> cacDong = Files.readAllLines(p, StandardCharsets.UTF_8);
            danhSach.clear();
            int soDong = 0;
            for (String dong : cacDong) {
                soDong++;
                String d = dong.replace("﻿", "").trim(); // bỏ BOM nếu file lưu bằng Notepad
                if (d.isEmpty() || d.startsWith("#")) {
                    continue;
                }
                try {
                    T dt = phanTichDong(d); // ĐA HÌNH: lớp con quyết định tạo đối tượng gì
                    String ma = layMa(dt);
                    if (ma == null || ma.isBlank()) {
                        throw new IllegalArgumentException("mã trống");
                    }
                    if (viTri(ma) >= 0) {
                        throw new IllegalArgumentException("trùng mã " + ma);
                    }
                    danhSach.add(dt);
                } catch (NumberFormatException e) {
                    baoDongLoi(duongDan, soDong, "số không hợp lệ");
                } catch (DateTimeParseException e) {
                    baoDongLoi(duongDan, soDong, "ngày không hợp lệ");
                } catch (RuntimeException e) {
                    baoDongLoi(duongDan, soDong, e.getMessage());
                }
            }
            return danhSach.size();
        } catch (IOException e) {
            System.out.println("Lỗi đọc file " + duongDan + " (kiểm tra file có lưu dạng UTF-8 không): " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean ghiFile(String duongDan) {
        if (duongDan == null || duongDan.isBlank()) {
            System.out.println("Lỗi: chưa cấu hình đường dẫn file.");
            return false;
        }
        Path p = Paths.get(duongDan);
        try {
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            List<String> cacDong = new ArrayList<>();
            for (T dt : danhSach) {
                cacDong.add(chuyenThanhDong(dt)); // ĐA HÌNH: mỗi đối tượng tự biết cách ghi
            }
            Files.write(p, cacDong, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.out.println("Lỗi ghi file " + duongDan + ": " + e.getMessage());
            return false;
        }
    }

    // ===== HỖ TRỢ =====

    private static void taoFileNeuChuaCo(Path p) throws IOException {
        if (p.getParent() != null) {
            Files.createDirectories(p.getParent());
        }
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
    }

    private static void baoDongLoi(String duongDan, int soDong, String lyDo) {
        System.out.println("Cảnh báo: bỏ qua dòng " + soDong + " trong " + duongDan + " (" + lyDo + ")");
    }

    /** STATIC: tách dòng theo '|' và kiểm tra đủ số trường. */
    protected static String[] tachTruong(String dong, int soTruongToiThieu) {
        String[] t = dong.split("\\|", -1);
        if (t.length < soTruongToiThieu) {
            throw new IllegalArgumentException("thiếu trường dữ liệu, cần ít nhất " + soTruongToiThieu);
        }
        for (int i = 0; i < t.length; i++) {
            t[i] = t[i].trim();
        }
        return t;
    }

    /** STATIC: kiểm tra ký hiệu loại ở đầu dòng. */
    protected static void kiemTraLoai(String thucTe, String mongDoi) {
        if (!thucTe.equalsIgnoreCase(mongDoi)) {
            throw new IllegalArgumentException("ký hiệu loại phải là " + mongDoi + " nhưng gặp " + thucTe);
        }
    }
}
