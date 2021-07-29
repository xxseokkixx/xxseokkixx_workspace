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

    // �����Ǹų����� ����ϴ� �޼ҵ�
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
                vo.setFoodDto(new FoodInfoDTO()); // foodInfoDto ����
                vo.setSalesNum(rs.getInt("salesnum")); // �ǸŹ�ȣ
                vo.setSalesFoodNum(rs.getInt("salesFoodnum")); // ���Ĺ�ȣ
                vo.getFoodDto().setFoodCategory(rs.getString("foodcate")); // ���ĺз�
                vo.getFoodDto().setFoodName(rs.getString("foodname")); // ���ĸ�
                vo.getFoodDto().setFoodPrice(rs.getInt("foodprice")); // ���İ���
                vo.setSalesDate(rs.getString("salesdate")); // �Ǹų�¥.
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
    
        // �Ǹų��� �հ豸�ϱ�.
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
