package quanly;

/**
 * INTERFACE: hợp đồng quản lý chung (thêm, sửa, xóa, tìm, hiển thị).
 * Được implement bởi lớp trừu tượng DanhSach<T>, nên mọi lớp danh sách đều có đủ 5 thao tác này.
 *
 * @param <T> kiểu đối tượng được quản lý
 */
public interface IQuanLy<T> {

    /** Thêm một đối tượng; trả về false nếu trùng mã. */
    boolean them(T doiTuong);

    /** Thay đối tượng có mã {@code ma} bằng đối tượng mới; trả về false nếu không tìm thấy. */
    boolean sua(String ma, T doiTuongMoi);

    /** Xóa đối tượng theo mã; trả về false nếu không tìm thấy. */
    boolean xoa(String ma);

    /** Tìm đối tượng theo mã (không phân biệt hoa thường); không có thì trả về null. */
    T timTheoMa(String ma);

    /** In toàn bộ danh sách ra màn hình. */
    void hienThiTatCa();
}
