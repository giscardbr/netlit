package netlit.payments.domain.credit_card.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CreditCardRepository : CrudRepository<CreditCardEntity, UUID>
