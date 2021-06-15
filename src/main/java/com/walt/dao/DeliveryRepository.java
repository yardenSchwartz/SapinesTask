package com.walt.dao;

import com.walt.model.City;
import com.walt.model.Driver;
import com.walt.model.Delivery;
import com.walt.model.DriverDistance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    List<Delivery> findAllDeliveriesByDriver(Driver driver);
    List<Delivery> findAllDeliveriesByDriverOrderByDeliveryTimeDesc(Driver driver);

//    List<Delivery> findAllDeliveriesByDriverBeforeLocalDateTime(Driver driver, LocalDateTime localDateTime);
//    @Query("Select t from Delivery t where t.driver.name =: name AND t.deliveryTime <= CURRENT_TIMESTAMP")
//    List<Delivery> findAllByDriverAndDeliveryTimeAfter(@Param("name") String name);


}


