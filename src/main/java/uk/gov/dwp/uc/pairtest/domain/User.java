package uk.gov.dwp.uc.pairtest.domain;

import uk.gov.dwp.uc.pairtest.util.Messages;

public final class User {

	private final Long accountId;

    public User(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }
    
    public String validateUser() {
    	int ZERO = 0;
		return accountId!=null && accountId.intValue()>ZERO ? Messages.VALID_ACCOUNT_ID: Messages.INVALID_ACCOUNT_ID;
    
    }

}
