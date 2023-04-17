package uk.gov.dwp.uc.pairtest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.domain.User;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.Messages;
import uk.gov.dwp.uc.pairtest.util.TicketServiceValidator;

public class TicketServiceImpl implements TicketService {

	TicketPaymentService paymentService;
	SeatReservationService reservationServcie;
	final TicketServiceValidator validator = new TicketServiceValidator();
	int paymentAmount;
	int numberOfSeatsForReservation;

	public TicketServiceImpl(SeatReservationService reservationServcie) {
		super();
		this.paymentService = new TicketPaymentServiceImpl();
		this.reservationServcie = reservationServcie;
	}

	public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService reservationServcie) {
		super();
		this.paymentService = paymentService;
		this.reservationServcie = reservationServcie;
	}

	/**
	 * Should only have private methods other than the one below.
	 */
	@Override
	public void purchaseTickets(User user, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

		validateRequest(user, ticketTypeRequests);
		findNumberOfSeatsAndPaymentAmount(user, Arrays.asList(ticketTypeRequests));
		paymentService.makePayment(user.getAccountId(), paymentAmount);
		reservationServcie.reserveSeat(user.getAccountId(), numberOfSeatsForReservation);// payment is always a success :Assumption

	}

	private void findNumberOfSeatsAndPaymentAmount(User user, List<TicketTypeRequest> ticketTypeRequests) {
		int numberOfAdults = validator.countTotalTicketType(ticketTypeRequests,
				(ticketTypeRequest) -> ticketTypeRequest.getTicketType().equals(Type.ADULT));
		int numberOfChilds = validator.countTotalTicketType(ticketTypeRequests,
				(ticketTypeRequest) -> ticketTypeRequest.getTicketType().equals(Type.CHILD));
		numberOfSeatsForReservation = numberOfAdults + numberOfChilds;
		paymentAmount = Math.addExact(Math.multiplyExact(numberOfAdults, Messages.ADULT_FARE),
				Math.multiplyExact(numberOfChilds, Messages.CHILD_FARE));
		
		

	}

	private boolean validateRequest(User user, TicketTypeRequest[] ticketTypeRequests) throws InvalidPurchaseException {

		String exceptionMessage = "";

		// User null check
		if (Objects.isNull(user)) {
			exceptionMessage = Messages.EXCEPTION_WHEN_USER_NULL;
		}

		// Account ID check
		else if (user.validateUser().equals(Messages.INVALID_ACCOUNT_ID)) {
			exceptionMessage = Messages.INVALID_ACCOUNT_ID;
		} else if (ticketTypeRequests.length < 1) {
			exceptionMessage = Messages.EXCEPTION_WHEN_ZERO_TICKET_REQUEST;
		}

		else if (validator.checkIfAdultNotPresent(Arrays.asList(ticketTypeRequests))) {

			exceptionMessage = Messages.EXCEPTION_WHEN_NO_ADULT;
		}
		
		else if (validator.checkInfantCountInValid(Arrays.asList(ticketTypeRequests))) {
			exceptionMessage = Messages.INFANTS_NUMBER_GREATER_THAN_ADULTS;
		}


		else if (validator.checkTicketQuantityInValid(Arrays.asList(ticketTypeRequests))) {
			exceptionMessage = Messages.INVALID_TICKET_QUANTITY;
		} 

		if (!exceptionMessage.equals("")) {
			throw new InvalidPurchaseException(exceptionMessage);
		}

		return true;

	}

}
