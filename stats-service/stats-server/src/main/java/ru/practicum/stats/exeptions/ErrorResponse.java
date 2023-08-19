package ru.practicum.stats.exeptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Integer status;
    private String timestamp;
    private String path;
    private String error;

}