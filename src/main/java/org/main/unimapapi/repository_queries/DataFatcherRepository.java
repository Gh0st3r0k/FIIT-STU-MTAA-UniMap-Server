package org.main.unimapapi.repository_queries;

import org.main.unimapapi.dtos.Subject_dto;
import org.main.unimapapi.dtos.TeacherSubjectRoles;
import org.main.unimapapi.dtos.Teacher_dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Repository to sample data by subject and teacher
 *
 * Used in:
 * - SubjectController -> /resources/subjects
 * - TeacherController -> /resources/teachers
 */
@Repository
public class DataFatcherRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;



    /*
     * RowMapper to convert the query result to Subject_dto
     * Includes subject information and grade percentage (A-Fx)
     */
    private final RowMapper<Subject_dto> SubjectsRowMapper = new RowMapper<Subject_dto>() {
        @Override
        public Subject_dto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subject_dto subject = new Subject_dto();
            subject.setCode(rs.getString("code"));
            subject.setName(rs.getString("name"));
            subject.setType(rs.getString("type"));
            subject.setCredits(rs.getInt("credits"));
            subject.setStudyType(rs.getString("study_type"));
            subject.setSemester(rs.getString("semester"));
            subject.setLanguages(Arrays.asList(rs.getString("languages").split(",")));
            subject.setCompletionType(rs.getString("completion_type"));
            subject.setStudentCount(rs.getInt("student_count"));
            subject.setAssesmentMethods(rs.getString("assesment_methods"));
            subject.setLearningOutcomes(rs.getString("learning_outcomes"));
            subject.setCourseContents(rs.getString("course_contents"));
            subject.setPlannedActivities(rs.getString("planned_activities"));
            subject.setEvaluationMethods(rs.getString("evaluation_methods"));

            subject.setAscore(rs.getString("a"));
            subject.setBscore(rs.getString("b"));
            subject.setCscore(rs.getString("c"));
            subject.setDscore(rs.getString("d"));
            subject.setEscore(rs.getString("e"));
            subject.setFXscore(rs.getString("fx"));
            return subject;
        }
    };

    /*
     * Receive all items
     *
     * Method: used in SubjectController
     * SQL: select from `subjects` + LEFT JOIN with `subject_evaluation`
     * Grouping: by subject.code
     */
    public List<Subject_dto> fetchAllSubjects() {
        String sql = "SELECT \n" +
                "    sub.*,\n" +
                "    MAX(CASE WHEN sub_eval.grade = 'A' THEN sub_eval.percent ELSE NULL END) AS A,\n" +
                "    MAX(CASE WHEN sub_eval.grade = 'B' THEN sub_eval.percent ELSE NULL END) AS B,\n" +
                "    MAX(CASE WHEN sub_eval.grade = 'C' THEN sub_eval.percent ELSE NULL END) AS C,\n" +
                "    MAX(CASE WHEN sub_eval.grade = 'D' THEN sub_eval.percent ELSE NULL END) AS D,\n" +
                "    MAX(CASE WHEN sub_eval.grade = 'E' THEN sub_eval.percent ELSE NULL END) AS E,\n" +
                "    MAX(CASE WHEN sub_eval.grade = 'Fx' THEN sub_eval.percent ELSE NULL END) AS FX\n" +
                "FROM \n" +
                "    subjects sub\n" +
                "LEFT JOIN \n" +
                "    subject_evaluation sub_eval ON sub.code = sub_eval.subject_code\n" +
                "GROUP BY \n" +
                "    sub.code";
        return jdbcTemplate.query(sql, SubjectsRowMapper);
    }














    // RowMapper for the teacher + the subjects where he/she teaches (teacher_subject_roles)
    private final RowMapper<Teacher_dto> TeachersRowMapper = new RowMapper<Teacher_dto>() {
        @Override
        public Teacher_dto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Teacher_dto teacher = new Teacher_dto();
            teacher.setId(rs.getString("id"));
            teacher.setName(rs.getString("name"));
            teacher.setEmail(rs.getString("email"));
            teacher.setPhone(rs.getString("phone"));
            teacher.setOffice(rs.getString("office"));

            String subjectCode = rs.getString("subject_code");
            String roles = rs.getString("roles");

            if (subjectCode != null && roles != null) {
                TeacherSubjectRoles tsr = new TeacherSubjectRoles();
                tsr.setSubjectName(subjectCode);
                tsr.setRoles(Arrays.asList(roles.split(",")));
                teacher.setSubjects(Collections.singletonList(tsr));
            } else {
                teacher.setSubjects(Collections.emptyList());
            }


            return teacher;
        }
    };


    /*
     * Receiving all teachers
     *
     * Method: used in TeacherController
     * SQL: LEFT JOIN `teachers` + `teacher_subject_roles`
     * Sorting: by tea.id
     */
    private List<TeacherSubjectRoles> fetchSubjectsByTeacherId(String teacherId) {
        String sql = "SELECT * FROM teacher_subject_roles WHERE teacher_id = ?";

        if (teacherId != null || !teacherId.matches("\\d+")) {
            throw new IllegalArgumentException("teacherId cannot be null or empty");
        }

        return jdbcTemplate.query(sql, new RowMapper<TeacherSubjectRoles>() {
            @Override
            public TeacherSubjectRoles mapRow(ResultSet rs, int rowNum) throws SQLException {
                TeacherSubjectRoles teacherSubjectRoles = new TeacherSubjectRoles();
                teacherSubjectRoles.setSubjectName(rs.getString("subject_code"));
                teacherSubjectRoles.setRoles(Arrays.asList(rs.getString("roles").split(",")));
                return teacherSubjectRoles;
            }
        }, teacherId);
    }




    public List<Teacher_dto> fetchAllTeachers() {
        String sql = "SELECT tea.id, tea.name, tea.email, tea.phone, tea.office,\n" +
                "       tea_sub.subject_code, tea_sub.roles\n" +
                "FROM teachers tea\n" +
                "LEFT JOIN teacher_subject_roles tea_sub ON tea.id = tea_sub.teacher_id\n" +
                "ORDER BY tea.id;";
        return jdbcTemplate.query(sql, TeachersRowMapper);
    }





}