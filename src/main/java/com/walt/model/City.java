package com.walt.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "CITY")
public class City extends NamedEntity{

    public City(){}

    public City(String name){
        super(name);
    }
}
