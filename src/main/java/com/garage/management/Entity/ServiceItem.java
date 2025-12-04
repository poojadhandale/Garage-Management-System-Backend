package com.garage.management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "service_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore  // IMPORTANT: Avoid circular JSON
    @JoinColumn(name = "service_id")
    private ServiceRecord serviceRecord;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private Integer quantityUsed;
}
