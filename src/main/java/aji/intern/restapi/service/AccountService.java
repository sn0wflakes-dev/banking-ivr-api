package aji.intern.restapi.service;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.account.UpdateEmailRequest;
import aji.intern.restapi.dto.account.UpdateEmailResponse;

public interface AccountService {
    UpdateEmailResponse updateCustomerEmail(MessageHeader header,
                                            UpdateEmailRequest request);
}
