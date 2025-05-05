package com.damiao.miniautorizador.core.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cards")
public class Card implements Serializable {

    private static final long serialVersionUID = -26674194015021L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "card_password")
    private String cardPassword;
    @Column(name = "available_balance", nullable = false)
    private BigDecimal availableBalance;

    @Column(name = "record_version")
    @Version
    private Integer recordVersion;

    @Embedded
    private Audit audit;
}
