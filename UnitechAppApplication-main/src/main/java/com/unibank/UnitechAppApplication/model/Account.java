package com.unibank.UnitechAppApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    public enum Status{
        ACTIVE,
        DEACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
