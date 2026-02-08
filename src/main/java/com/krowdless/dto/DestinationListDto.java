package com.krowdless.dto;

public class DestinationListDto {

    private Long id;
    private String name;
    private String stateName;

    private String shortDescription;
    private String coverImageUrl;

    private Double rating;        
    private Integer price;
    private String crowdLevel;
    
    
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
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public String getCoverImageUrl() {
        return coverImageUrl;
    }
    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getCrowdLevel() {
        return crowdLevel;
    }
    public void setCrowdLevel(String crowdLevel) {
        this.crowdLevel = crowdLevel;
    } 

    
    
}
