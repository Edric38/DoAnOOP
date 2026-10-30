package quanly;

public class Main {
    public static void main(String[] args) {
        DanhSachNguoiDung dsnd = new DanhSachNguoiDung();
        
        // Đặt đường dẫn tuyệt đối trỏ thẳng vào thư mục gốc dự án của bạn trên máy tính
        String tenFile = "C:/Users/PC/DoanJava/DoAnOOP/nguoidung.txt";
        
        // Tự động đọc dữ liệu từ file (nếu đã có file từ trước)
        dsnd.docFile(tenFile);
        
        System.out.println("\n========================================");
        System.out.println("   CHUONG TRINH QUAN LY THI TRAC NGHIEM ");
        System.out.println("   Phan: Quan Ly Nguoi Dung (Nguyen Khanh Duy)");
        System.out.println("========================================");
        
        // Mở menu tương tác người dùng
        MenuNguoiDung.hienThiMenu(dsnd, tenFile);
        
        // Tự động lưu dữ liệu vào file khi người dùng chọn thoát chương trình (chọn 0)
        dsnd.ghiFile(tenFile);
        System.out.println("Da luu du lieu vao file. Tam biet!");
    }
}