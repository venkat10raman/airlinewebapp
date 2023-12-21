package com.venkat.airlinewebapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.venkat.airlinewebapp.entity.Flight;


@Repository
public interface IFlightRepository extends CrudRepository<Flight, Integer>{

	Flight findByFlightNumber(String flightNumber);
	
}
