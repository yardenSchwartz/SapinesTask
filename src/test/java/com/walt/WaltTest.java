package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest()
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WaltTest {

    @TestConfiguration
    static class WaltServiceImplTestContextConfiguration {

        @Bean
        public WaltService waltService() {
            return new WaltServiceImpl();
        }
    }

    @Autowired
    WaltService waltService;

    @Resource
    CityRepository cityRepository;

    @Resource
    CustomerRepository customerRepository;

    @Resource
    DriverRepository driverRepository;

    @Resource
    DeliveryRepository deliveryRepository;

    @Resource
    RestaurantRepository restaurantRepository;

    LocalDateTime rightNow = LocalDateTime.now();
    LocalDateTime d_Month= LocalDateTime.of(rightNow.getYear(), rightNow.minusMonths((long)1).getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.getHour(), 00);//history
    LocalDateTime d_2Hours= LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), 15, rightNow.minusHours((long)2).getHour(), 00);//history
    LocalDateTime d_4Hours= LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), 15, rightNow.minusHours((long)4).getHour(), 00);//history
    LocalDateTime d_P1Hours = LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)1).getHour(), 00);
    LocalDateTime d_P2Hours = LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)2).getHour(), 00);
    LocalDateTime d_P3Hours= LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)3).getHour(), 00);
    LocalDateTime d_P4Hours= LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)4).getHour(), 00);
    LocalDateTime d_P5Hours= LocalDateTime.of(rightNow.getYear(), rightNow.getMonth().getValue(), rightNow.getDayOfMonth(), rightNow.plusHours((long)5).getHour(), 00);

    @BeforeEach()
    public void prepareData(){

        City jerusalem = new City("Jerusalem");
        City tlv = new City("Tel-Aviv");
        City bash = new City("Beer-Sheva");
        City haifa = new City("Haifa");

        cityRepository.save(jerusalem);
        cityRepository.save(tlv);
        cityRepository.save(bash);
        cityRepository.save(haifa);

        createDrivers(jerusalem, tlv, bash, haifa);

        createCustomers(jerusalem, tlv, haifa, bash);

        createRestaurant(jerusalem, tlv, bash);

    }

    private void createRestaurant(City jerusalem, City tlv, City bash) {
        Restaurant meat = new Restaurant("meat", jerusalem, "All meat restaurant");
        Restaurant vegan = new Restaurant("vegan", bash, "Only vegan");
        Restaurant cafe = new Restaurant("cafe", tlv, "Coffee shop");
        Restaurant chinese = new Restaurant("chinese", tlv, "chinese restaurant");
        Restaurant mexican = new Restaurant("restaurant", tlv, "mexican restaurant ");

        restaurantRepository.saveAll(Lists.newArrayList(meat, vegan, cafe, chinese, mexican));
//        Restaurant r = restaurantRepository.findByName("vegan");
//        r.setName("vegeterian");
//        Restaurant rnew = restaurantRepository.save(r);
//        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
//        int x=5;
//        int y= x;
    }

    private void createCustomers(City jerusalem, City tlv, City haifa, City bash) {
        Customer beethoven = new Customer("Beethoven", tlv, "Ludwig van Beethoven");
        Customer mozart = new Customer("Mozart", jerusalem, "Wolfgang Amadeus Mozart");
        Customer chopin = new Customer("Chopin", haifa, "Frédéric François Chopin");
        Customer rachmaninoff = new Customer("Rachmaninoff", tlv, "Sergei Rachmaninoff");
        Customer bach = new Customer("Bach", bash, "Sebastian Bach. Johann");

        customerRepository.saveAll(Lists.newArrayList(beethoven, mozart, chopin, rachmaninoff, bach));
    }

    private void createDrivers(City jerusalem, City tlv, City bash, City haifa) {
        Driver mary = new Driver("Mary", tlv);
        Driver patricia = new Driver("Patricia", tlv);
        Driver jennifer = new Driver("Jennifer", haifa);
        Driver james = new Driver("James", bash);
        Driver john = new Driver("John", bash);
        Driver robert = new Driver("Robert", jerusalem);
        Driver david = new Driver("David", jerusalem);
        Driver daniel = new Driver("Daniel", tlv);
        Driver noa = new Driver("Noa", haifa);
        Driver ofri = new Driver("Ofri", haifa);
        Driver nata = new Driver("Neta", jerusalem);

        driverRepository.saveAll(Lists.newArrayList(mary, patricia, jennifer, james, john, nata, david,robert, daniel, noa, ofri));
    }

    @Test
    public void testBasics() {
        List<Restaurant> list = (List<Restaurant>) restaurantRepository.findAll();
        assertEquals(((List<City>) cityRepository.findAll()).size(),4);
        assertEquals((driverRepository.findAllDriversByCity(cityRepository.findByName("Beer-Sheva")).size()), 2);
        Restaurant restaurant = restaurantRepository.findByName("meat");
        assertEquals(restaurant.getName(), "meat");
        List<Driver> driverList = (List<Driver>) driverRepository.findAll();
        assertEquals(driverList.size(), 11);
        Driver d = driverRepository.findByName("James");
        assertEquals(d.getName(), "James");
    }

    @Test
    public void testBashAndJerusalem(){

        //Driver-james
        //history
        Delivery prev_delivery1 = new Delivery((long) 5, driverRepository.findByName("James"), restaurantRepository.findByName("vegan"), customerRepository.findByName("Bach"), d_Month);
        Delivery prev_delivery2 = new Delivery((long) 1, driverRepository.findByName("James"), restaurantRepository.findByName("vegan"), customerRepository.findByName("Bach"), d_4Hours);
        //future
        Delivery prev_delivery8 = new Delivery((long) 1, driverRepository.findByName("James"), restaurantRepository.findByName("vegan"), customerRepository.findByName("Bach"), d_P2Hours);

        //Driver-john
        //history
        Delivery prev_delivery3 = new Delivery((long) 5, driverRepository.findByName("John"), restaurantRepository.findByName("vegan"), customerRepository.findByName("Bach"), d_4Hours);

        //Driver-robert
        //history
        Delivery prev_delivery4 = new Delivery((long) 4, driverRepository.findByName("Robert"), restaurantRepository.findByName("meat"), customerRepository.findByName("Mozart"), d_2Hours);
        //future
        Delivery prev_delivery7 = new Delivery((long) 3, driverRepository.findByName("Robert"), restaurantRepository.findByName("meat"), customerRepository.findByName("Mozart"), d_P5Hours);

        //Driver-david
        //history
        Delivery prev_delivery5 = new Delivery((long) 5,driverRepository.findByName("David"), restaurantRepository.findByName("meat"), customerRepository.findByName("Mozart"), d_2Hours);
        Delivery prev_delivery6 = new Delivery((long) 2,driverRepository.findByName("David"), restaurantRepository.findByName("meat"), customerRepository.findByName("Mozart"), d_Month);


        deliveryRepository.saveAll(Lists.newArrayList(prev_delivery1,prev_delivery2,prev_delivery3,prev_delivery3,prev_delivery4,prev_delivery5,prev_delivery6,prev_delivery7,prev_delivery8));

        //delivery that customer id and restaurant id do not match but delivery date is ok
        Delivery delivery1 = createDelivery("Mozart", "vegan",  d_P2Hours, (long) 5);
        assertNull(delivery1);

        Delivery delivery2 = createDelivery("Mozart", "meat",  d_P2Hours, (long) 5);
        assertEquals(delivery2.getDriver().getName(), "Neta");

        Delivery delivery3 = createDelivery("Bach", "vegan",  d_P2Hours, (long) 2);
        assertEquals(delivery3.getDriver().getName(), "John");

        Delivery delivery4 = createDelivery("Bach", "vegan",  d_P3Hours, (long) 3);
        assertEquals(delivery4.getDriver().getName(), "John");

        List<DriverDistance> driverDistanceList = waltService.getDriverRankReport();
        assertEquals(driverDistanceList.get(0).getDriver().getName(), "David");

        List<DriverDistance> driverDistanceListByJerusalemCity = waltService.getDriverRankReportByCity(cityRepository.findByName("Jerusalem"));
        assertEquals(driverDistanceListByJerusalemCity.get(0).getDriver().getName(), "David");

        List<DriverDistance> driverDistanceListByBashCity = waltService.getDriverRankReportByCity(cityRepository.findByName("Beer-Sheva"));
        assertEquals(driverDistanceListByBashCity.get(0).getDriver().getName(), "James");


    }
    private Delivery createDelivery(String customerName, String resturantName, LocalDateTime date, Long distance) {
        Customer customer = customerRepository.findByName(customerName);
        Restaurant restaurant = restaurantRepository.findByName(resturantName);
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        if(delivery!=null) {
            delivery.setDistance(distance);
            deliveryRepository.save(delivery);
        }
        return delivery;
    }

