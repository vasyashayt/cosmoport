package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;// ID корабля
    @Column(nullable = false)
    private String name;// Название корабля (до 50 знаков включительно)
    @Column(nullable = false)
    private String planet;// Планета пребывания (до 50 знаков включительно)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipType shipType;// Тип корабля
    @Column(nullable = false)
    private Date prodDate;// Дата выпуска.
    //Диапазон значений года 2800..3019 включительно
    @Column(nullable = false)
    private Boolean isUsed;// Использованный / новый
    @Column(nullable = false)
    private Double speed;// Максимальная скорость корабля. Диапазон значений
//0,01..0,99 включительно. Используй математическое
//    округление до сотых.
    @Column(nullable = false)
    private Integer crewSize;// Количество членов экипажа. Диапазон значений
    //1..9999 включительно.
    @Column(nullable = false)
    private Double rating;// Рейтинг корабля. Используй математическое
    //округление до сотых


    public Ship(Long id, String name, String planet, ShipType shipType,
                Date prodDate, Boolean isUsed, Double speed,
                Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public Ship() {

    }

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

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
