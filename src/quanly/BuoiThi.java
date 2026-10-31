package quanly;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Tổ chức một buổi thi: chọn thí sinh, chọn đề, làm bài có tính giờ, chấm điểm, lưu KetQuaThi.
 * ĐA HÌNH: danh sách ArrayList<CauHoi> chứa đủ 3 loại câu hỏi; khi gọi
 * hienThiKhiThi(), laTraLoiHopLe(), kiemTraDapAn(), chamDiem() thì Java tự chọn
 * phiên bản của lớp con thực tế (dynamic binding).
 */
public class BuoiThi {

    private DanhSachNguoiDung dsNguoiDung;
    private DanhSachDeThi dsDeThi;
    private DanhSachCauHoi dsCauHoi;
    private DanhSachKetQua dsKetQua;

    /** Constructor không tham số: tự tạo các danh sách và đọc dữ liệu từ thư mục data/. */
    public BuoiThi() {
        this(new DanhSachNguoiDung(), new DanhSachDeThi(), new DanhSachCauHoi(), new DanhSachKetQua());
        dsNguoiDung.docLai();
        dsDeThi.docLai();
        dsCauHoi.docLai();
        dsKetQua.docLai();
    }

    /** Constructor có tham số: dùng chung các danh sách với chương trình quản lý. */
    public BuoiThi(DanhSachNguoiDung dsNguoiDung, DanhSachDeThi dsDeThi,
                   DanhSachCauHoi dsCauHoi, DanhSachKetQua dsKetQua) {
        this.dsNguoiDung = dsNguoiDung;
        this.dsDeThi = dsDeThi;
        this.dsCauHoi = dsCauHoi;
        this.dsKetQua = dsKetQua;
    }

    /** Luồng chính của buổi thi. */
    public void toChucThi() {
        System.out.println();
        System.out.println("----- LÀM BÀI THI -----");
        List<ThiSinh> cacThiSinh = dsNguoiDung.layDanhSachThiSinh();
        if (cacThiSinh.isEmpty()) {
            System.out.println("Chưa có thí sinh nào. Hãy thêm thí sinh trước.");
            return;
        }
        if (dsDeThi.soLuong() == 0) {
            System.out.println("Chưa có đề thi nào. Hãy tạo đề thi trước.");
            return;
        }
        ThiSinh thiSinh = chonThiSinh(cacThiSinh);
        if (thiSinh == null) {
            System.out.println("Đã hủy buổi thi.");
            return;
        }
        DeThi deThi = chonDeThi();
        if (deThi == null) {
            System.out.println("Đã hủy buổi thi.");
            return;
        }
        if (!NhapLieu.xacNhan("Thí sinh " + thiSinh.getHoTen() + " bắt đầu làm đề " + deThi.getMa() + "?")) {
            System.out.println("Đã hủy buổi thi.");
            return;
        }
        KetQuaThi ketQua = lamBai(thiSinh, deThi);
        if (ketQua != null) {
            if (dsKetQua.them(ketQua)) {
                System.out.println("Đã lưu kết quả " + ketQua.getMa() + " vào file " + dsKetQua.getDuongDanFile() + ".");
            } else {
                System.out.println("Lỗi: không lưu được kết quả (trùng mã " + ketQua.getMa() + ").");
            }
        }
    }

