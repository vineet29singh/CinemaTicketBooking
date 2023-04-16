package uk.gov.dwp.uc.pairtest.util;

import java.util.List;
import java.util.function.Predicate;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketServiceValidator {

	
	public boolean checkIfAdultNotPresent(List<TicketTypeRequest> ticketTypeRequests) {
		
		return ticketTypeRequests.stream().map(TicketTypeRequest::getTicketType)
				.noneMatch(ticketType -> ticketType.equals(Type.ADULT));
	}
	
	public boolean checkTicketQuantityInValid(List<TicketTypeRequest> ticketTypeRequests) {
		Predicate<TicketTypeRequest> infantType = ticketTypeRequest -> ticketTypeRequest.getTicketType()
				.equals(Type.INFANT);
		Integer totalNumberOFtickets = ticketTypeRequests.stream().filter(infantType.negate()) // ignoring infant in
				.map(TicketTypeRequest::getNoOfTickets).reduce(Integer::sum).get();
		return (totalNumberOFtickets.intValue() > 20) ? true : false;
	}
	
	
	public boolean checkInfantCountInValid(List<TicketTypeRequest> ticketTypeRequests) {
		
		int numberOfAdults = countTotalTicketType(ticketTypeRequests, (ticketType) -> ticketType.equals(Type.ADULT));
		int numberOfInfants = countTotalTicketType(ticketTypeRequests, (ticketType) -> ticketType.equals(Type.INFANT));
		return (numberOfInfants > numberOfAdults)? true:false;
	}
	
	
	public int countTotalTicketType(List<TicketTypeRequest> ticketTypeRequests, Predicate<Type> checkForType) {
		Long numberOfAdults = ticketTypeRequests.stream()
				.map(TicketTypeRequest::getTicketType)
				.filter(checkForType) 
				.count();
		return numberOfAdults.intValue();
	}
		
	
}
