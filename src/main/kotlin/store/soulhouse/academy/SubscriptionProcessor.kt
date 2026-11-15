package store.soulhouse.academy

import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component
import store.soulhouse.academy.SubscriptionType.MONTH
import store.soulhouse.academy.SubscriptionType.YEAR
import java.time.LocalDate

@Component
class SubscriptionProcessor {

    @CommandHandler
    fun handle(command: RegisterSubscription, @InjectEntity(idProperty = "email") customerSubscription: CustomerSubscription?, eventAppender: EventAppender) {
        customerSubscription ?:
            eventAppender.append(
                CustomerRegistered(
                    email = command.email,
                    fullName = command.fullName
                )
            )
            println("Customer with email: ${command.email} registered")

        customerSubscription?.let {
                println("IMHO this should always be printed")
        }
        eventAppender.append(
            Subscribed(
                email = command.email,
                courseId = command.courseId,
                subscriptionExpiryDate = when (command.type) {
                    MONTH -> LocalDate.now().plusMonths(1)
                    YEAR -> LocalDate.now().plusYears(1)
                }
            )
        )
        customerSubscription?.let {
            if ( it.subscriptionExpiryDate.isAfter(LocalDate.now())) {
                println("Subscription prolonged until ${it.subscriptionExpiryDate}")
            }
        }
    }
}