package aji.intern.restapi.service;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.account.UpdateAccountRequest;
import aji.intern.restapi.dto.account.UpdateAccountResponse;

public interface AccountService {
    UpdateAccountResponse updateAccount(MessageHeader header,
                                        UpdateAccountRequest request);
}
