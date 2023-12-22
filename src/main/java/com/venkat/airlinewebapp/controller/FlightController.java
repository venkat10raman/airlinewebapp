package com.venkat.airlinewebapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
		Link link = linkTo(methodOn(FlightController.class).getFlight(flightId)).withSelfRel();
		FlightDto flightDto = flightService.getFlight(flightId);
		flightDto.add(link);
		return ResponseEntity.ok().body(flightDto);
	}
	
	@GetMapping(path = "/flight")
	public ResponseEntity<List<FlightDto>> getAllFlights() {
		List<FlightDto> flights = flightService.getAllFlights();
		
		for(FlightDto dto : flights) {
			Link linkById = linkTo(methodOn(FlightController.class).getFlight(dto.getId())).withSelfRel();
			dto.add( linkById);
		}
		
		return ResponseEntity.ok(flights);
	}
	
	@GetMapping(path = "/flight/flightData/{flightNumber}")
	public ResponseEntity<FlightDto> getFlightByFlightNumber(@PathVariable(name="flightNumber") String flightNumber) {
		FlightDto flightDto = flightService.getFlightByflightNumber(flightNumber);
		Link link = linkTo(methodOn(FlightController.class).getFlightByFlightNumber(flightNumber)).withSelfRel();
		flightDto.add(link);
		return ResponseEntity.ok(flightDto);
	}
	
	@PostMapping(path = "/flight")
	public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto) {
		FlightDto dto = flightService.createFlight(flightDto);
		Link link = linkTo(methodOn(FlightController.class).getFlight(dto.getId())).withSelfRel();
		dto.add(link);
		return ResponseEntity.ok(dto);
	}

	@PutMapping(path = "/flight")
	public ResponseEntity<FlightDto> updateFlight(@RequestBody @Valid FlightDto flightDto) {
		FlightDto dto = flightService.updateFlight(flightDto);
		Link link = linkTo(methodOn(FlightController.class).getFlight(dto.getId())).withSelfRel();
		dto.add(link);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping(path = "/flight/delete/{id}")
	public void deleteFlight(@PathVariable(name="id") @Positive Integer flightId) {
		flightService.deleteFlight(flightId);
		
	}

}
