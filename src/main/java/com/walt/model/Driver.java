package com.walt.model;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "DRIVER")
public class Driver extends NamedEntity implements DriverDistance {

    @ManyToOne
    City city;

    @Column(name = "totalDistance")
    Long totalDistance;

//    @OneToMany(targetEntity = Delivery.class, cascade = CascadeType.ALL)
//    @JoinColumn(name= "deliveryd_fk", referencedColumnName = "id")
//    private List<Delivery> deliverys;

//    Boolean isFree;

    public Driver(){}


    public Driver(String name, City city){
        super(name);
        this.city = city;
//        this.isFree = true;
//        System.out.println("total distance after all:" + this.getDriver().getTotalDistance());
        this.totalDistance = Long.valueOf(0);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public Driver getDriver() {
        return this;
    }

    @Override
    public Long getTotalDistance(){
        return totalDistance;
    }

//    public void setIsFree(Boolean isFree) {
//        this.isFree = isFree;
//    }
//    public Boolean getIsFree() {
//        return this.isFree;
//    }

//    public double getTotalDistance() {
//        return totalDistance;
//    }

    @Modifying
    public void setTotalDistance(Long distance) {
        if (this.totalDistance == null) {
            this.totalDistance = distance;
        } else {
            this.totalDistance = distance;
//            this.totalDistance = this.totalDistance + distance;
        }
    }

}
