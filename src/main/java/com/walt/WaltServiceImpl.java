package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@Transactional
public class WaltServiceImpl implements WaltService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
//    @Modifying(flushAutomatically  = true)
    public Delivery createOrderAndAssignDriver(Customer customer, Restaurant restaurant, LocalDateTime deliveryTime) {
        //null and existing checks
        //check if customer exist in the system
        if(customer==null || customerRepository.findByName(customer.getName())==null){
            System.out.print("The customer did not exist in the system");
            return null;
        }

        //maybe to do update for driver and delivery tables


        //choose driver
        HashMap<Driver, Integer> deliveriesPerFreeDriver = new HashMap<>();
        Integer minDeliveriesValue = Integer.MAX_VALUE;
        Driver chosenDriver = new Driver();

        //driver should be picked if he/she lives in the same city of the restaurant & customer
        if (customer.getCity().getId() == restaurant.getCity().getId()) {
            //check if deliveryTime-hour is after current hour at least in one hour
            //and check if we are talking on delivery to this current day
            if (deliveryTime.getMonth().getValue() == LocalDateTime.now().getMonth().getValue() && deliveryTime.getYear() == LocalDateTime.now().getYear() && deliveryTime.getMonth().getValue() == LocalDateTime.now().getMonth().getValue() && deliveryTime.getHour() >= LocalDateTime.now().plusHours(1).getHour()) {
                //yes

                //search for drivers at the same city
                List<Driver> drivers = driverRepository.findAllDriversByCity(customer.getCity());

                for (int i = 0; i < drivers.size(); i++) {
                    //check if the current driver is already on a delivery
                    boolean isFree = true;
                    List<Delivery> deliveriesByDriver = deliveryRepository.findAllDeliveriesByDriver(drivers.get(i));
//                Collections.sort(deliveriesByDriver, Comparator.comparing(Delivery::getDeliveryTime).reversed());

//                if(drivers.get(i).getIsFree()){
//                //the driver is free
//                    deliveriesPerFreeDriver.put(drivers.get(i),deliveriesByDriver.size());
//                }
                    //the driver isFree = False, we need to check if he/she finished the delivery or not
                    for (int j = 0; j < deliveriesByDriver.size(); j++) {
                        if (deliveriesByDriver.get(j).getDeliveryTime().getHour() == deliveryTime.getHour()) {
                            //the driver is already assign to delivery
                            isFree = false;
                            break;
                        }
                    }

                    if (isFree) {
                        //need to check if one hour from now will be ok according to deliveryTime
                        List<Delivery> historyDelivery = new ArrayList<>();
                        //take history deliveries

                        for(Delivery delivery: deliveriesByDriver){
                            if(delivery.getDeliveryTime().isBefore(deliveryTime)){
                                historyDelivery.add(delivery);
                            }
                        }

                        //he/she has no other delivery at the same time
                        int numOfDeliveriesPerDriver = historyDelivery.size();

                        deliveriesPerFreeDriver.put(drivers.get(i), numOfDeliveriesPerDriver);
                        if (minDeliveriesValue > numOfDeliveriesPerDriver) {
                            minDeliveriesValue = numOfDeliveriesPerDriver;
                            chosenDriver = drivers.get(i);
                        }
                    }
                }

                Delivery delivery = new Delivery();

                if (deliveriesPerFreeDriver.size() == 0) {
                    //there is no free driver
                    System.out.println("There is no free driver to assign");
                    return null;
                } else {
                    if (deliveriesPerFreeDriver.size() == 1) {
                        //assign delivery
                        Driver d = driverRepository.save(deliveriesPerFreeDriver.keySet().iterator().next().getDriver());
                        delivery = new Delivery(d, restaurant, customer, deliveryTime);
//                    driverRepository.updateDriver(d.getTotalDistance());
                    } else {
                        if (deliveriesPerFreeDriver.size() > 1) {
                            //choose the least busy driver according to the driver history
                            delivery = new Delivery(chosenDriver, restaurant, customer, deliveryTime);
                        }
                    }
//                deliveryRepository.save(delivery);
                }

                return delivery;
            }
            else{
                System.out.println("No driver will be able to deliver on time");
                return null;
            }
        }
        else
            System.out.println("Id's of city and restaurant do not match");
            return null;

    }

    @Override
    public List<DriverDistance> getDriverRankReport() {

        List<Driver> driverList = (List<Driver>) driverRepository.findAll();

        for(Driver driver: driverList){
            Long distance = Long.valueOf(0);
            List<Delivery> deliveryList = deliveryRepository.findAllDeliveriesByDriverOrderByDeliveryTimeDesc(driver);
            for(Delivery d: deliveryList){
                if(d.getDeliveryTime().isBefore(LocalDateTime.now())){
                    distance += d.getDistance();
                }
                else
                    break;
            }
            driver.setTotalDistance(distance);
            driverRepository.save(driver);
        }

        List<DriverDistance> driverDistances = (List<DriverDistance>) driverRepository.findAll(Sort.by(Sort.Direction.DESC, "totalDistance"));
        for (DriverDistance driverTemp : driverDistances) {

            System.out.println("driver name is: "+ driverTemp.getDriver().getName() +" and his total distance is: " + driverTemp.getTotalDistance());
        }
        return driverDistances;
    }

    @Override
    public List<DriverDistance> getDriverRankReportByCity(City city) {

        List<Driver> driverList = (List<Driver>) driverRepository.findAllDriversByCity(city);

        for(Driver driver: driverList){
            Long distancePerDriver = Long.valueOf(0);
            List<Delivery> deliveryList = deliveryRepository.findAllDeliveriesByDriverOrderByDeliveryTimeDesc(driver);
            for(Delivery d: deliveryList){
                if(d.getDeliveryTime().isBefore(LocalDateTime.now())){
                    distancePerDriver += d.getDistance();
                }
                else
                    break;
            }
            driver.setTotalDistance(distancePerDriver);
            driverRepository.save(driver);
        }

        List<DriverDistance> driverDistances = (List<DriverDistance>) driverRepository.findAllDriversByCityOrderByTotalDistanceDesc(city);
        for (DriverDistance driverTemp : driverDistances) {

            System.out.println("driver name is: "+ driverTemp.getDriver().getName() +" and his total distance is: " + driverTemp.getTotalDistance());
        }
        return driverDistances;
    }
}
