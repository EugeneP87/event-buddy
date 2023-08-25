package ru.practicum.mainService.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;

    public Location(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

}