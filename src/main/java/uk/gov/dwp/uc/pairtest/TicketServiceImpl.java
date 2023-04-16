package uk.gov.dwp.uc.pairtest;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.domain.User;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.Messages;


public class TicketServiceImpl implements TicketService {


	TicketPaymentService paymentService=  new TicketPaymentServiceImpl();



	/**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(User user, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        
    	boolean isValid = validateRequest(user,ticketTypeRequests);
    	int paymentToBeMade = calculatePaymentAmount( user, ticketTypeRequests);
    	paymentService.makePayment(user.getAccountId(), paymentToBeMade);
    	
    	
    }

    
	private int calculatePaymentAmount(User user, TicketTypeRequest[] ticketTypeRequests) {
		
		int numberOfAdults = countTotalTicketType(ticketTypeRequests, (ticketType) -> ticketType.equals(Type.ADULT));
		int numberOfChilds = countTotalTicketType(ticketTypeRequests, (ticketType) -> ticketType.equals(Type.CHILD));
		int totalFare = Math.addExact(Math.multiplyExact(numberOfAdults, Messages.ADULT_FARE), 
					    Math.multiplyExact(numberOfChilds, Messages.CHILD_FARE));
		return totalFare;
		
	}


	private boolean validateRequest(User user, TicketTypeRequest[] ticketTypeRequests) throws InvalidPurchaseException {
		
		Predicate<TicketTypeRequest>  checkForInfant =  (ticketTypeRequest) -> ticketTypeRequest.getTicketType().equals(Type.INFANT);
		Predicate<Type>  checkForAdult =  (ticketType) -> ticketType.equals(Type.ADULT);
		
	//  User null check	
		if(Objects.isNull(user)) {
			throw new InvalidPurchaseException(Messages.EXCEPTION_WHEN_USER_NULL);
		}
// Account ID check 
	     String message = user.validateUser();
		 if(message.equals(Messages.INVALID_ACCOUNT_ID)) {
			 throw new InvalidPurchaseException(Messages.INVALID_ACCOUNT_ID);
			 
		 }
// Check if ticketTypeRequests is not provided	
		if(ticketTypeRequests.length<1) {
			throw new InvalidPurchaseException(Messages.EXCEPTION_WHEN_ZERO_TICKET_REQUEST);
		}
		
 // true only if at least one adult is present  
		
		boolean ifAdultPresent = Stream.of(ticketTypeRequests)
									.map(TicketTypeRequest::getTicketType)
				                     .anyMatch(checkForAdult);
		if(!ifAdultPresent) {
			
			throw new InvalidPurchaseException(Messages.EXCEPTION_WHEN_NO_ADULT);
		}
		
	// Check ticket quantity is (1,20), ignore Infant(Adult+Child <=20)	
		Integer totalNumberOFtickets = Stream.of(ticketTypeRequests)
				.filter(checkForInfant.negate()) // ignoring infant in total count
				.map(TicketTypeRequest::getNoOfTickets)
				.reduce(Integer::sum).get();
		
		if(totalNumberOFtickets.intValue()>20) {
			
			throw new InvalidPurchaseException(Messages.INVALID_TICKET_QUANTITY);
		}
		
		// Check if number of infants are greater than adults
		int numberOfAdults = countTotalTicketType(ticketTypeRequests, (ticketType) -> ticketType.equals(Type.ADULT));
		int numberOfInfants = countTotalTicketType(ticketTypeRequests, (ticketType) -> ticketType.equals(Type.INFANT));
		
		if(numberOfInfants>numberOfAdults) {
			
			throw new InvalidPurchaseException(Messages.INFANTS_NUMBER_GREATER_THAN_ADULTS);
		}
		
		return true;
		
	}


	/**
	 * @param ticketTypeRequests
	 * @param checkForAdult
	 * @return
	 */
	private int countTotalTicketType(TicketTypeRequest[] ticketTypeRequests, Predicate<Type> checkForType) {
		Long numberOfAdults = Stream.of(ticketTypeRequests)
				.map(TicketTypeRequest::getTicketType)
				.filter(checkForType) 
				.count();
		return numberOfAdults.intValue();
	}
}
