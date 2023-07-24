package com.kh.jdbc.day03.student.model.dao;

import java.sql.*; // java.sql에 모든것을 사용하기 위해 *을 사용해서 임포트 한다.
import java.util.ArrayList;
import java.util.List;

import com.kh.jdbc.day03.student.model.vo.Student;

// 데이터 접근 객층
public class StudentDAO {
	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String USER = "student";
	private final String PASSWORD = "student";
	
	public List<Student> selectAll() {
		String query = "SELECT * FROM TEST_TABLE"; // 쿼리문을 보고 어떤것을 사용할지 결정해야함.
		List<Student> sList = new ArrayList<Student>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			while(rset.next()) {
				// rset을 쓸 때에는 꼭 next를 써줘야한다.
				Student student = rsetToStudent(rset);
				sList.add(student);
			} 
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sList;
	}

	private Student rsetToStudent(ResultSet rset) throws SQLException {
		Student student = new Student();
		// 컬럼명 오타때문에 힘들다 하면 컬럼에 순서에 따라 번호로도 사용할 수 있다.
		student.setStudentId(rset.getString(1));
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
		// 리턴은 null이 아니어야 한다.
		return student;
	}

	public Student selectOneById(String studentId) {
//		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_ID = '"+studentId+"'"; // 쿼리문을 보고 어떤것을 사용할지 결정해야함.
		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_ID = ?"; // preparedStatement를 위한 ? << 위치홀더 세팅
		Student student = null;
		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt.executeQuery(query);
			PreparedStatement pstmt = conn.prepareStatement(query); // << 실행값에 query를 넣어줘야한다.
			pstmt.setString(1, studentId);
			ResultSet rset = pstmt.executeQuery(); // 전달값이 없다.
			if(rset.next()) {
				// rset을 쓸 때에는 꼭 next를 써줘야한다.
				student = rsetToStudent(rset);
			} 
			rset.close();
			pstmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}

	public List<Student> selectAllByName(String studentName) {
//		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_NAME = '"+studentName+"'"; // 쿼리문을 보고 어떤것을 사용할지 결정해야함.
		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_NAME = ?"; // ? << 위치홀더를 사용한다.
		List<Student> sList = new ArrayList<Student>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt.executeQuery(query);
			pstmt = conn.prepareStatement(query); //statement와 다르게 전달값으로 쿼리문이 필요하다.
			pstmt.setString(1, studentName);
			rset = pstmt.executeQuery(); // 전달값이 필요없다.
			while(rset.next()) {
				// rset을 쓸 때에는 꼭 next를 써줘야한다.
				Student student = rsetToStudent(rset);
				sList.add(student);
			} 
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sList;
	}

	public Student selectLoginInfo(Student student) {
//		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_ID = '"+student.getStudentId()+"' AND MEMBER_PWD = '"+student.getStudentPwd()+"'"; // 쿼리문을 보고 어떤것을 사용할지 결정해야함.
		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_ID = ? AND MEMBER_PWD = ?"; // 물음표는 = 위치홀더라고 부른다.
		Student result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
//			ResultSet rset = stmt.executeQuery(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, student.getStudentId());
			pstmt.setString(2, student.getStudentPwd()); // 시작은 1로 하고 마지막 숫자는 ?의 갯수와 같아야 한다.
			rset = pstmt.executeQuery();
			if(rset.next()) {
				// rset을 쓸 때에는 꼭 next를 써줘야한다.
				result = rsetToStudent(rset);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public int insertStudent(Student student) {
//		String query = "INSERT INTO TEST_TABLE VALUES('"+student.getStudentId()+"','"+student.getStudentPwd()+"','"+student.getStudentName()+"','"+student.getGender()+"',"+student.getAge()+",'"+student.getEmail()+"','"+student.getPhone()+"','"+student.getAddress()+"','"+student.getHobby()+"',SYSDATE)"; // 쿼리문을 보고 어떤것을 사용할지 결정해야함.
		String query = "INSERT INTO TEST_TABLE VALUES(?,?,?,?,?,?,?,?,?,SYSDATE)"; 
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
//			result = stmt.executeUpdate(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, student.getStudentId());
			pstmt.setString(2, student.getStudentPwd());
			pstmt.setString(3, student.getStudentName());
			pstmt.setString(4, String.valueOf(student.getGender()));
			pstmt.setInt(5, student.getAge());
			pstmt.setString(6, student.getEmail());
			pstmt.setString(7, student.getPhone());
			pstmt.setString(8, student.getAddress());
			pstmt.setString(9, student.getHobby());
			// ***** 쿼리문 실행 빼먹지 않기 *****
			result = pstmt.executeUpdate(); 
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int updateStudent(Student student) {
//		String query = "UPDATE TEST_TABLE SET MEMBER_PWD = '"+student.getStudentPwd()+"', EMAIL = '"+student.getEmail()+"', PHONE = '"+student.getPhone()+"', ADDRESS = '"+student.getAddress()+"', HOBBY = '"+student.getHobby()+"' WHERE MEMBER_ID = '"+student.getStudentId()+"'";
		String query = "UPDATE TEST_TABLE SET MEMBER_PWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ?, HOBBY = ? WHERE MEMBER_ID = ?";
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
//			result = stmt.executeUpdate(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, student.getStudentPwd());
			pstmt.setString(2, student.getEmail());
			pstmt.setString(3, student.getPhone());
			pstmt.setString(4, student.getAddress());
			pstmt.setString(5, student.getHobby());
			pstmt.setString(6, student.getStudentId());
			result = pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public int deleteStudent(String studentId) {
//		String query = "DELETE FROM TEST_TABLE WHERE MEMBER_ID = '"+studentId+"'";
		String query = "DELETE FROM TEST_TABLE WHERE MEMBER_ID = ?";
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//			Statement stmt = conn.createStatement();
			pstmt = conn.prepareStatement(query); //statement와 다르게 전달값으로 쿼리문이 필요하다.
			pstmt.setString(1, studentId);
			result = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
