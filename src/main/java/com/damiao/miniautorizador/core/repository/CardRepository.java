package com.damiao.miniautorizador.core.repository;

import com.damiao.miniautorizador.core.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {

   boolean existsByCardNumber(String cardNumber);

   Card findByCardNumber(String cardNumber);

   @Query(value = "SELECT c.availableBalance FROM Card c WHERE c.cardNumber = :cardNumber")
   Optional<BigDecimal> findAvailableBalanceByNumber(@Param("cardNumber") String cardNumber);

}
