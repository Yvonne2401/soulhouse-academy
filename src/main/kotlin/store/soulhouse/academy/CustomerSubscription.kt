package store.soulhouse.academy
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import  org.axonframework.extension.spring.stereotype.EventSourced
import store.soulhouse.academy.CustomerEvent.Companion.CUSTOMER_SUBSCRIPTION_TAG
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@EventSourced(idType = UUID::class, tagKey = CUSTOMER_SUBSCRIPTION_TAG)
class CustomerSubscription @EntityCreator constructor() {

    lateinit var email: String
    var subscriptionExpiryDate: LocalDate = Instant.ofEpochMilli(0L).atZone(ZoneId.systemDefault()).toLocalDate()

        @EventSourcingHandler
        fun on(event: CustomerRegistered) {
            email = event.email
        }

    @EventSourcingHandler
    fun on (event: Subscribed){
        subscriptionExpiryDate = event.subscriptionExpiryDate
    }
}