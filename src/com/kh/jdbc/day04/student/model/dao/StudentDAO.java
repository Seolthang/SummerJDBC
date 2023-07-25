package com.kh.jdbc.day04.student.model.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kh.jdbc.day04.student.common.JDBCTemplate;
import com.kh.jdbc.day04.student.model.vo.Student;

/*
 * 1. Statement
 * - createStatement() 메소드를 통해서 객체 생성
 * - execute*()를 실행할 때 쿼리문이 필요함
 * - 쿼리문을 별도로 컴파일 하지 않아서 단순 실행일 경우 빠름
 * - ex) 전체정보조회
 * 
 * 2. PreparedStatement
 * - Statement를 상속받아서 만들어진 인터페이스
 * - prepareStatement() 메소들를 통해서 객체 생성하는데 이때 쿼리문 필요
 * - 쿼리문을 미리 컴파일하여 캐싱한 후 재사용하는 구조
 * - 쿼리문을 컴파일 할때 위치홀더(?)를 이용하여 값이 들어가는 부분을 표시한 후 쿼리문 실행전에
 * 값을 셋팅해주어야함.
 * - 컴파일 하는 과정이 있어 느릴 수 있지만 쿼리문을 반복해서 실행할 때는 속도가 빠름
 * - 전달값이 있는 쿼리문에 대해서 SqlInjection을 방어할 수 있는 보안기능이 추가됨
 * - ex) 아이디로 정보조회, 이름으로 정보조회
 * 
 */

public class StudentDAO {
	/*
	 * 1. Checked Exception과 Unchecked Exception
	 * 2. 예외의 종류 Throwable - Exception(checked exception 한정)
	 * 3. 예외처리 처리 방법 : throws, try ~ catch
	 * 
	 */
	private Properties prop;
	
	public StudentDAO() {
		prop = new Properties();
		Reader reader;
		try {
			reader = new FileReader("resources/query.properties");
			prop.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		// Unreachable catch block for FileNotFoundException. It is already handled by the catch block for Exception
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}

	public List<Student> selectAll(Connection conn) {
		Statement stmt = null;
		ResultSet rset = null;
		String query = prop.getProperty("selectAll");
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
//				conn.close(); 를 서비스에서 하기 때문에 객체마다 따로 넣지 않아도 된다.
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sList;
	}
	
	public List<Student> selectAllByName(Connection conn, String studentName) {
		String query = prop.getProperty("selectAllByName"); // ? << 위치홀더를 사용한다.
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
			String query = prop.getProperty("selectOneById");
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
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return student;
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

	public int insertStudent(Connection conn, Student student) {
		String query = prop.getProperty("insertStudent"); // preparedStatement를 위한 ? << 위치홀더 세팅
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int updateStudent(Connection conn, Student student) {
		String query = prop.getProperty("updateStudent");
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
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public int deleteStudent(Connection conn, String studentId) {
		String query = prop.getProperty("deleteStudent"); // preparedStatement를 위한 ? << 위치홀더 세팅
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
