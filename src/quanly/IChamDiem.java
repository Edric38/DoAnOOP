package quanly;

/**
 * INTERFACE: hợp đồng chấm điểm một câu trả lời.
 * Được implement bởi lớp trừu tượng CauHoi; lớp CauHoiNhieuDapAn override lại để chấm điểm từng phần.
 */
public interface IChamDiem {

    /**
     * Chấm điểm câu trả lời.
     *
     * @param traLoi     câu trả lời thí sinh nhập
     * @param diemToiDa  số điểm tối đa của câu
     * @return số điểm đạt được (từ 0 đến diemToiDa)
     */
    double chamDiem(String traLoi, double diemToiDa);
}
