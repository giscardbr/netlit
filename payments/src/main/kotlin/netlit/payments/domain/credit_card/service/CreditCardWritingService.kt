package netlit.payments.domain.credit_card.service

import netlit.payments.domain.credit_card.adyen.Adyen
import netlit.payments.domain.credit_card.adyen.authorise.AdditionalDataRequest
import netlit.payments.domain.credit_card.adyen.authorise.AuthorizationRequest
import netlit.payments.domain.credit_card.repository.CreditCardEntity
import netlit.payments.domain.credit_card.repository.CreditCardRepository
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Service
@Validated
class CreditCardWritingService(val adyen: Adyen, val repository: CreditCardRepository) {

    private val log = LogManager.getLogger(CreditCardWritingService::class.java)

    fun create(@Valid creditCardRequest: CreditCardCreationRequest): CreditCardEntity {
        val creditCardId = UUID.randomUUID()!!
        val accountId = creditCardRequest.accountId
        val encryptedCard = creditCardRequest.encryptedData
        this.adyen.authorize(
                AuthorizationRequest(
                        additionalData = AdditionalDataRequest(encryptedCard),
                        reference = creditCardId.toString(),
                        shopperReference = accountId.toString()))
        val details = this.adyen.listRecurringDetails(accountId.toString())!!
        val card = details.card!!
        return this.repository.save(CreditCardEntity(
                id = creditCardId,
                number = card.number,
                issuingNetwork = details.variant,
                name = card.holderName,
                exp = "${card.expiryMonth}/${card.expiryYear}",
                accountId = accountId))
    }
}

class CreditCardCreationRequest(@field:NotEmpty val encryptedData: String, val accountId: UUID)