package uk.gov.dwp.uc.pairtest.util;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;


class TicketServiceValidatorTest {
	
	private TicketServiceValidator unit;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		unit = new TicketServiceValidator();
	}

	@Test
	final void givenRequestWithNoAdult_when_checkIfAdultNotPresent_true() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        boolean outputBool=  unit.checkIfAdultNotPresent(List.of(ticketTypeRequest_infant,ticketTypeRequest_child));
		assertTrue(outputBool);
	}
	
	@Test
	final void givenRequestWithNoAdult_when_checkIfAdultNotPresent_false() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        boolean outputBool=  unit.checkIfAdultNotPresent(List.of(ticketTypeRequest_infant,ticketTypeRequest_child,ticketTypeRequest_adult));
		assertFalse(outputBool);
	}
	
	
	@Test
	final void givenRequestWithInfantCountGreaterThanAdult_when_checkInfantCountInValid_true() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        boolean outputBool=  unit.checkInfantCountInValid(List.of(ticketTypeRequest_infant,ticketTypeRequest_child));
		assertTrue(outputBool);
	}
	
	@Test
	final void givenRequestWithEqualInfantAndAdultCount_when_checkInfantCountInValid_false() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        boolean outputBool=  unit.checkInfantCountInValid(List.of(ticketTypeRequest_infant,ticketTypeRequest_child,ticketTypeRequest_adult));
		assertFalse(outputBool);
	}
	
	
	@Test
	final void givenRequestWithTicketCountGreaterThanTwenty_when_checkTicketQuantityInValid_true() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,10);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,11);
        boolean outputBool=  unit.checkTicketQuantityInValid(List.of(ticketTypeRequest_infant,ticketTypeRequest_child,ticketTypeRequest_adult));
		assertTrue(outputBool);
	}
	
	@Test
	final void givenRequestWithTicketCountGreaterThanTwenty_when_checkTicketQuantityInValid_false() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,19);
        boolean outputBool=  unit.checkTicketQuantityInValid(List.of(ticketTypeRequest_infant,ticketTypeRequest_adult));
		assertFalse(outputBool);
	}
	
	@Test
	final void givenRequestWithTicketCountLessThanTwenty_Ignore_Infant_Count_when_checkTicketQuantityInValid_false() {
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,19);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        boolean outputBool=  unit.checkTicketQuantityInValid(List.of(ticketTypeRequest_infant,ticketTypeRequest_adult,ticketTypeRequest_child));
		assertFalse(outputBool);
	}
	
	@Test
	final void testCountTotalTicket_AdultType() {
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,19);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        int ticketCount =  unit.countTotalTicketType(List.of(ticketTypeRequest_adult,ticketTypeRequest_child),
        		(ticketTypeRequest) -> ticketTypeRequest.getTicketType().equals(Type.ADULT));
		assertTrue(ticketCount == 20);
	}
	
	@Test
	final void testCountTotalTicket_ForChildType() {
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,11);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,10);
        int ticketCount =  unit.countTotalTicketType(List.of(ticketTypeRequest_adult,ticketTypeRequest_child),
        		(ticketTypeRequest) -> ticketTypeRequest.getTicketType().equals(Type.CHILD));
		assertTrue(ticketCount == 11);
	}

	



}
