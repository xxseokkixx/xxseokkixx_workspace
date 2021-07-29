
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
            // jdbc 설정.
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("드라이버 로딩성공");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("실패.");
        }
    }

    // DriverManager를 설정.
    public static Connection getConn(String url, String id, String pwd) throws SQLException {
        // DriverManager를 사용해서 Connection획득
        // Connection을 반환한 순간? => Oracle에 접속.
        // close전까지 동작.
        return DriverManager.getConnection(url, id, pwd);
    }
}
