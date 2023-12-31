package com.venkat.airlinewebapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venkat.airlinewebapp.dto.FlightDto;
import com.venkat.airlinewebapp.entity.Flight;
import com.venkat.airlinewebapp.repository.IFlightRepository;


@Service
public class FlightService implements IFlightService {

	@Autowired
	private IFlightRepository flightRepo;
	
	@Override
	public FlightDto createFlight(FlightDto flightDto) {
		
		Flight flight = new Flight();
		BeanUtils.copyProperties(flightDto, flight);
		
		flight = flightRepo.save(flight);
		
		BeanUtils.copyProperties(flight, flightDto);
		
		return flightDto;
	}
	@Override
	public FlightDto getFlight(Integer flightId) {
		Optional<Flight> flight = flightRepo.findById(flightId);
		
		FlightDto dto = null;
		if(flight.isPresent())
		{
			dto = new FlightDto();
			BeanUtils.copyProperties(flight.get(), dto);
		}
		
		return dto;
	}
	@Override
	public FlightDto updateFlight(FlightDto flightDto) {
		
		Optional<Flight> flightOpt =flightRepo.findById(flightDto.getId());
		if(flightOpt.isPresent())
		{
			Flight flight = new Flight();
			BeanUtils.copyProperties(flightDto, flight);
			
			flight = flightRepo.save(flight);
			
			BeanUtils.copyProperties(flight, flightDto);
			
		}
		else
		{
			//TODO throw Exception
		}
		
		return flightDto;
	}
	@Override
	public void deleteFlight(Integer flightId) {
		Optional<Flight> flightOpt =flightRepo.findById(flightId);
		if(flightOpt.isPresent())
		{
			flightRepo.deleteById(flightId);
			
		}
		else
		{
			//TODO throw Exception
		}
		
	}
	
	@Override
	public List<FlightDto> getAllFlights() {
		Iterable<Flight> itreable = flightRepo.findAll();
		
		List<FlightDto> flights = StreamSupport.stream(itreable.spliterator(), false).map(flight ->{
			FlightDto dto = new FlightDto();
			BeanUtils.copyProperties(flight, dto);
			return dto;
		}).collect(Collectors.toList());
		
		return flights;
	}
	@Override
	public FlightDto getFlightByflightNumber(String flightNumber) {
		
		Flight flight = flightRepo.findByFlightNumber(flightNumber);
		
		FlightDto dto  = new FlightDto();
		BeanUtils.copyProperties(flight, dto);
		
		return dto;
	}
}
