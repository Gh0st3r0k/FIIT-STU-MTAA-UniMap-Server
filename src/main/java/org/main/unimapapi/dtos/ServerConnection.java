package org.main.unimapapi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerConnection {
    private String status;
    private String timestamp;
}