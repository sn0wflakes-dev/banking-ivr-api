package aji.intern.restapi.service;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.card.ActivateCardNumberRequest;
import aji.intern.restapi.dto.card.ActivateCardNumberResponse;
import aji.intern.restapi.dto.card.AuthenticateCardRequest;
import aji.intern.restapi.dto.card.AuthenticateCardResponse;

public interface CardService {
    AuthenticateCardResponse authCard(MessageHeader messageHeader, AuthenticateCardRequest request);
    ActivateCardNumberResponse activateCard(MessageHeader messageHeader, ActivateCardNumberRequest request);
}
