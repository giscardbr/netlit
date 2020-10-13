package netlit.payments.domain.credit_card.repository

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
data class CreditCardEntity(
        @Id
        var id: UUID? = null,
        @NotNull
        var number: String? = null,
        @NotNull
        var issuingNetwork: String? = null,
        @NotNull
        var name: String? = null,
        @NotNull
        var exp: String? = null,
        @NotNull
        var accountId: UUID? = null,
        @NotNull
        val recurringDetailReference: String? = null)