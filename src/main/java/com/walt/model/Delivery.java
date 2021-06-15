package com.walt.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
//    @JoinColumn(name = "driver_id", nullable = false)
    Driver driver;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Customer customer;

    LocalDateTime deliveryTime;
    Long distance;

    public Delivery() {
    }

    public Delivery(Driver driver, Restaurant restaurant, Customer customer, LocalDateTime deliveryTime) {
        this.driver = driver;
        this.restaurant = restaurant;
        this.customer = customer;
        this.deliveryTime = deliveryTime;
        this.setDistance();
    }

    //For Test
    public Delivery(Long distance, Driver driver, Restaurant restaurant, Customer customer, LocalDateTime deliveryTime) {
        this.driver = driver;
        this.restaurant = restaurant;
        this.customer = customer;
        this.deliveryTime = deliveryTime;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Long getDistance() {
        return distance;
    }

    //For Test - NO random
    public void setDistance(Long dis){
        this.distance = dis;
    }

    //set distance to be a random number between 0-20
    public void setDistance() {

        Random randomGenerator = new Random();
        this.distance = (long) randomGenerator.nextInt(20);
        System.out.println(this.distance);

    }
}
