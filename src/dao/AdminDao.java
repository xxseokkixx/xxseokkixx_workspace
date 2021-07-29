package dao;

import connection.MyCon;
import dto.AdminInfoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xxseokkixx
 */
public class AdminDao {

    private static AdminDao daoA;
    private String url; // jdbc
    private String id; // ID
    private String pw; // PassWord

    public AdminDao() {
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        id = "semiproA";
        pw = "semi1";
    }

    public static AdminDao getDao() {
        if (daoA == null) {
            daoA = new AdminDao();
        }
        return daoA;
    }

    public Map<String, String> loginAdmin(String adminid, String adminpw) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select count(*) cnt, adminid, adminname from admininfo "
                + "where adminid = ? and adminpw = ? group by adminid, adminname";
        Map<String, String> maps = new HashMap<>();
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, adminid);
            pstmt.setString(2, adminpw);
            rs = pstmt.executeQuery();
            System.out.println(rs);
            if (rs.next()) {
                System.out.println(rs.getInt("cnt"));
                System.out.println(rs.getString("adminid"));
                maps.put("cnt", String.valueOf(rs.getInt("cnt")));
                maps.put("adminid", rs.getString("adminid"));
                maps.put("adminname", rs.getString("adminname"));
            } else {
                maps.put("cnt", "0");
                maps.put("adminid", "No");
                maps.put("adminname", "No");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("����");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return maps;
    }

    // ��� �����ڸ� ����ϴ� method.
    public ArrayList<AdminInfoDTO> searchAllAdmin() {
        // �˻������� ������ arrLIst.
        ArrayList<AdminInfoDTO> adminArr = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select adminnum, adminid, adminname, adminposition, adminhiredate "
                + "from admininfo order by 1 asc";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                AdminInfoDTO vo = new AdminInfoDTO();
                vo.setAdminNum(rs.getInt("adminnum")); // �����ڹ�ȣ
                vo.setAdminId(rs.getString("adminid")); // ������id
                vo.setAdminName(rs.getString("adminname")); // �����ڸ�
                vo.setAdminPosition(rs.getString("adminposition")); // ������ ��å
                vo.setAdminHireDate(rs.getString("adminhiredate")); // �Ի�����.
                adminArr.add(vo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("����");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adminArr;
    }

    // �޺��ڽ� ���� �� �˻������� �����ڸ� �˻��ϴ� �޼ҵ�.
    public ArrayList<AdminInfoDTO> searchPrintAdmin(String input, int index) {
        // �˻������� ������ arrLIst.
        ArrayList<AdminInfoDTO> adminArr = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = MyCon.getConn(url, id, pw);
            switch (index) {
                case 0:
                    sql = "select adminnum, adminid, adminname, adminposition, adminhiredate "
                            + "from admininfo where adminid like ? order by 1 asc";
                    break;
                case 1:
                    sql = "select adminnum, adminid, adminname, adminposition, adminhiredate "
                            + "from admininfo where adminname like ? order by 1 asc";
                    break;
                case 2:
                    sql = "select adminnum, adminid, adminname, adminposition, adminhiredate "
                            + "from admininfo where adminposition like ? order by 1 asc";
                    break;
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + input + "%");
            rs = pstmt.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                AdminInfoDTO vo = new AdminInfoDTO();
                vo.setAdminNum(rs.getInt("adminnum")); // �����ڹ�ȣ
                vo.setAdminId(rs.getString("adminid")); // ������id
                vo.setAdminName(rs.getString("adminname")); // �����ڸ�
                vo.setAdminPosition(rs.getString("adminposition")); // ������ ��å
                vo.setAdminHireDate(rs.getString("adminhiredate")); // �Ի�����.
                adminArr.add(vo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("����");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adminArr;
    }

    public static void main(String[] args) {
        Map<String, String> res = AdminDao.getDao().loginAdmin("ad1", "111");
        System.out.println(res.get("cnt"));
        System.out.println(res.get("adminid"));
    }
}
