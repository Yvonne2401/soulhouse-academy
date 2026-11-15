import org.axonframework.common.configuration.ApplicationConfigurer
import org.axonframework.eventsourcing.configuration.EventSourcingConfigurer
import org.axonframework.test.extension.AxonTestFixtureProvider
import org.axonframework.test.fixture.AxonTestFixture
import store.soulhouse.academy.AcademyApplication

data object TestFixtures {

    fun acadamyTestFixture(configuration: EventSourcingConfigurer.() -> ApplicationConfigurer): AxonTestFixtureProvider = AxonTestFixtureProvider {
        AxonTestFixture.with(
            AcademyApplication.configurer().configuration()
        )
    }

}