package at.fhtw.swen2.tutorial.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="logs")
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String dateTime;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private int totalTime;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private Long tourId;

    @ManyToOne
    @JoinColumn(name = "tourId", insertable = false, updatable = false)
    private TourEntity tour;


}
