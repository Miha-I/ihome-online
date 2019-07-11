package ua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("unused")
@Entity
@Table(name = "home")
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "current_period")
    private LocalDate currentPeriod;

    @NotNull
    @Size(max = 50)
    @Column(name = "city")
    private String city;

    @NotNull
    @Size(max = 50)
    @Column(name = "street")
    private String street;

    @NotNull
    @Size(max = 20)
    @Column(name = "number")
    private String number;

    @Column(name = "postcode")
    private Integer postcode;

    @Column(name = "date_construction")
    private LocalDate dateConstruction;

    @Column(name = "date_last_overhaul")
    private LocalDate dateLastOverhaul;

    @Column(name = "area_stairs", precision = 8, scale = 2)
    private Float areaStairs;

    @Column(name = "area_basement", precision = 8, scale = 2)
    private Float areaBasement;

    @Size(max = 50)
    @Column(name = "roofing_type")
    private String roofingType;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lng")
    private Float lng;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "entity_id")
    private LegalEntity legalEntity;

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public LocalDate getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(LocalDate currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public LocalDate getDateConstruction() {
        return dateConstruction;
    }

    public void setDateConstruction(LocalDate dateConstruction) {
        this.dateConstruction = dateConstruction;
    }

    public LocalDate getDateLastOverhaul() {
        return dateLastOverhaul;
    }

    public void setDateLastOverhaul(LocalDate dateLastOverhaul) {
        this.dateLastOverhaul = dateLastOverhaul;
    }

    public Float getAreaStairs() {
        return areaStairs;
    }

    public void setAreaStairs(Float areaStairs) {
        this.areaStairs = areaStairs;
    }

    public Float getAreaBasement() {
        return areaBasement;
    }

    public void setAreaBasement(Float areaBasement) {
        this.areaBasement = areaBasement;
    }

    public String getRoofingType() {
        return roofingType;
    }

    public void setRoofingType(String roofingType) {
        this.roofingType = roofingType;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity){
        this.legalEntity = legalEntity;
    }

    public String getShortAddress(){
        return street + " " + number;
    }
}
