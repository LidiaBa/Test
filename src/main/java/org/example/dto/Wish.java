package org.example.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.StringTokenizer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Wish {
    private Long id;
    private String title;
    private String link;
    private Integer price;
    private String status;
    private Long userId;
}

