package com.auth0.example;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan(basePackages = "com.auth0.example")
@EnableAutoConfiguration
@PropertySources({ @PropertySource("classpath:application.properties"), @PropertySource("classpath:auth0.properties") })
public class App {

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer cookieProcessorCustomizer() {
		return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if (container instanceof TomcatEmbeddedServletContainerFactory) {
					((TomcatEmbeddedServletContainerFactory) container).addContextCustomizers(new TomcatContextCustomizer() {

						@Override
						public void customize(Context context) {
							context.setCookieProcessor(new LegacyCookieProcessor());
						}

					});
				}
			}

		};
	}

}
