package dao;

import connection.MyCon;
import dto.FoodInfoDTO;
import dto.OrderInfoDTO;
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
public class OrderDao {

    private static OrderDao daoo;
    private String url; // jdbc
    private String id; // ID
    private String pw; // PassWord

    public OrderDao() {
        url = "jdbc:oracle:thin:@localhost:1521:xe";
        id = "semiproA";
        pw = "semi1";
    }

    public static OrderDao getDao() {
        if (daoo == null) {
            daoo = new OrderDao();
        }
        return daoo;
    }

    public boolean addOrderList(OrderInfoDTO dto) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "Insert Into orderinfo Values(orderInfo_seq.nextVal,?,?, ?,?,?,?, sysdate)";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dto.getCustId()); // 회원아이디
            pstmt.setString(2, dto.getOrderName()); // 회원번호
            pstmt.setInt(3, dto.getFoodNum()); // 회원번호
            pstmt.setString(4, dto.getOrderTel()); // 회원번호
            pstmt.setString(5, dto.getOrderAddr()); // 회원번호
            pstmt.setString(6, dto.getOrderDate()); // 회원번호
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

    public ArrayList<OrderInfoDTO> printOrderList(String custId) {
        ArrayList<OrderInfoDTO> orderArr = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select o.orderhis, f.foodcate, o.foodname, f.foodprice, o.ordertel,"
                + " o.orderaddr, o.orderdate from orderinfo o, foodinfo f "
                + "where o.foodnum = f.foodnum and o.custid = ? order by orderdate desc";
        try {
            con = MyCon.getConn(url, id, pw);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, custId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OrderInfoDTO orderVo = new OrderInfoDTO();
                // join한 음식테이블의 정보를 가져오기위한 DTO선언
                orderVo.setFoodInfoDTO(new FoodInfoDTO());
                // 주문번호.
                orderVo.setOrderHis(rs.getInt("orderhis"));
                // 음식카테고리
                orderVo.getFoodInfoDTO().setFoodCategory(rs.getString("foodcate"));
                // 음식명
                orderVo.setFoodName(rs.getString("foodname"));
                // 음식가격
                orderVo.getFoodInfoDTO().setFoodPrice(rs.getInt("foodprice"));
                // 주문자 전화번호
                orderVo.setOrderTel(rs.getString("ordertel"));
                // 주문자 주소
                orderVo.setOrderAddr(rs.getString("orderaddr"));
                // 주문 날짜
                orderVo.setOrderDate(rs.getString("orderdate"));
                // 읽어온 데이터를 저장
                orderArr.add(orderVo);
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
        return orderArr;
    }

//    public static void main(String[] args) {
//        ArrayList<OrderInfoDTO> orderArr = OrderDao.getDao().printOrderList("아잉");
//        for (OrderInfoDTO e : orderArr) {
//            System.out.println("음식명 : " + e.getFoodName());
//            System.out.println("음식카테고리 : " + e.getFoodInfoDTO().getFoodCategory());
//        }
//    }
}