    private ThiSinh chonThiSinh(List<ThiSinh> cacThiSinh) {
        System.out.println("Danh sách thí sinh:");
        for (ThiSinh ts : cacThiSinh) {
            System.out.printf("  %-7s %-24s lớp %s%n", ts.getMa(), ts.getHoTen(), ts.getLop());
        }
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã thí sinh (0 để hủy)").toUpperCase();
            if (ma.equals("0")) {
                return null;
            }
            NguoiDung nd = dsNguoiDung.timTheoMa(ma);
            if (nd instanceof ThiSinh) {
                return (ThiSinh) nd;
            }
            if (nd == null) {
                System.out.println("Lỗi: không tìm thấy người dùng có mã " + ma + ".");
            } else {
                System.out.println("Lỗi: " + ma + " là " + nd.getVaiTro().toLowerCase() + ", không phải thí sinh.");
            }
        }
    }

    private DeThi chonDeThi() {
        System.out.println("Danh sách đề thi:");
        for (DeThi de : dsDeThi.getTatCa()) {
            System.out.printf("  %-7s %-32s môn %-7s %3d phút, %d câu%n", de.getMa(), de.getTenDe(),
                    de.getMaMonHoc(), de.getThoiGianLamBai(), de.getSoCauHoi());
        }
        while (true) {
            String ma = NhapLieu.nhapChuoi("Nhập mã đề thi (0 để hủy)").toUpperCase();
            if (ma.equals("0")) {
                return null;
            }
            DeThi de = dsDeThi.timTheoMa(ma);
            if (de != null) {
                return de;
            }
            System.out.println("Lỗi: không tìm thấy đề thi có mã " + ma + ".");
        }
    }

    /** Làm bài và chấm điểm; trả về KetQuaThi hoặc null nếu đề không có câu hỏi hợp lệ. */
    private KetQuaThi lamBai(ThiSinh thiSinh, DeThi deThi) {
        // ĐA HÌNH: biến kiểu lớp cha CauHoi chứa đối tượng của các lớp con
        ArrayList<CauHoi> cacCauHoi = new ArrayList<>();
        for (String maCH : deThi.getDanhSachMaCauHoi()) {
            CauHoi ch = dsCauHoi.timTheoMa(maCH);
            if (ch == null) {
                System.out.println("Cảnh báo: câu hỏi " + maCH + " không còn tồn tại, bỏ qua.");
            } else {
                cacCauHoi.add(ch);
            }
        }
        if (cacCauHoi.isEmpty()) {
            System.out.println("Đề thi không có câu hỏi hợp lệ, không thể thi.");
            return null;
        }

        int soCau = cacCauHoi.size();
        double diemMoiCau = 10.0 / soCau;
        String[] cacTraLoi = new String[soCau];
        long hetGioLuc = System.currentTimeMillis() + deThi.getThoiGianLamBai() * 60_000L;

        System.out.println();
        System.out.println("Đề: " + deThi.getTenDe() + " | " + soCau + " câu | " + deThi.getThoiGianLamBai() + " phút");
        System.out.println("Nhấn Enter để bỏ qua câu. Hết giờ thì các câu chưa làm được 0 điểm.");

        for (int i = 0; i < soCau; i++) {
            CauHoi ch = cacCauHoi.get(i);
            long conLai = (hetGioLuc - System.currentTimeMillis()) / 1000;
            if (conLai <= 0) {
                System.out.println("HẾT GIỜ! Các câu còn lại không được tính.");
                break;
            }
            System.out.println();
            System.out.printf("(Còn %d phút %02d giây)%n", conLai / 60, conLai % 60);
            ch.hienThiKhiThi(i + 1); // ĐA HÌNH
            System.out.println("  Hướng dẫn: " + ch.getHuongDanTraLoi()); // ĐA HÌNH
            String traLoi;
            while (true) {
                traLoi = NhapLieu.nhapChuoiCoTheRong("  Trả lời");
                if (traLoi.isEmpty() || ch.laTraLoiHopLe(traLoi)) { // ĐA HÌNH
                    break;
                }
                System.out.println("  Lỗi: câu trả lời không hợp lệ. " + ch.getHuongDanTraLoi());
            }
            if (System.currentTimeMillis() > hetGioLuc) {
                System.out.println("HẾT GIỜ! Câu trả lời vừa nhập không được tính.");
                break;
            }
            cacTraLoi[i] = traLoi;
        }

        // ===== CHẤM ĐIỂM ĐA HÌNH =====
        double tongDiem = 0;
        int soCauDung = 0;
        System.out.println();
        System.out.println("===== KẾT QUẢ BÀI THI =====");
        System.out.printf("%-4s %-7s %-14s %-9s %-9s %-6s %s%n", "Câu", "Mã", "Loại", "Trả lời", "Đáp án", "Đúng?", "Điểm");
        for (int i = 0; i < soCau; i++) {
            CauHoi ch = cacCauHoi.get(i);
            String traLoi = cacTraLoi[i] == null ? "" : cacTraLoi[i];
            boolean dung = !traLoi.isEmpty() && ch.kiemTraDapAn(traLoi); // ĐA HÌNH
            IChamDiem boChamDiem = ch; // INTERFACE: biến kiểu interface trỏ tới đối tượng câu hỏi
            double diem = traLoi.isEmpty() ? 0.0 : boChamDiem.chamDiem(traLoi, diemMoiCau);
            if (dung) {
                soCauDung++;
            }
            tongDiem += diem;
            System.out.printf("%-4d %-7s %-14s %-9s %-9s %-6s %.2f%n", i + 1, ch.getMa(), ch.getLoai(),
                    traLoi.isEmpty() ? "(bỏ)" : traLoi.toUpperCase(), ch.getDapAnDungHienThi(),
                    dung ? "Đúng" : "Sai", diem);
        }
        tongDiem = Math.min(10.0, NhapLieu.lamTron2(tongDiem));
        System.out.println("---------------------------------------------");
        System.out.printf("Thí sinh: %s - %s%n", thiSinh.getMa(), thiSinh.getHoTen());
        System.out.printf("Số câu đúng: %d/%d | Điểm: %.2f | Xếp loại: %s%n",
                soCauDung, soCau, tongDiem, KetQuaThi.xepLoai(tongDiem));

        return new KetQuaThi(KetQuaThi.sinhMa(), thiSinh.getMa(), deThi.getMa(),
                tongDiem, soCauDung, soCau, LocalDate.now());
    }
}
