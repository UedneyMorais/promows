package com.supermarket.promows.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Garante a coluna {@code use_default_price_board_background} na tabela {@code companies}.
 * Em bases já criadas antes desse campo, o Hibernate {@code ddl-auto=update} nem sempre adiciona
 * a coluna no PostgreSQL; sem isso a API quebra ao consultar a empresa.
 */
@Configuration
public class CompanyTableSchemaFix {

    private static final Logger log = LoggerFactory.getLogger(CompanyTableSchemaFix.class);

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ApplicationRunner ensureCompanyUseDefaultColumn(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("""
                        ALTER TABLE companies
                        ADD COLUMN IF NOT EXISTS use_default_price_board_background BOOLEAN NOT NULL DEFAULT FALSE
                        """);
                log.debug("Schema OK: companies.use_default_price_board_background");
            } catch (Exception e) {
                log.warn(
                        "Não foi possível garantir a coluna use_default_price_board_background em companies: {}",
                        e.getMessage());
            }
        };
    }
}
