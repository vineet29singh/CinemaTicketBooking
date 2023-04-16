/**
 * 
 */
package uk.gov.dwp.uc.pairtest.util;

/**
 * @author vinee
 *
 */
public interface Messages {
	
     String EXCEPTION_WHEN_ZERO_TICKET_REQUEST = "Atleast one TicketType should be provided for purchase.";
	 String EXCEPTION_WHEN_NO_ADULT = "Atleast one adult should be present for ticket purchase.";
	 String EXCEPTION_WHEN_USER_NULL = "User should be provided for ticket purchase.";
     String INVALID_ACCOUNT_ID = "Invalid account Id, Account id should be a number greater than Zero.";
     String INVALID_TICKET_QUANTITY = "Only a maximum of 20 tickets that can be purchased at a time.";
     //Self introduced
     String INFANTS_NUMBER_GREATER_THAN_ADULTS = "Number of infants should not be greater than number of adults.";
	 String VALID_ACCOUNT_ID = "VALID ACCOUNT ID";
	 int CHILD_FARE = 10;
	 int ADULT_FARE = 20;

}
