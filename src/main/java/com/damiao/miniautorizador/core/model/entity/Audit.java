package com.damiao.miniautorizador.core.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class Audit implements Serializable {

    @Serial
    private static final long serialVersionUID = -1694532445568540954L;

    @CreatedDate
    @Column(name = "dh_created_at", updatable = false)
    private LocalDateTime dhCreated;

    @LastModifiedDate
    @Column(name = "dh_updated_at")
    private LocalDateTime dhUpdated;
}

