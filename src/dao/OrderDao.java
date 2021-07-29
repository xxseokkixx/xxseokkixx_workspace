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
            pstmt.setString(1, dto.getCustId()); // ȸ�����̵�
            pstmt.setString(2, dto.getOrderName()); // ȸ����ȣ
            pstmt.setInt(3, dto.getFoodNum()); // ȸ����ȣ
            pstmt.setString(4, dto.getOrderTel()); // ȸ����ȣ
            pstmt.setString(5, dto.getOrderAddr()); // ȸ����ȣ
            pstmt.setString(6, dto.getOrderDate()); // ȸ����ȣ
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
                // join�� �������̺��� ������ ������������ DTO����
                orderVo.setFoodInfoDTO(new FoodInfoDTO());
                // �ֹ���ȣ.
                orderVo.setOrderHis(rs.getInt("orderhis"));
                // ����ī�װ�
                orderVo.getFoodInfoDTO().setFoodCategory(rs.getString("foodcate"));
                // ���ĸ�
                orderVo.setFoodName(rs.getString("foodname"));
                // ���İ���
                orderVo.getFoodInfoDTO().setFoodPrice(rs.getInt("foodprice"));
                // �ֹ��� ��ȭ��ȣ
                orderVo.setOrderTel(rs.getString("ordertel"));
                // �ֹ��� �ּ�
                orderVo.setOrderAddr(rs.getString("orderaddr"));
                // �ֹ� ��¥
                orderVo.setOrderDate(rs.getString("orderdate"));
                // �о�� �����͸� ����
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
//        ArrayList<OrderInfoDTO> orderArr = OrderDao.getDao().printOrderList("����");
//        for (OrderInfoDTO e : orderArr) {
//            System.out.println("���ĸ� : " + e.getFoodName());
//            System.out.println("����ī�װ� : " + e.getFoodInfoDTO().getFoodCategory());
//        }
//    }
}
