package com.meal.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.dinner.repositories", "com.dinner.model"])
@Configuration
class SpringConfig