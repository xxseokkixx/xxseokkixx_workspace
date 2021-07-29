package dao;

import connection.MyCon;
import dto.FoodInfoDTO;
import dto.SalesChkDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author xxseokkixx
 */
public class SalesFoodDao {

    private static SalesFoodDao daoS;
    private String url; // jdbc
    private String id; // ID
    private String pw; // PassWord

    public SalesFoodDao() {
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        id = "semiproA";
        pw = "semi1";
    }

    public static SalesFoodDao getDao() {
        if (daoS == null) {
            daoS = new SalesFoodDao();
        }
        return daoS;
    }

    // 음식판매내역을 출력하는 메소드
    public ArrayList<SalesChkDTO> showSalesFood() {
        ArrayList<SalesChkDTO> salesArr = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select a.salesnum, a.salesFoodnum, f.foodcate, f.foodname, f.foodprice, a.salesdate "
                + "from foodinfo f, adminsalesChk a where a.salesFoodnum = f.foodnum order by a.salesnum asc";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SalesChkDTO vo = new SalesChkDTO();
                vo.setFoodDto(new FoodInfoDTO()); // foodInfoDto 선언
                vo.setSalesNum(rs.getInt("salesnum")); // 판매번호
                vo.setSalesFoodNum(rs.getInt("salesFoodnum")); // 음식번호
                vo.getFoodDto().setFoodCategory(rs.getString("foodcate")); // 음식분류
                vo.getFoodDto().setFoodName(rs.getString("foodname")); // 음식명
                vo.getFoodDto().setFoodPrice(rs.getInt("foodprice")); // 음식가격
                vo.setSalesDate(rs.getString("salesdate")); // 판매날짜.
                salesArr.add(vo);
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
        return salesArr;
    }
    
        // 판매내역 합계구하기.
        public int sumFoodPrice() {
        int total = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select price_f(salesFoodnum) from adminsaleschk";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("price_f(salesFoodnum)");
            } else {
                total = 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
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
        return total;
    }
        public static void main(String[] args) {
        int f = SalesFoodDao.getDao().sumFoodPrice();
            System.out.println(f);
    }
}
