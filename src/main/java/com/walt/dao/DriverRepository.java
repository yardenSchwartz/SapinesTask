package com.walt.dao;

import com.walt.model.City;
import com.walt.model.Driver;
import com.walt.model.DriverDistance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DriverRepository extends CrudRepository<Driver,Long> {
    List<Driver> findAllDriversByCity(City city);
//    List<Driver> findAllOrderByTotalDistanceAsc();

    Driver findByName(String name);

    List<DriverDistance> findAll(Sort totalDistance);
    List<DriverDistance> findAllDriversByCityOrderByTotalDistanceDesc(City city);
}
