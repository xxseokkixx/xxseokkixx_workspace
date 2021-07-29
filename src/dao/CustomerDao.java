package dao;

import connection.MyCon;
import dto.CustomerInfoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xxseokkixx
 */
public class CustomerDao {

    private static CustomerDao daoc;
    private String url; // jdbc
    private String id; // ID
    private String pw; // PassWord

    public CustomerDao() {
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        id = "semiproA";
        pw = "semi1";
    }

    public static CustomerDao getDao() {
        if (daoc == null) {
            daoc = new CustomerDao();
        }
        return daoc;
    }

    // -- select CUSTID, CUSTPW from customerinfo where custid=? and custpw=?;
    // select count(*) cnt, custname from cust
    /* 
    select count(×) cnt, custname from customerinfo where custid = '아잉' and custpw = '1234' 
    group by custname;
     */
    public Map<String, String> loginProcess(CustomerInfoDTO vo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select count(*) cnt, custid, custname from customerinfo where custid = ? "
                + "and custpw = ? group by custid, custname";
        Map<String, String> maps = new HashMap<>();
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vo.getCustId());
            pstmt.setString(2, vo.getCustPw());
            rs = pstmt.executeQuery();
            System.out.println(rs);
            if (rs.next()) {
                System.out.println(rs.getInt("cnt"));
                System.out.println(rs.getString("custname")); 
                maps.put("cnt", String.valueOf(rs.getInt("cnt")));
                maps.put("custid", rs.getString("custid"));
                maps.put("custname", rs.getString("custname"));
            } else {
                maps.put("cnt", "0");
                maps.put("custid", "Noid");
                maps.put("custname", "NoName");
                System.out.println("계정이 존재하지 않습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public boolean addCustomer(CustomerInfoDTO dto) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "insert into customerinfo values(CUSTOMERINFO_SEQ.nextval,?,?,?,?,?,?,?,sysdate)";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            // ?로 설정한 val값에 dto로부터 읽어온 값을 저장.
            pstmt.setString(1, dto.getCustId());
            pstmt.setString(2, dto.getCustPw());
            pstmt.setString(3, dto.getCustName());
            pstmt.setInt(4, dto.getCustAge());
            pstmt.setString(5, dto.getCustGender());
            pstmt.setString(6, dto.getCustAddr());
            pstmt.setString(7, dto.getCustTel());
            pstmt.executeUpdate();
            System.out.println("성공");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("실패");
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int findUserId(String id) {
        // select count(*) as cnt from mymember where id='test'
        String sql = "select count(*) as cnt from customerInfo where custid=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = MyCon.getConn(url, this.id, pw);
            pstmt = con.prepareStatement(sql);
            // 바인딩된 ?에 전달받은 인자값 입력하기.
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
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
        return 0;
    }

    public CustomerInfoDTO custDetail(String custid) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // select해온 회원 정보를 저장할 dto
        CustomerInfoDTO vo = new CustomerInfoDTO();
        String sql = "select custnum, custid, custpw, custname, custage, custgender, custaddr, custtel "
                + "from customerinfo where custid = ?";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, custid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setCustNum(rs.getInt("custnum")); // 회원번호 PK
                vo.setCustId(rs.getString("custid")); // 회원비밀번호
                vo.setCustPw(rs.getString("custpw")); // 회원비밀번호
                vo.setCustName(rs.getString("custname")); // 회원이름
                vo.setCustAge(rs.getInt("custage")); // 회원나이
                vo.setCustGender(rs.getString("custgender")); // 회원성별
                vo.setCustAddr(rs.getString("custaddr")); // 회원주소
                vo.setCustTel(rs.getString("custtel")); // 회원연락처
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return vo;
    }

    // 회원정보를 update
    public boolean updateCustomer(CustomerInfoDTO dto) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "update customerinfo set custpw=?, custname=?,custage=?,custgender=?,"
                + "custaddr=?,custtel=? where custid=?";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dto.getCustPw());
            pstmt.setString(2, dto.getCustName());
            pstmt.setInt(3, dto.getCustAge());
            pstmt.setString(4, dto.getCustGender());
            pstmt.setString(5, dto.getCustAddr());
            pstmt.setString(6, dto.getCustTel());
            // PK
            pstmt.setString(7, dto.getCustId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        CustomerInfoDTO vo = new CustomerInfoDTO();
//        vo.setCustId("아잉");
//        vo.setCustPw("1234");
//        Map<String, String> map = CustomerDao.getDao().loginProcess(vo);
//        System.out.println("Cnt :" + map.get("cnt"));
//        System.out.println("custid :" + map.get("custid"));
//        System.out.println("custname :" + map.get("custname"));
//        if (map.get("cnt").equals(0)) {
//            System.out.println("로그인");
//        }
//        CustomerInfoDTO vo1 = new CustomerInfoDTO();
//        vo1.setCustId("abc");
//        vo1.setCustPw("111");
//        CustomerDao.getDao().addCustomer(vo1);
//
//        int res = CustomerDao.getDao().findUserId("111");
//        if (res == 1) {
//            System.out.println("중복된 아이디");
//        } else {
//            System.out.println("사용가능한 아이디");
//        }
//        CustomerInfoDTO vo = new CustomerInfoDTO();
//        vo.setCustId("아잉");
//        vo.setCustPw("1234");
//        Map<String, String> map = CustomerDao.getDao().loginProcess(vo);
//        CustomerInfoDTO vo1 = CustomerDao.getDao().custDetail("아잉");
//        System.out.println("회원번호 : " + vo1.getCustNum());
//        System.out.println("아이디 : " + vo1.getCustId());
//
//    }
}
