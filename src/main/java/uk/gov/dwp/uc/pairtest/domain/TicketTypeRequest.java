package uk.gov.dwp.uc.pairtest.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Immutable Object
 */
public final class TicketTypeRequest {

    private int noOfTickets;
    private Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }
    
	public static Optional<Type> getTicketType(String ticketType) {
		
		Objects.requireNonNull(ticketType,"ticket type must not be null");
		
		for (Type currType : Type.values()) {
			
		// trimmed the parameter	
			if (currType.name().equalsIgnoreCase(ticketType.trim())) {
				return Optional.ofNullable(currType);
			}

		}

		return Optional.empty();

	}
    
    
}
