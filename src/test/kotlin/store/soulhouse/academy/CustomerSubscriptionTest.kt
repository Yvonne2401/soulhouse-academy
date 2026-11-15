package store.soulhouse.academy

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.axonframework.eventsourcing.configuration.EventSourcedEntityModule
import org.axonframework.eventsourcing.configuration.EventSourcingConfigurer
import org.axonframework.messaging.commandhandling.configuration.CommandHandlingModule
import org.axonframework.test.fixture.AxonTestFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

private const val EMAIL = "life-is-like-a-box-of-chocolates@gmail.com"
private const val NAME = "Forrest Gump"

//@ExtendWith(SpringExtension::class)
@ExtendWith(OutputCaptureExtension::class)
class CustomerSubscriptionTest {
    lateinit var fixture : AxonTestFixture


    @BeforeEach
    fun beforeEach() {
        var configurer = EventSourcingConfigurer.create().registerEntity(EventSourcedEntityModule.autodetected(String::class.java,
            CustomerSubscription::class.java)).messaging { messaging ->
            messaging.registerCommandHandlingModule(
                CommandHandlingModule.named("subscription-processor")
                    .commandHandlers()
                    .autodetectedCommandHandlingComponent {_ -> SubscriptionProcessor()}
            )
        }
        fixture = AxonTestFixture.with(configurer)
    }

    @Test
    fun createCustomerSubscription(capturedOutput: CapturedOutput) {
        val courseId = UUID.randomUUID()
        fixture.given()
            .noPriorActivity()
            .`when`()
            .command(RegisterSubscription(EMAIL, NAME, courseId, BigDecimal(88), SubscriptionType.MONTH))
            .then()
            .events(CustomerRegistered(EMAIL, NAME), Subscribed(EMAIL,courseId, LocalDate.now().plusMonths(1)))
            .success()

        assertThat(capturedOutput).contains("Customer with email: ${EMAIL} registered")
        assertThat(capturedOutput).contains("IMHO this should always be printed")
    }

    @Test
    fun createCustomerSubscriptionRenewal(capturedOutput: CapturedOutput) {
        val courseId = UUID.randomUUID()
        fixture.given()
            .event(CustomerRegistered(EMAIL, NAME))
            .`when`()
            .command(RegisterSubscription(EMAIL, NAME, courseId, BigDecimal(88), SubscriptionType.YEAR))
            .then()
            .events(Subscribed(EMAIL,courseId, LocalDate.now().plusYears(1)))
            .success()
        assertThat(capturedOutput).contains("IMHO this should always be printed")
        assertThat(capturedOutput).contains("Subscription prolonged until ${LocalDate.now().plusYears(1)}")
    }


    @AfterEach
    fun after() {
        fixture.stop()
    }
}

