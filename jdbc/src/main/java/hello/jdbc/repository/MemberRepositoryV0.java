package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null; // PreparedStatement: 파라미터를 바인딩 할 수 있는 인터페이스

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            log.error("db error: ", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    /**
     * 사용한 자원 닫기. SQLException 이 발생하면 이후의 자원 닫기를 처리할 수 없어지는 상황을 방지하기 위해 모두 try/catch 처리 해준다.
     * @param con
     * @param stmt
     * @param rs
     */
    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error ", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error ", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error ", e);
            }
        }
    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
