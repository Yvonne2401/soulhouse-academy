package store.soulhouse.academy

import org.axonframework.conversion.DelegatingGeneralConverter
import org.axonframework.conversion.GeneralConverter
import org.axonframework.conversion.jackson2.Jackson2Converter
import org.axonframework.eventsourcing.configuration.EventSourcingConfigurer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AcademyApplication {
	companion object {
		@JvmStatic
		fun configurer(): EventSourcingConfigurer = EventSourcingConfigurer.create()
			.componentRegistry {
				it.registerComponent(GeneralConverter::class.java) { _ ->
					DelegatingGeneralConverter(Jackson2Converter())
				}
			}
	}
}

fun main(args: Array<String>) {
	runApplication<AcademyApplication>(*args)
}
