package thirdparty.vouchers;

import thirdparty.vouchers.exception.InvalidDiscountCodeException;

public interface VoucherService {

    Discount getDiscountPercentage(long accountId, String voucherCode) throws InvalidDiscountCodeException;
}
