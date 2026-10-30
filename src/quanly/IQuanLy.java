package quanly;

public interface IQuanLy<T> {
    void them(T obj);
    void sua(String ma);
    void xoa(String ma);
    T timTheoMa(String ma);
    void hienThi();
}