package dao;

import connection.MyCon;
import dto.FoodInfoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xxseokkixx
 */
public class FoodDao {

    private static FoodDao daof;
    private String url; // jdbc
    private String id; // ID
    private String pw; // PassWord

    public FoodDao() {
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        id = "semiproA";
        pw = "semi1";
    }

    public static FoodDao getDao() {
        if (daof == null) {
            daof = new FoodDao();
        }
        return daof;
    }

    // 카테고리에 해당하는 음식들을 출력하는 method.
    public ArrayList<FoodInfoDTO> listCategory(String category) {
        ArrayList<FoodInfoDTO> foodArr = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select foodnum, foodcate, foodname, fooddetail, foodprice "
                + "from foodinfo where foodcate=?";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FoodInfoDTO vo = new FoodInfoDTO();
                vo.setFoodNum(rs.getInt("foodnum")); // 음식번호
                vo.setFoodCategory(rs.getString("foodcate")); // 음식카테고리
                vo.setFoodName(rs.getString("foodname")); // 음식이름
                vo.setFoodDetail(rs.getString("fooddetail")); // 음식상세정보
                vo.setFoodPrice(rs.getInt("foodprice")); // 음식가격
                foodArr.add(vo); // SQL에서 select해온 값을 반환값에 저장.
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
        return foodArr;
    }

    public FoodInfoDTO selectFood(int num) {
        FoodInfoDTO vo = new FoodInfoDTO();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select foodnum , foodcate,foodname,fooddetail,foodprice from foodinfo where foodnum=?";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setFoodNum(rs.getInt("foodnum"));
                vo.setFoodCategory(rs.getString("foodcate"));
                vo.setFoodName(rs.getString("foodname"));
                vo.setFoodDetail(rs.getString("fooddetail"));
                vo.setFoodPrice(rs.getInt("foodprice"));
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
            return vo;
        }
    }
}
