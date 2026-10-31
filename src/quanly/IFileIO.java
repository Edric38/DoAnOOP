package quanly;

/**
 * INTERFACE: hợp đồng đọc/ghi file text.
 * Được implement bởi lớp trừu tượng DanhSach<T>.
 */
public interface IFileIO {

    /**
     * Đọc dữ liệu từ file text UTF-8 vào danh sách (xóa dữ liệu cũ trong bộ nhớ).
     * Nếu file chưa tồn tại thì tự tạo file rỗng.
     *
     * @return số đối tượng đọc được
     */
    int docFile(String duongDan);

    /**
     * Ghi toàn bộ danh sách ra file text UTF-8, mỗi dòng một đối tượng.
     *
     * @return true nếu ghi thành công
     */
    boolean ghiFile(String duongDan);
}
