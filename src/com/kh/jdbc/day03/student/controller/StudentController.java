package com.kh.jdbc.day03.student.controller;

import java.util.List;

import com.kh.jdbc.day03.student.model.dao.StudentDAO;
import com.kh.jdbc.day03.student.model.vo.Student;

// DAO를 호출한다.
public class StudentController {
	private StudentDAO sDao;
	public StudentController() {
		sDao = new StudentDAO();
	}
	// SELECT * FROM TEST_TABLE
	public List<Student> selectAllStudent() {
		List<Student> sList = sDao.selectAll(); // Dao에 메소드를 생성해서 호출
		return sList; // 오류메시지를 보기 싫으면 일단 null값을 넣어서 리턴을 써준다.
	}
	public Student selectOneById(String studentId) {
		Student student = sDao.selectOneById(studentId);
		return student;
	}
	public List<Student> selectAllByName(String studentName) {
		List<Student> sList = sDao.selectAllByName(studentName);
		return sList;
	}
	public Student studentLogin(Student student) {
		Student result = sDao.selectLoginInfo(student);
		return result;
	}
	public int insertStudent(Student student) {
		int result = sDao.insertStudent(student);
		return result;
	}
	public int deleteStudent(String studentId) {
		int result = sDao.deleteStudent(studentId);
		return result;
	}
	public int updateStudent(Student student) {
		int result = sDao.updateStudent(student);
		return result;
	}
}
