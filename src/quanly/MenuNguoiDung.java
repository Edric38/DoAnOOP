package quanly;

import java.io.File;
import java.util.List;

public class MenuNguoiDung {
    
    // Hiển thị menu quản lý người dùng chính
    public static void hienThiMenu(DanhSachNguoiDung dsnd, String tenFile) {
        int chon;
        do {
            System.out.println("\n========================================");
            System.out.println("       QUAN LY DANH SACH NGUOI DUNG     ");
            System.out.println("========================================");
            System.out.println("1. Hien thi danh sach nguoi dung");
            System.out.println("2. Them nguoi dung moi (Thi sinh / Giao vien)");
            System.out.println("3. Sua thong tin nguoi dung theo ma");
            System.out.println("4. Xoa nguoi dung theo ma");
            System.out.println("5. Tim kiem nguoi dung");
            System.out.println("6. Quan ly File (Doc/Ghi/Thong tin file)");
            System.out.println("0. Quay lai menu chinh");
            System.out.println("----------------------------------------");
            chon = NhapLieu.nhapInt("Chon chuc nang (0-6): ");

            switch (chon) {
                case 1:
                    System.out.println("\n--- DANH SACH NGUOI DUNG ---");
                    dsnd.hienThi();
                    break;
                case 2:
                    themNguoiDung(dsnd);
                    break;
                case 3:
                    String maSua = NhapLieu.nhapString("Nhap ma nguoi dung can sua: ");
                    dsnd.sua(maSua);
                    break;
                case 4:
                    String maXoa = NhapLieu.nhapString("Nhap ma nguoi dung can xoa: ");
                    dsnd.xoa(maXoa);
                    break;
                case 5:
                    timNguoiDung(dsnd);
                    break;
                case 6:
                    menuFile(dsnd, tenFile);
                    break;
                case 0:
                    System.out.println("Dang quay lai menu chinh...");
                    break;
                default:
                    System.out.println("Loi: Lua chon khong hop le! Vui long chon tu 0 den 6.");
            }
        } while (chon != 0);
    }

    // Hàm thêm người dùng mới
    public static void themNguoiDung(DanhSachNguoiDung dsnd) {
        System.out.println("\n--- THEM NGUOI DUNG MOI ---");
        System.out.println("1. Them Thi Sinh");
        System.out.println("2. Them Giao Vien");
        int loai = NhapLieu.nhapInt("Chon loai doi tuong (1-2): ");

        String ma = NhapLieu.nhapString("Nhap ma nguoi dung: ");
        if (dsnd.timTheoMa(ma) != null) {
            System.out.println("Loi: Ma nguoi dung nay da ton tai trong danh sach!");
            return;
        }
        String hoTen = NhapLieu.nhapString("Nhap ho ten: ");
        String email = NhapLieu.nhapEmail("Nhap email: ");
        String sdt = NhapLieu.nhapString("Nhap so dien thoai: ");

        if (loai == 1) {
            String lop = NhapLieu.nhapString("Nhap ten lop cua thi sinh: ");
            ThiSinh ts = new ThiSinh(ma, hoTen, email, sdt, lop);
            dsnd.them(ts);
        } else if (loai == 2) {
            String boMon = NhapLieu.nhapString("Nhap ten bo mon cua giao vien: ");
            GiaoVien gv = new GiaoVien(ma, hoTen, email, sdt, boMon);
            dsnd.them(gv);
        } else {
            System.out.println("Loi: Lua chon loai khong hop le!");
        }
    }

    // Hàm tìm kiếm người dùng theo nhiều tiêu chí
    public static void timNguoiDung(DanhSachNguoiDung dsnd) {
        System.out.println("\n--- TIM KIEM NGUOI DUNG ---");
        System.out.println("1. Tim theo ma nguoi dung");
        System.out.println("2. Tim theo ho ten");
        System.out.println("3. Tim theo email");
        System.out.println("4. Tim theo vai tro (ThiSinh / GiaoVien)");
        int chon = NhapLieu.nhapInt("Chon phuong thuc tim kiem (1-4): ");

        switch (chon) {
            case 1:
                String ma = NhapLieu.nhapString("Nhap ma can tim: ");
                NguoiDung nd = dsnd.timTheoMa(ma);
                if (nd != null) {
                    System.out.println("Da tim thay:");
                    nd.hienThiThongTin();
                } else {
                    System.out.println("Khong tim thay nguoi dung co ma: " + ma);
                }
                break;
            case 2:
                String ten = NhapLieu.nhapString("Nhap tu khoa ten can tim: ");
                List<NguoiDung> kqTen = dsnd.timTheoTen(ten);
                if (kqTen.isEmpty()) {
                    System.out.println("Khong tim thay nguoi dung nao chua ten: " + ten);
                } else {
                    System.out.println("Ket qua tim kiem (" + kqTen.size() + "):");
                    for (NguoiDung x : kqTen) x.hienThiThongTin();
                }
                break;
            case 3:
                String email = NhapLieu.nhapString("Nhap email can tim: ");
                NguoiDung kqEmail = dsnd.timTheoEmail(email);
                if (kqEmail != null) {
                    System.out.println("Da tim thay:");
                    kqEmail.hienThiThongTin();
                } else {
                    System.out.println("Khong tim thay nguoi dung co email: " + email);
                }
                break;
            case 4:
                String vaiTro = NhapLieu.nhapString("Nhap vai tro (thisinh hoac giaovien): ");
                List<NguoiDung> kqVaiTro = dsnd.timTheoVaiTro(vaiTro);
                if (kqVaiTro.isEmpty()) {
                    System.out.println("Khong tim thay nguoi dung nao thuoc vai tro: " + vaiTro);
                } else {
                    System.out.println("Ket qua tim kiem (" + kqVaiTro.size() + "):");
                    for (NguoiDung x : kqVaiTro) x.hienThiThongTin();
                }
                break;
            default:
                System.out.println("Loi: Lua chon khong hop le!");
        }
    }

    // Menu quản lý file
    public static void menuFile(DanhSachNguoiDung dsnd, String tenFile) {
        System.out.println("\n--- QUAN LY FILE NGUOI DUNG ---");
        System.out.println("1. Doc du lieu tu file (" + tenFile + ")");
        System.out.println("2. Ghi du lieu vao file (" + tenFile + ")");
        System.out.println("3. Hien thi thong tin chi tiet file");
        int chon = NhapLieu.nhapInt("Chon chuc nang file (1-3): ");

        switch (chon) {
            case 1:
                dsnd.docFile(tenFile);
                break;
            case 2:
                dsnd.ghiFile(tenFile);
                break;
            case 3:
                inThongTinFile(tenFile);
                break;
            default:
                System.out.println("Loi: Lua chon khong hop le!");
        }
    }

    // Hàm in thông tin chi tiết của file
    public static void inThongTinFile(String tenFile) {
        File file = new File(tenFile);
        System.out.println("\n--- THONG TIN CHI TIET FILE ---");
        System.out.println("Ten file: " + file.getName());
        System.out.println("Duong dan tuyet doi: " + file.getAbsolutePath());
        System.out.println("Trang thai ton tai: " + (file.exists() ? "Da ton tai" : "Chua ton tai"));
        if (file.exists()) {
            System.out.println("Kich thuoc file: " + file.length() + " bytes");
            System.out.println("Quyen doc: " + (file.canRead() ? "Co" : "Khong"));
            System.out.println("Quyen ghi: " + (file.canWrite() ? "Co" : "Khong"));
        }
    }
}