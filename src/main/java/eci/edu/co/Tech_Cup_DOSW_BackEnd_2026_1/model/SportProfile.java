package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sport_profiles")
public class SportProfile {

    @Id
    private String id;
    private String userId;
    private Position position;
    private int jerseyNumber;
    private String photoUrl;
    private boolean available;
    private Integer semester;
    private String gender;
    private Integer age;
}
