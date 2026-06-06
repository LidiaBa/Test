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
public class Booking {
    private Long id;
    private Long wishId;
    private Long userId;
    private Long ownerId;
}
