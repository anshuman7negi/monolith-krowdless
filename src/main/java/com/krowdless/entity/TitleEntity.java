package com.krowdless.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "title")
public class TitleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private Integer minTrips;
    private Integer minSpent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinTrips() {
        return minTrips;
    }

    public void setMinTrips(Integer minTrips) {
        this.minTrips = minTrips;
    }

    public Integer getMinSpent() {
        return minSpent;
    }

    public void setMinSpent(Integer minSpent) {
        this.minSpent = minSpent;
    }

    // getters & setters

}
