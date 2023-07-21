package com.kh.jdbc.day02.student.model.dao;

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
	// 멤버변수
	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String USER = "student";
	private final String PASSWORD = "student";
	
	public List<Student> selectAll() {
		/*
		 * 1. 드라이버 등록
		 * 2. DB 연결 생성
		 * 3. 쿼리문 실행 준비
		 * 4. 쿼리문 실행 및 5. 결과 받기
		 * 6. 자원해제(close())
		 */
		String query = "SELECT * FROM Test_Table";
		List<Student> sList = null; 
		Student student = null;
		try {
			// 1. 드라이버 등록
			Class.forName(DRIVER_NAME);
			// 2. DB 연결 생성(DriverManager를 사용)
			// 한번쓰고 데이터가 사라지기 때문에 컨넥션에 넣어준다.
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			// 3. 쿼리문 실행 준비
			Statement stmt = conn.createStatement();
			// 4. 쿼리문 실행 및 5. 결과받기
			// SELECT니까 ResultSet을 쓴다.
			ResultSet rset = stmt.executeQuery(query);
			
			sList = new ArrayList<Student>();
			// 후처리
			while(rset.next()) {
				student = rsetToStudent(rset);
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

	public List<Student> selectAllByName(String studentName) {
		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_NAME ='"+studentName + "'";
		List<Student> sList = new ArrayList<Student>();
		Student student = null;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			// 후처리
			while(rset.next()) {
				student = rsetToStudent(rset);
				sList.add(student); // 가져온 데이터들을 최종 저장소인 리스트에 담아줘야 한다.
			}
			
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

	public Student selectOneById(String studentId) {
					// SELECT * FROM STUDENT_TBL WHERE STUDENT_ID = 'khuser01'
		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_ID ='"+studentId+"'";
		Student student = null;
		try {
			// 1. 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2. 디비 연결 생성
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			// 3. 쿼리 실행 준비
			Statement stmt = conn.createStatement();
			// 4. 쿼리문 실행하고  5. 결과 받기
			ResultSet rset = stmt.executeQuery(query);
			// 여러번 반복해야한다면 while(rset.next()) {} 을 사용
			// 한 번 이라면 if(rset.next()) {} 을 사용
			if(rset.next()) {
				student = rsetToStudent(rset);
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
		return student;
	}

	public int insertStudent(Student student) {
		/*
		 * 1. 드라이버 등록
		 * 2. DB 연결 생성
		 * 3. 쿼리문 실행 준비
		 * 4. 쿼리문 실행 및 5. 결과 받기
		 * 6. 자원해제
		 * 
		 */
		// INSERT INTO TEST_TABLE VALUES ('admin', 'admin', '관리자', 'M', 30, 'admin@iei.or.kr'
		// , '01012345678', '서울시 강남구 역삼동 테헤란로 7', '기타,독서,운동', '16/03/15')
		// DB에서 데이터를 입력할때 썼던 구문과 비슷하게 간다
		// 숫자에는 홑따옴표 없이 쌍따옴표만 넣어줘야한다.
		String query = "INSERT INTO TEST_TABLE VALUES("
				+ "'"+student.getStudentId()+"', '"
					+student.getStudentPwd()+"', '"
					+student.getStudentName()+"', '"
					+student.getGender()+"', "
					+student.getAge()+", '"
					+student.getEmail()+"', '"
					+student.getPhone()+"', '"
					+student.getAddress()+"', '"
					+student.getHobby()+"', "
					+"SYSDATE)";
		int result = -1;
		try { 
			// 체크드 익셉션 트라이 캐치 필요
			// 1. 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2. 디비 연결 생성
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			// 3. 쿼리 실행 준비
			Statement stmt = conn.createStatement();
			// 4. 실행하고 결과받기
//			stmt.executeQuery(query); 	// SELECT용
			result = stmt.executeUpdate(query);  // DML(INSERT, UPDATE, DELETE)
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public int updateStudent(Student student) {
		// UPDATE TEST_TABLE
		// SET MEMBER_PWD = 'pass11', EMAIL = 'hong66@kh.org' WHERE MEMBER_ID = 'user11';
		String query = "UPDATE TEST_TABLE SET "
		+ "MEMBER_PWD = '"+student.getStudentPwd()+"',"
				+ "EMAIL = '"+student.getEmail()+"',"
					+ "PHONE = '"+student.getPhone()+"',"
						+ "ADDRESS = '"+student.getAddress()+"'," 
							+ "HOBBY = '"+student.getHobby()+"'"
								+ "WHERE MEMBER_ID = '"+student.getStudentId() +"'";
		int result = -1;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
			
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int deleteStudent(String studentId) {
		String query = "DELETE FROM TEST_TABLE WHERE MEMBER_ID ='" + studentId+"'";
		int result = 0;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
			
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Student rsetToStudent(ResultSet rset) throws SQLException {
		Student student = new Student();
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
		return student;
	}
}