//    @Test
////    @Transactional
//    public void test() {
//
////        LocalDateTime d1 = LocalDateTime.of(2021, Month.MAY, 14, 17, 00);
////        LocalDateTime d2= LocalDateTime.of(2021, Month.JUNE, 15, 11, 00);
////        LocalDateTime d3= LocalDateTime.of(2021, Month.JUNE, 15, 12, 00);
////        LocalDateTime d4= LocalDateTime.of(2021, Month.JUNE, 15, 13, 00);
////        LocalDateTime d5= LocalDateTime.of(2021, Month.JUNE, 15, 14, 00);
////        LocalDateTime d6= LocalDateTime.of(2021, Month.JUNE, 15, 15, 00);
////        LocalDateTime d7= LocalDateTime.of(2021, Month.JUNE, 15, 20, 00);
//
////        Delivery delivery1 = createDelivery("Mozart", "meat",  d2);
////        Delivery delivery2 = createDelivery("Mozart", "meat", d4);
////        Delivery delivery3 = createDelivery("Mozart", "meat", d4);
//
////        List<Delivery> deliveryList = Lists.newArrayList(delivery1,delivery2,delivery3);
////        for(Delivery delivery: deliveryList) {
////            if (delivery != null) {
////                deliveryRepository.save(delivery);
////            }
////        }
//
////        assertEquals(delivery1.getDriver().getName(), "Robert");
////        assertEquals(delivery2.getDriver().getName(), "Robert");
////        assertEquals(delivery3.getDriver().getName(), "Robert");
//
////        List<DriverDistance> driverDistanceList = waltService.getDriverRankReport();
////        assertEquals(driverDistanceList.get(0), "Robert");
//
//
//
//    }
}
