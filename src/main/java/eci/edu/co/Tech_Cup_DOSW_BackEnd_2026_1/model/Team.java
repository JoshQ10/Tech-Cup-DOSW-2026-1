package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teams")
public class Team {

    @Id
    private String id;
    private String name;
    private String shieldUrl;
    private String uniformColors;
    private String tournamentId;
    private String captainId;
    private List<String> players;
}
