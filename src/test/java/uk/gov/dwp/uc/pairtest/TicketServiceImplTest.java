package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.User;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.Messages;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

//@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    private TicketServiceImpl unit;

    @BeforeEach
    public void setUp(){
         unit = new TicketServiceImpl();
    }
    
    
    @Test
    public void given_NoTicketInformation_when_purchaseTicket_thenThrowException() {
        User user = new User(1L);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user)).getMessage();
        assertEquals(Messages.EXCEPTION_WHEN_ZERO_TICKET_REQUEST, message);
    }

	@Test
	public void given_infant_only_when_purchaseTicket_thenThrowException() {
		User user = new User(2L);
		TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user,ticketTypeRequest)).getMessage();
        assertEquals(Messages.EXCEPTION_WHEN_NO_ADULT, message);
	}
	
    @Test
    public void given_child_only_when_purchaseTicket_thenThrowException() {
    	User user = new User(3L);
		TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
		
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user,ticketTypeRequest)).getMessage();
        assertEquals(Messages.EXCEPTION_WHEN_NO_ADULT, message);
    }
    
    @Test
    public void given_childAndInfant_only_when_purchaseTicket_thenThrowException() {
        User user = new User(4L);
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest_infant,ticketTypeRequest_child)).getMessage();
        assertEquals(Messages.EXCEPTION_WHEN_NO_ADULT, message);
		
		
    }
    
    
    @Test
    public void given_Adult_only_when_purchaseTicket_then_Success(){
    	User user = new User(5L);
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,
        		new Random().nextInt(20));
        assertDoesNotThrow(()->unit.purchaseTickets(user, ticketTypeRequest));
    } 
    
    
    @Test
    public void given_Null_User_when_purchaseTicket_then_ThrowException() {
    	User user = null;
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,
        		new Random().nextInt(20));
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest)).getMessage();
        assertEquals(Messages.EXCEPTION_WHEN_USER_NULL, message);
        
    }

    @Test
    public void given_Negative_AccountID_User_when_purchaseTicket_then_ThrowException() {
    	User user = new User(-1L);
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,
        		new Random().nextInt(20));
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest)).getMessage();
        assertEquals(Messages.INVALID_ACCOUNT_ID, message);
        		
    }
    
    
    @Test
    public void given_moreThanTwentyTicket_InSingleRequest_when_purchaseTicket_then_Exception() {
    	User user = new User(24l);
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,25);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest)).getMessage();
        assertEquals(Messages.INVALID_TICKET_QUANTITY, message);
    }
    
    @Test
    public void given_moreThanTwentyTicket_InOneOrder_when_purchaseTicket_then_Exception() {
    	User user = new User(25L);
        TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,10);
        TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,10);
        TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest1,ticketTypeRequest2,ticketTypeRequest3)).getMessage();
        assertEquals(Messages.INVALID_TICKET_QUANTITY, message);
    }
    
    

    @Test
    public void given_NumberOfInfants_GreaterThanAdults_when_purchaseTicket_then_Exception(){
        User user = new User(Long.valueOf(5));
        TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,10);
        TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,10);
        TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest1,ticketTypeRequest2,ticketTypeRequest3)).getMessage();
        assertEquals(Messages.INFANTS_NUMBER_GREATER_THAN_ADULTS, message);
    }

    @Test
    public void number_of_tickets_equal_twenty_purchaseTickets(){
        User user = new User(Long.valueOf(5));
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,20);
        unit.purchaseTickets(user, ticketTypeRequest);
        assertTrue(true);
    }



    @Test
    public void accountId_greater_than_zero_purchaseTickets(){
        User user = new User(Long.valueOf(2));
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,2);
        unit.purchaseTickets(user, ticketTypeRequest_adult, ticketTypeRequest_child);
        assertTrue(true);
    }


    @Test
    public void infant_no_of_tickets_equal_to_adult_no_of_tickets_purchaseTickets(){
        User user = new User(Long.valueOf(50));
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        unit.purchaseTickets(user, ticketTypeRequest_infant, ticketTypeRequest_adult);
        assertTrue(true);
    }

    @Test
    public void infant_no_of_tickets_less_than_the_adult_no_of_tickets_purchaseTickets(){
        User user = new User(Long.valueOf(50));
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        unit.purchaseTickets(user, ticketTypeRequest_infant, ticketTypeRequest_adult);
        assertTrue(true);
    }

    @Test
    public void infant_no_of_tickets_greater_than_the_adult_no_of_tickets_purchaseTickets(){
        User user = new User(Long.valueOf(20));
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,5);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        unit.purchaseTickets(user, ticketTypeRequest_infant, ticketTypeRequest_adult);
        assertTrue(true);

        // Need to Recheck this case
    }

    @Test
    public void adult_with_infant_purchaseTickets(){
        User user = new User(Long.valueOf(22));
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,3);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        unit.purchaseTickets(user, ticketTypeRequest_infant, ticketTypeRequest_adult);
        assertTrue(true);
    }

    @Test
    public void adult_with_child_purchaseTickets(){
        User user = new User(Long.valueOf(10));
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        unit.purchaseTickets(user, ticketTypeRequest_child, ticketTypeRequest_adult);
        assertTrue(true);
    }

    @Test
    public void adult_with_infant_and_child_purchaseTickets(){
        User user = new User(Long.valueOf(10));
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);
        unit.purchaseTickets(user,ticketTypeRequest_adult,ticketTypeRequest_infant,ticketTypeRequest_child);
        assertTrue(true);
    }
    @Test
    public void with_insufficient_funds_purchaseTickets()  throws InvalidPurchaseException{
        // TO-DO
    }

    @Test
    public void with_sufficient_funds_purchaseTickets(){

    }
    @Test
    public void no_seats_available_purchaseTickets(){

    }

}
