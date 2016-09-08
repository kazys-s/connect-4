package com.connect4.web.configuration;

import com.connect4.domain.Player;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Player.class)
                .tags(
                        new Tag("Players", "Create, view players"),
                        new Tag("Games", "Create, view, play games")
                )
                .select()
                .paths(or(
                        regex("/players.*"),
                        regex("/games.*")
                ))
                .build();
    }
}