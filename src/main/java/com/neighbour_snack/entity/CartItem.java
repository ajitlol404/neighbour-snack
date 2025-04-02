package com.neighbour_snack.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int packetCount; // Number of packets user wants

    @Column(nullable = false)
    private int packetSize; // Weight per packet (if weight-based) or pieces per packet (if piece-based)

    @Column(nullable = false)
    private int totalQuantity; // Total quantity (packetSize × packetCount)

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Price per unit at time of addition
}
