package dto;

/**
 *
 * @author xxseokkixx
 */
public class AdminInfoDTO {

    private int adminNum;
    private String adminId;
    private String adminPw;
    private String adminName;
    private String adminPosition;
    private String adminHireDate;

    public int getAdminNum() {
        return adminNum;
    }

    public void setAdminNum(int adminNum) {
        this.adminNum = adminNum;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPw() {
        return adminPw;
    }

    public void setAdminPw(String adminPw) {
        this.adminPw = adminPw;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPosition() {
        return adminPosition;
    }

    public void setAdminPosition(String adminPosition) {
        this.adminPosition = adminPosition;
    }

    public String getAdminHireDate() {
        return adminHireDate;
    }

    public void setAdminHireDate(String adminHireDate) {
        this.adminHireDate = adminHireDate;
    }

}
