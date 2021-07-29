
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author xxseokkixx
 */
public class MyCon {
        static {
        try {
            // jdbc ����.
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("����̹� �ε�����");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("����.");
        }
    }

    // DriverManager�� ����.
    public static Connection getConn(String url, String id, String pwd) throws SQLException {
        // DriverManager�� ����ؼ� Connectionȹ��
        // Connection�� ��ȯ�� ����? => Oracle�� ����.
        // close������ ����.
        return DriverManager.getConnection(url, id, pwd);
    }
}
