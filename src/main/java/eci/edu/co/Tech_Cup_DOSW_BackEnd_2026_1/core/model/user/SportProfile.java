package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Program;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.DominantFoot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportProfile {

    private Long id;
    private Long userId;
    private Position position;
    private Position secondaryPosition;
    private DominantFoot dominantFoot;
    private Program program;
    private int jerseyNumber;
    private String photoUrl;
    private String fullPhotoUrl;
    private boolean available;
    private Integer semester;
    private String gender;
    private Integer age;
    private LocalDateTime lastAvailabilityChange;
    private String availabilityChangeReason;

    public boolean isAvailable() {
        return available;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public static class SportProfileBuilder {
        public SportProfileBuilder available(boolean available) {
            this.available = available;
            return this;
        }
    }
}
