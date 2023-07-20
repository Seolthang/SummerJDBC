package com.kh.jdbc.day01.student.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kh.jdbc.day01.student.model.vo.Student;

public class StudentDAO {
		// DAO에 역할은 DB에 데이터를 가지고 와서 객체화 시켜놓는것이 목적이다.
	public List<Student> selectAll() {
		/*
		 * 1. 드라이버 등록
		 * 2. DB 연결 생성
		 * 3. 쿼리문 실행 준비
		 * 4. 쿼리문 실행 및 5. 결과 받기
		 * 6. 자원해제(close())
		 */
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "student";
		String password = "student";
		String query = "SELECT * FROM Test_Table";
		List<Student> sList = null; 
		Student student = null;
		try {
			// 1. 드라이버 등록
			Class.forName(driverName);
			// 2. DB 연결 생성(DriverManager를 사용)
			// 한번쓰고 데이터가 사라지기 때문에 컨넥션에 넣어준다.
			Connection conn = DriverManager.getConnection(url, user, password);
			// 3. 쿼리문 실행 준비
			Statement stmt = conn.createStatement();
			// 4. 쿼리문 실행 및 5. 결과받기
			// SELECT니까 ResultSet을 쓴다.
			ResultSet rset = stmt.executeQuery(query);
			
			sList = new ArrayList<Student>();
			// 후처리
			while(rset.next()) {
				student = new Student();
				student.setStudentId(rset.getString("MEMBER_ID"));
				student.setStudentPwd(rset.getString("MEMBER_PWD"));
				student.setStudentName(rset.getString("MEMBER_NAME"));
				// 문자는 문자열에 문자로 잘라서 사용, charAt() 메소드를 사용한다.
				student.setGender(rset.getString("GENDER").charAt(0));
				student.setAge(rset.getInt("AGE"));
				student.setEmail(rset.getString("EMAIL"));
				student.setPhone(rset.getString("PHONE"));
				student.setAddress(rset.getString("ADDRESS"));
				student.setHobby(rset.getString("HOBBY"));
				student.setEnrollDate(rset.getDate("ENROLL_DATE"));
				sList.add(student); // 가져온 데이터들을 최종 저장소인 리스트에 담아줘야 한다.
			}
			// 6. 자원해제
			rset.close();
			stmt.close();
			conn.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sList;
	}
}
