package com.venkat.airlinewebapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venkat.airlinewebapp.dto.FlightDto;
import com.venkat.airlinewebapp.exception.ApiErrorResponse;
import com.venkat.airlinewebapp.service.IFlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


@RestController
@Validated
@Tag(name = "Flight Controller", description = "Flight Resource")
public class FlightController {

	@Autowired
	private IFlightService flightService;

	@Operation(summary = "Get Flight", description = "Fetch the Flight record by flight Id", method = "GET", tags = {"Flight"})
	@Parameter(name = "id", example = "8", required = true, description = "Flight Id", in = ParameterIn.PATH)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetch is Successful",
					content = @Content(
							schema = @Schema(implementation = FlightDto.class),
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "404", description = "Flight Resource Not Fount",
					content = @Content(
							schema = @Schema(implementation = ApiErrorResponse.class),
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "500", description = "Server Exception",
					content = @Content(
							schema = @Schema(implementation = ApiErrorResponse.class),
							mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@GetMapping(path = "/flight/{id}")
	@PreAuthorize("hasAuthority('flight_read')")
	public ResponseEntity<FlightDto> getFlight(@PathVariable(name="id") @Positive Integer flightId) {
		Link link = linkTo(methodOn(FlightController.class).getFlight(flightId)).withSelfRel();
		FlightDto flightDto = flightService.getFlight(flightId);
		flightDto.add(link);
		return ResponseEntity.ok().body(flightDto);
	}
	
	@Operation(summary = "Fetch All Flights", description = "Fetch All the Flight records", method = "GET", tags = {"Flight"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fetch is Successful",
					content = @Content(
							schema = @Schema(implementation = FlightDto.class),
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "404", description = "Flight Resource Not Found",
					content = @Content(
							schema = @Schema(implementation = ApiErrorResponse.class),
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "500", description = "Server Exception",
					content = @Content(
							schema = @Schema(implementation = ApiErrorResponse.class),
							mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@GetMapping(path = "/flight")
	@PreAuthorize("hasAuthority('flight_read')")
	public ResponseEntity<List<FlightDto>> getAllFlights() {
		List<FlightDto> flights = flightService.getAllFlights();
		
		for(FlightDto dto : flights) {
			Link linkById = linkTo(methodOn(FlightController.class).getFlight(dto.getId())).withSelfRel();
			dto.add( linkById);
		}
		
		return ResponseEntity.ok(flights);
	}
	
	@Operation(summary = "Fetch Flight", description = "Fetch the Flight record by flight number", method = "GET", tags = {"Flight"})
	@Parameter(name = "flightNumber", example = "22", required = true, description = "Flight Number", in = ParameterIn.PATH)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Fetch is Successful",
					content = @Content(schema = @Schema(implementation = FlightDto.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "404" , description = "Flight Resource Not Found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode="500" , description = "Server Exception",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE))
	})
	@GetMapping(path = "/flight/flightData/{flightNumber}")
	@PreAuthorize("hasAuthority('flight_read')")
	public ResponseEntity<FlightDto> getFlightByFlightNumber(@PathVariable(name="flightNumber") String flightNumber) {
		FlightDto flightDto = flightService.getFlightByflightNumber(flightNumber);
		Link link = linkTo(methodOn(FlightController.class).getFlightByFlightNumber(flightNumber)).withSelfRel();
		flightDto.add(link);
		return ResponseEntity.ok(flightDto);
	}
	
	@Operation(summary = "Create Flight" , description = "Create the Flight record" , method = "POST" , tags= {"Flight"})
	@Parameter(name="flightDto" , description = "Flight Dto in the request body",
	required=true, content = @Content(schema = @Schema(implementation = FlightDto.class)), 
	in = ParameterIn.DEFAULT)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Flight Record Successfully Created",
					content = @Content(schema = @Schema(implementation = FlightDto.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode="500" , description = "Server Exception",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE))
	})
	@PostMapping(path = "/flight", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('flight_create')")
	public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto) {
		FlightDto dto = flightService.createFlight(flightDto);
		Link link = linkTo(methodOn(FlightController.class).getFlight(dto.getId())).withSelfRel();
		dto.add(link);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Update Flight" , description = "Update the Flight record" , method = "PUT" , tags= {"Flight"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Flight Record Successfully Created",
					content = @Content(schema = @Schema(implementation = FlightDto.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "404" , description = "Flight Resource Not Found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode="500" , description = "Server Exception",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE))
	})
	@PutMapping(path = "/flight",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('flight_update')")
	public ResponseEntity<FlightDto> updateFlight(
			@Parameter(name="flightDto" , description = "Flight Dto in the request body",
			required=true, content = @Content(schema = @Schema(implementation = FlightDto.class)), 
			in = ParameterIn.DEFAULT)
			@RequestBody @Valid FlightDto flightDto) {
		FlightDto dto = flightService.updateFlight(flightDto);
		Link link = linkTo(methodOn(FlightController.class).getFlight(dto.getId())).withSelfRel();
		dto.add(link);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Delete Flight" , description = "Delete the Flight record" , method = "DELETE" , tags= {"Flight"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Flight Record Successfully Created",
					content = @Content()),
			@ApiResponse(responseCode = "404" , description = "Flight Resource Not Found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode="500" , description = "Server Exception",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
					mediaType=MediaType.APPLICATION_JSON_VALUE))
	})
	@DeleteMapping(path = "/flight/delete/{id}")
	@PreAuthorize("hasAuthority('flight_delete')")
	public void deleteFlight(@PathVariable(name="id") @Positive Integer flightId) {
		flightService.deleteFlight(flightId);
		
	}

}
