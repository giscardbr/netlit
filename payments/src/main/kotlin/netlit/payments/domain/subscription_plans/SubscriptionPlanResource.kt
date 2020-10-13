package netlit.payments.domain.subscription_plans

import netlit.payments.infra.error.EntityNotFoundException
import netlit.payments.infra.utils.UUIDString
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subscription-plans")
class SubscriptionPlanResource(private val planRepository: SubscriptionPlanRepository) {

    @GetMapping(produces = ["application/hal+json"])
    fun findAll(): Resources<SubscriptionPlan> {
        val plans = this.planRepository.findAll()

        for (plan in plans) {
            val id = plan.uuid!!
            plan.add(linkTo(SubscriptionPlanResource::class.java).slash(id).withSelfRel())
            plan.add(linkTo(SubscriptionPlanResource::class.java).slash(id).withRel("subscription_plan"))
        }

        return Resources(plans, linkTo(SubscriptionPlanResource::class.java).withSelfRel())
    }

    @GetMapping(value = ["/{id}"], produces = ["application/hal+json"])
    fun findById(@PathVariable id: String): Resource<SubscriptionPlan> {
        val uuid = UUIDString(id).uuid
        val plan = uuid?.let { planRepository.findById(it) }!!.orElseThrow { EntityNotFoundException() }
        return Resource(plan,
                linkTo(SubscriptionPlanResource::class.java).slash(plan.uuid).withSelfRel(),
                linkTo(SubscriptionPlanResource::class.java).slash(plan.uuid).withRel("subscription_plan"))
    }
}
