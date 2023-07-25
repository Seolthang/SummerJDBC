package com.kh.jdbc.day04.student.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kh.jdbc.day04.student.common.JDBCTemplate;
import com.kh.jdbc.day04.student.model.vo.Student;

public class StudentDAO {
	
	public List<Student> selectAll(Connection conn) {
		Statement stmt = null;
		ResultSet rset = null;
		String query = "SELECT * FROM TEST_TABLE";
		List<Student> sList = null;
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			sList = new ArrayList<Student>();
			while(rset.next()) {
				Student student = rsetToStudent(rset);
				sList.add(student);
			}
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
	
	public Student selectOneById(Connection conn, String studentId) {
			PreparedStatement pstmt = null;
	//		Statement stmt = null;
			ResultSet rset = null;
			Student student = null;
			String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_ID = ?";
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, studentId);
				rset = pstmt.executeQuery();
	//			stmt = conn.createStatement();
	//			rset = stmt.executeQuery(query);
				if(rset.next()) {
					student = rsetToStudent(rset);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					rset.close();
					pstmt.close();
	//				stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return student;
		}

	public List<Student> selectAllByName(Connection conn, String studentName) {
		String query = "SELECT * FROM TEST_TABLE WHERE MEMBER_NAME = ?"; // ? << 위치홀더를 사용한다.
		List<Student> sList = new ArrayList<Student>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			pstmt = conn.prepareStatement(query); //statement와 다르게 전달값으로 쿼리문이 필요하다.
			pstmt.setString(1, studentName);
			rset = pstmt.executeQuery(); // 전달값이 필요없다.
			while(rset.next()) {
				Student student = rsetToStudent(rset);
				sList.add(student);
			} 
				
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

	public int deleteStudent(Connection conn, String studentId) {
		String query = "DELETE * FROM TEST_TABLE WHERE MEMBER_ID = ?"; // preparedStatement를 위한 ? << 위치홀더 세팅
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(query); // << 실행값에 query를 넣어줘야한다.
			pstmt.setString(1, studentId);
			result = pstmt.executeUpdate(); // 전달값이 없다.
			
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

	public int insertStudent(Connection conn, Student student) {
		String query = "INSERT INTO TEST_TABLE VALUES(?,?,?,?,?,?,?,?,?, SYSDATE)"; // preparedStatement를 위한 ? << 위치홀더 세팅
		PreparedStatement pstmt = null;
		int result = 0;
		try {
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

	public int updateStudent(Connection conn, Student student) {
		String query = "UPDATE TEST_TABLE SET MEMBER_PWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ?, HOBBY = ? WHERE MEMBER_ID = ?";
		int result = -1;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, student.getStudentPwd());
			pstmt.setString(2, student.getEmail());
			pstmt.setString(3, student.getPhone());
			pstmt.setString(4, student.getAddress());
			pstmt.setString(5, student.getHobby());
			pstmt.setString(6, student.getStudentId());
			result = pstmt.executeUpdate();
			
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
}
