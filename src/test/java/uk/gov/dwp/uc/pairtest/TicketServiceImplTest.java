package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.User;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.Messages;

//@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    private TicketServiceImpl unit;
    @InjectMocks
	private TicketPaymentService paymentService;
    @InjectMocks
	private SeatReservationService reservationServcie;

    @BeforeEach
    public void setUp(){
    	
         unit = new TicketServiceImpl(paymentService,reservationServcie);
         
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
        TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,11);
        TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        String message=  assertThrowsExactly(InvalidPurchaseException.class, 
        		()->unit.purchaseTickets(user, ticketTypeRequest1,ticketTypeRequest2,ticketTypeRequest3)).getMessage();
        assertEquals(Messages.INVALID_TICKET_QUANTITY, message);
    }
    
    @Test
    public void given_lessThanTwentyTicket_InOneOrder_when_purchaseTicket_then_success() {
    	User user = new User(28L);
        TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,10);
        TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,10);
        TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        assertDoesNotThrow(()->unit.purchaseTickets(user, ticketTypeRequest1,ticketTypeRequest2,ticketTypeRequest3));
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
    public void given_NumberOfInfants_LessThanAdults_when_purchaseTicket_then_Success(){
        User user = new User(Long.valueOf(5));
        TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,10);
        TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,9);
        assertDoesNotThrow(()->unit.purchaseTickets(user, ticketTypeRequest1,ticketTypeRequest2));
    }
    

//positive
    @Test
    public void adult_with_infant_purchaseTickets(){
        User user = new User(Long.valueOf(22));
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,3);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        unit.purchaseTickets(user, ticketTypeRequest_infant, ticketTypeRequest_adult);
        assertTrue(true);
    }

    //positive
    @Test
    public void adult_with_child_purchaseTickets(){
        User user = new User(Long.valueOf(10));
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        unit.purchaseTickets(user, ticketTypeRequest_child, ticketTypeRequest_adult);
        assertTrue(true);
    }
//positive
    @Test
    public void adult_with_infant_and_child_purchaseTickets(){
        User user = new User(Long.valueOf(10));
        TicketTypeRequest ticketTypeRequest_adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        TicketTypeRequest ticketTypeRequest_infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequest_child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);
        unit.purchaseTickets(user,ticketTypeRequest_adult,ticketTypeRequest_infant,ticketTypeRequest_child);
        assertTrue(true);
    }



}
