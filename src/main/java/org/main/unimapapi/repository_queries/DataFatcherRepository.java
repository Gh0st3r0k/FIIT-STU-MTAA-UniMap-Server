package org.main.unimapapi.repository_queries;

import org.main.unimapapi.dtos.Subject_dto;
import org.main.unimapapi.dtos.TeacherSubjectRoles;
import org.main.unimapapi.dtos.Teacher_dto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

/*
 * Repository to sample data by subject and teacher
 *
 * Used in:
 * - SubjectController -> /resources/subjects
 * - TeacherController -> /resources/teachers
 */
@Repository
@RequiredArgsConstructor
public class DataFatcherRepository {
    private final JdbcTemplate jdbcTemplate;

    /*
     * RowMapper to convert the query result to Subject_dto
     * Includes subject information and grade percentage (A-Fx)
     */
    private final RowMapper<Subject_dto> subjectsRowMapper = (rs, rowNum) -> {
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
    };

    /*
     * Receive all items
     *
     * Method: used in SubjectController
     * SQL: select from `subjects` + LEFT JOIN with `subject_evaluation`
     * Grouping: by subject.code
     */
    public List<Subject_dto> fetchAllSubjects() {
        String sql = """
                SELECT\s
                    sub.*,
                    MAX(CASE WHEN sub_eval.grade = 'A' THEN sub_eval.percent  END) AS A,
                    MAX(CASE WHEN sub_eval.grade = 'B' THEN sub_eval.percent END) AS B,
                    MAX(CASE WHEN sub_eval.grade = 'C' THEN sub_eval.percent END) AS C,
                    MAX(CASE WHEN sub_eval.grade = 'D' THEN sub_eval.percent  END) AS D,
                    MAX(CASE WHEN sub_eval.grade = 'E' THEN sub_eval.percent  END) AS E,
                    MAX(CASE WHEN sub_eval.grade = 'Fx' THEN sub_eval.percent  END) AS FX
                FROM\s
                    subjects sub
                LEFT JOIN\s
                    subject_evaluation sub_eval ON sub.code = sub_eval.subject_code
                GROUP BY\s
                    sub.code""";
        return jdbcTemplate.query(sql, subjectsRowMapper);
    }

    // RowMapper for the teacher + the subjects where he/she teaches (teacher_subject_roles)
    private final RowMapper<Teacher_dto> teachersRowMapper = (rs, rowNum) -> {
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

        if (teacherId == null || !teacherId.matches("\\d+")) {
            throw new IllegalArgumentException("teacherId cannot be null or empty");
        }

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TeacherSubjectRoles teacherSubjectRoles = new TeacherSubjectRoles();
            teacherSubjectRoles.setSubjectName(rs.getString("subject_code"));
            teacherSubjectRoles.setRoles(Arrays.asList(rs.getString("roles").split(",")));
            return teacherSubjectRoles;
        }, teacherId);
    }

    public List<Teacher_dto> fetchAllTeachers() {
        String sql = """
                SELECT tea.id, tea.name, tea.email, tea.phone, tea.office,
                       tea_sub.subject_code, tea_sub.roles
                FROM teachers tea
                LEFT JOIN teacher_subject_roles tea_sub ON tea.id = tea_sub.teacher_id
                ORDER BY tea.id;""";
        return jdbcTemplate.query(sql, teachersRowMapper);
    }
}