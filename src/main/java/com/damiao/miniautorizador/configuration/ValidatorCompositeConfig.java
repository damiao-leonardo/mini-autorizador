package com.damiao.miniautorizador.configuration;

import com.damiao.miniautorizador.core.service.validator.CardExistValidation;
import com.damiao.miniautorizador.core.service.validator.CompositeTransactionValidator;
import com.damiao.miniautorizador.core.service.validator.PasswordValidation;
import com.damiao.miniautorizador.core.service.validator.SufficientBalanceValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidatorCompositeConfig {

    @Bean
    public CompositeTransactionValidator compositeTransactionValidator() {
        return new CompositeTransactionValidator(List.of(
                new CardExistValidation(),
                new PasswordValidation(),
                new SufficientBalanceValidator()
        ));
    }
}
