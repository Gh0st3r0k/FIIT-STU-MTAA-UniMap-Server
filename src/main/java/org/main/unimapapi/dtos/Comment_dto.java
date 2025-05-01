package org.main.unimapapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment_dto {
    private int user_id;
    // looking can be like subject_id or teacher_id
    private String name;
    private String looking_id;
    private String description;
    private String rating;
    private int levelAccess;
    private int comment_id;
}