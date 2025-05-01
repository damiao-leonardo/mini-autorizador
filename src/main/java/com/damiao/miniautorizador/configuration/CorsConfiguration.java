package com.damiao.miniautorizador.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing) para a aplicação.
 *
 * Esta configuração permite apenas requisições originadas de domínios específicos e confiáveis.
 * Ao restringir as origens, protegemos a API contra acessos não autorizados vindos de aplicações
 * externas desconhecidas, evitando ataques como Cross-Site Request Forgery (CSRF) e uso indevido
 * dos endpoints públicos por terceiros.
 *
 * Domínios permitidos:
 * Neste momento a aplicação ficará aberto para qualquer dominio poder acessar, contudo em produção essa regra deverá ser alterada
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
