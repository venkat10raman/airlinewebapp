package com.venkat.airlinewebapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venkat.airlinewebapp.dto.FlightDto;
import com.venkat.airlinewebapp.service.IFlightService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


@RestController
@Validated
public class FlightController {

	@Autowired
	private IFlightService flightService;

	@GetMapping(path = "/flight/{id}")
	public ResponseEntity<FlightDto> getFlight(@PathVariable(name="id") @Positive Integer flightId) {
		FlightDto flightDto = flightService.getFlight(flightId);
		return ResponseEntity.ok().body(flightDto);
	}
	
	@GetMapping(path = "/flight")
	public ResponseEntity<List<FlightDto>> getAllFlights() {
		List<FlightDto> flights = flightService.getAllFlights();
		return ResponseEntity.ok(flights);
	}
	
	@GetMapping(path = "/flight/flightData/{flightNumber}")
	public ResponseEntity<FlightDto> getFlightByFlightNumber(@PathVariable(name="flightNumber") String flightNumber) {
		FlightDto flightDto = flightService.getFlightByflightNumber(flightNumber);
		return ResponseEntity.ok(flightDto);
	}
	
	@PostMapping(path = "/flight")
	public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto) {
		FlightDto flight = flightService.createFlight(flightDto);
		return ResponseEntity.ok(flight);
	}

	@PutMapping(path = "/flight")
	public ResponseEntity<FlightDto> updateFlight(@RequestBody @Valid FlightDto flightDto) {
		FlightDto flight = flightService.updateFlight(flightDto);
		return ResponseEntity.ok(flight);
	}

	@DeleteMapping(path = "/flight/delete/{id}")
	public void deleteFlight(@PathVariable(name="id") @Positive Integer flightId) {
		flightService.deleteFlight(flightId);
		
	}

}
