package netlit.payments.infra.database

import netlit.payments.domain.subscription_plans.Features
import netlit.payments.domain.subscription_plans.SubscriptionPlan
import netlit.payments.domain.subscription_plans.SubscriptionPlanRepository
import netlit.payments.infra.utils.UUIDString
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Initializer(private val planRepository: SubscriptionPlanRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        this.planRepository.save(
                SubscriptionPlan(
                        uuid = UUIDString("1133e017-90bb-4b38-8048-84ce35787dbc").uuid,
                        name = "Lendo muito",
                        value = 9.9,
                        features = Features(
                                concurrentAccessesAmount = 2,
                                accountsAmount = 2)))
        this.planRepository.save(
                SubscriptionPlan(
                        uuid = UUIDString("a6ebd9e8-ef35-43a2-ae5d-779e5c71764d").uuid,
                        name = "Lendo adoidado",
                        value = 11.9,
                        features = Features(
                                concurrentAccessesAmount = 4,
                                accountsAmount = 4)))
    }
}
