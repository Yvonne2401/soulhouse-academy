package store.soulhouse.academy

import org.axonframework.eventsourcing.annotation.EventTag
import store.soulhouse.academy.CustomerEvent.Companion.CUSTOMER_SUBSCRIPTION_TAG
import java.time.LocalDate
import java.util.UUID

sealed interface Event

sealed interface CustomerEvent : Event {
    companion object {
        const val CUSTOMER_SUBSCRIPTION_TAG = "customerSubscription"
    }
}

data class CustomerRegistered(@EventTag(key = CUSTOMER_SUBSCRIPTION_TAG) val email: String, val fullName: String) : CustomerEvent
data class Subscribed(@EventTag(key = CUSTOMER_SUBSCRIPTION_TAG)  val email: String, val courseId: UUID, val subscriptionExpiryDate: LocalDate) : CustomerEvent
data class SubscriptionExpired(@EventTag(key = CUSTOMER_SUBSCRIPTION_TAG)  val email: String, val courseId: UUID)
