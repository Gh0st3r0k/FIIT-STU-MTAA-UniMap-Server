package org.main.unimapapi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectTeacherEntity {
    private Teacher teacher;
    private List<String> roles;
    private List<String> languages;
}