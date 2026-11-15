package store.soulhouse.academy

import org.axonframework.modelling.annotation.TargetEntityId
import java.math.BigDecimal
import java.util.UUID

data class RegisterSubscription(
    @TargetEntityId
    val email: String,
    val fullName: String,
    val courseId: UUID,
    val amount: BigDecimal,
    val type: SubscriptionType
)


enum class SubscriptionType {
    MONTH,
    YEAR
}
