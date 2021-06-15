package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
//@EnableScheduling
public class WaltApplication {

    private static final Logger log = LoggerFactory.getLogger(WaltApplication.class);

    private static Logger getLogger(){
//        if(logger == null){
//            try {
//                new MyLogging();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return log;
    }

    public static void main(String[] args) {

//        SpringApplication.run(WaltApplication.class, args);

        ConfigurableApplicationContext configurableApplicationContext =  SpringApplication.run(WaltApplication.class, args);
//
        CityRepository cityRepository = configurableApplicationContext.getBean(CityRepository.class);
        CustomerRepository customerRepository = configurableApplicationContext.getBean(CustomerRepository.class);
        DriverRepository driverRepository = configurableApplicationContext.getBean(DriverRepository.class);
        RestaurantRepository restaurantRepository = configurableApplicationContext.getBean(RestaurantRepository.class);
        DeliveryRepository deliveryRepository = configurableApplicationContext.getBean(DeliveryRepository.class);

        City city1 = new City("tlv");
        City city2 = new City("beer-sheva");
        City city3 = new City("yokneam");

        Customer cus1 = new Customer("yarden", city3, "abc");
        Customer cus2 = new Customer("almog", city2, "abc");
        Customer cus3 = new Customer("nadav", city1, "abcde");

        Driver mary = new Driver("Mary", city1);
//        Driver patricia = new Driver("Patricia", city1);
        Driver jennifer = new Driver("Jennifer", city2);
        Driver james = new Driver("James", city2);
        Driver john = new Driver("John", city2);

        Restaurant meat = new Restaurant("meat", city2, "All meat restaurant");
        Restaurant vegan = new Restaurant("vegan", city1, "Only vegan");
        Restaurant cafe = new Restaurant("cafe", city1, "Coffee shop");
        Restaurant chinese = new Restaurant("chinese", city1, "chinese restaurant");

        LocalDateTime rightNow = LocalDateTime.now();
        LocalDateTime d_P2Hours = LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)2).getHour(), 00);
        LocalDateTime d_P1Hours = LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)1).getHour(), 00);
//        Delivery delivery1 = new Delivery(john, meat, cus2, d1);
        Delivery prev_delivery4 = new Delivery((long) 4, mary, vegan, cus3, d_P2Hours);
        Delivery prev_delivery2 = new Delivery((long) 4, mary, vegan, cus3, d_P1Hours);


        cityRepository.saveAll(Lists.newArrayList(city1,city2,city3));
        customerRepository.saveAll(Lists.newArrayList(cus1, cus2, cus3));
//        driverRepository.saveAll(Lists.newArrayList(mary, patricia,jennifer, james,john));
        driverRepository.saveAll(Lists.newArrayList(mary,jennifer, james,john));
        restaurantRepository.saveAll(Lists.newArrayList(meat, vegan, cafe, chinese));


//        createOrderAndAssignDriver(Customer customer, Restaurant restaurant, Date deliveryTime)
        deliveryRepository.save(prev_delivery4);
        deliveryRepository.save(prev_delivery2);


    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
    }
}
