package org.main.unimapapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject_dto {
    private String code;
    private String name;
    private String type;
    private int credits;
    private String studyType;
    private String semester;
    private List<String> languages;
    private String completionType;
    private long studentCount;
    private List<Evaluation> evaluation;
    private String assesmentMethods;
    private String learningOutcomes;
    private String courseContents;
    private String plannedActivities;
    private String evaluationMethods;

    private String ascore;
    private String bscore;
    private String cscore;
    private String dscore;
    private String escore;
    private String FXscore;
}
