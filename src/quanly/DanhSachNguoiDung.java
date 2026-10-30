package quanly;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DanhSachNguoiDung extends DanhSach<NguoiDung> implements IQuanLy<NguoiDung> {

    @Override
    public void them(NguoiDung obj) {
        danhSach.add(obj);
        System.out.println("Them nguoi dung thanh cong!");
    }

    @Override
    public void sua(String ma) {
        NguoiDung nd = timTheoMa(ma);
        if (nd == null) {
            System.out.println("Khong tim thay nguoi dung co ma: " + ma);
            return;
        }
        System.out.println("Dang sua thong tin cho: " + nd.getHoTen());
        nd.setHoTen(NhapLieu.nhapString("Nhap ho ten moi: "));
        nd.setEmail(NhapLieu.nhapEmail("Nhap email moi: "));
        nd.setSoDienThoai(NhapLieu.nhapString("Nhap so dien thoai moi: "));
        if (nd instanceof ThiSinh) {
            ((ThiSinh) nd).setLop(NhapLieu.nhapString("Nhap lop moi: "));
        } else if (nd instanceof GiaoVien) {
            ((GiaoVien) nd).setBoMon(NhapLieu.nhapString("Nhap bo mon moi: "));
        }
        System.out.println("Cap nhat thong tin thanh cong!");
    }

    @Override
    public void xoa(String ma) {
        NguoiDung nd = timTheoMa(ma);
        if (nd == null) {
            System.out.println("Khong tim thay nguoi dung co ma: " + ma);
            return;
        }
        danhSach.remove(nd);
        System.out.println("Xoa nguoi dung thanh cong!");
    }

    @Override
    public NguoiDung timTheoMa(String ma) {
        for (NguoiDung nd : danhSach) {
            if (nd.getMaNguoiDung().equalsIgnoreCase(ma)) {
                return nd;
            }
        }
        return null;
    }

    @Override
    public void hienThi() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sach nguoi dung dang trong!");
            return;
        }
        for (NguoiDung nd : danhSach) {
            nd.hienThiThongTin();
        }
    }

    // Các hàm tìm kiếm chuyên biệt theo yêu cầu phân công
    public List<NguoiDung> timTheoTen(String ten) {
        List<NguoiDung> ketQua = new ArrayList<>();
        for (NguoiDung nd : danhSach) {
            if (nd.getHoTen().toLowerCase().contains(ten.toLowerCase())) {
                ketQua.add(nd);
            }
        }
        return ketQua;
    }

    public List<NguoiDung> timTheoVaiTro(String vaiTro) {
        List<NguoiDung> ketQua = new ArrayList<>();
        for (NguoiDung nd : danhSach) {
            if (vaiTro.equalsIgnoreCase("thisinh") && nd instanceof ThiSinh) {
                ketQua.add(nd);
            } else if (vaiTro.equalsIgnoreCase("giaovien") && nd instanceof GiaoVien) {
                ketQua.add(nd);
            }
        }
        return ketQua;
    }

    public NguoiDung timTheoEmail(String email) {
        for (NguoiDung nd : danhSach) {
            if (nd.getEmail().equalsIgnoreCase(email)) {
                return nd;
            }
        }
        return null;
    }

    @Override
    public void docFile(String tenFile) {
        File file = new File(tenFile);
        if (!file.exists()) {
            System.out.println("File " + tenFile + " chua ton tai.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int dong = 0;
            while ((line = br.readLine()) != null) {
                dong++;
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.out.println("Loi dong hong tai dong " + dong);
                    continue;
                }
                String loai = parts[0].trim();
                String ma = parts[1].trim();
                String hoTen = parts[2].trim();
                String email = parts[3].trim();
                String sdt = parts[4].trim();
                String thuocTinhRieng = parts[5].trim();

                if (loai.equalsIgnoreCase("TS")) {
                    danhSach.add(new ThiSinh(ma, hoTen, email, sdt, thuocTinhRieng));
                } else if (loai.equalsIgnoreCase("GV")) {
                    danhSach.add(new GiaoVien(ma, hoTen, email, sdt, thuocTinhRieng));
                }
            }
            System.out.println("Doc file nguoi dung thanh cong!");
        } catch (IOException e) {
            System.out.println("Loi doc file: " + e.getMessage());
        }
    }

    @Override
    public void ghiFile(String tenFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tenFile))) {
            for (NguoiDung nd : danhSach) {
                bw.write(nd.toString());
                bw.newLine();
            }
            System.out.println("Ghi file nguoi dung thanh cong!");
        } catch (IOException e) {
            System.out.println("Loi ghi file: " + e.getMessage());
        }
    }
}