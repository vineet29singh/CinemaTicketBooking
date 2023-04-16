package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.User;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public interface TicketService {

    void purchaseTickets(User user, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException;

}
