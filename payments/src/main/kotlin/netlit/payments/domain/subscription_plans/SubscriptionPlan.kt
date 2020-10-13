package netlit.payments.domain.subscription_plans

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.springframework.data.repository.CrudRepository
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "value", "features")
@Relation(value = "subscription_plan", collectionRelation = "subscription_plans")
class SubscriptionPlan(@Id @JsonIgnore var uuid: UUID?,
                       @JsonProperty("name") var name: String?,
                       @JsonProperty("value") var value: Double?,
                       @JsonProperty("features") var features: Features?) : ResourceSupport()

@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("concurrent_accesses_amount", "accounts_amount")
class Features(
        @JsonProperty("concurrent_accesses_amount") var concurrentAccessesAmount: Long?,
        @JsonProperty("accounts_amount") var accountsAmount: Long?)

@Repository
interface SubscriptionPlanRepository : CrudRepository<SubscriptionPlan, UUID>
