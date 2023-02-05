package com.meal.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.meal.repositories", "com.meal.model"])
@Configuration
class SpringConfig