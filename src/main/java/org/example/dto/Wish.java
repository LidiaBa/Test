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
    private String title;              // "Новый ноутбук"
    private String link;               // Ссылка на товар
    private String imageUrl;           // Картинка
    private Integer price;             // Цена (опционально)
    private String status;             // FREE, BOOKED
    private Long userId;               // Владелец желания
}

