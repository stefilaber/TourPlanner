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
@Entity(name="tours")
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tourDescription;

    @Column(nullable = false)
    private String tourFrom;

    @Column(nullable = false)
    private String tourTo;

    @Column(nullable = false)
    private String transportType;

    @Column(nullable = false)
    private int tourDistance;

    @Column(nullable = false)
    private int estimatedTime;

}
