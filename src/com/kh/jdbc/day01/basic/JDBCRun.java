package com.kh.jdbc.day01.basic;
// sql 뒤에 *을 넣어줌으로써 sql에서 사용할수 있는 모든것들을 코드 한줄로 사용 가능하다.
import java.sql.*;

public class JDBCRun {

	public static void main(String[] args) {
		/*
		 * JDBC 코딩 절차
		 * 1. 드라이버 등록 (ojdbc6.jar 파일과 관련이 있음)
		 * 2. DBMS 연결 생성 (드라이버 매니저 사용)
		 * 3. Statement 객체 생성(쿼리문 실행 준비)
		 *  - new Statement();를 통해 생성해주는것이 아니라 연결을 통해 객체를 생성한다.
		 * 4. SQL 전송 (쿼리문 실행)
		 * 5. 결과받기 (ResultSet으로 바로 받는다)
		 * 6. 자원해제 (close())
		 * 
		 */
		
		
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "KH";
			String password = "KH";
			String query = "SELECT EMP_NAME, SALARY FROM EMPLOYEE";
			// 1. 드라이버 등록
			// 등록만 하고 끝이난다.
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2. DBMS 연결 생성
			Connection conn = DriverManager.getConnection(url, user, password);
			// 3. 쿼리문 실행준비(Statement 객체 생성)
			Statement stmt = conn.createStatement();
			// 4. 쿼리문 실행, 5. 결과값 받기(resultset은 테이블 형태)
			// SELECT면 ResultSet을 사용한다.
			ResultSet rset = stmt.executeQuery(query);
			// 후처리가 필요하다 - DB에서 가져온 데이터를 사용하기 위함.
			while(rset.next()) {
				System.out.printf("직원명 : %s, 급여 : %s\n", rset.getString("EMP_NAME"), rset.getInt(2));
			}
			// 6. 자원해제
			rset.close();
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
