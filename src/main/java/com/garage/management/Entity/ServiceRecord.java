package com.garage.management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate serviceDate;

    private Double totalCost;

    private String remarks;

    @OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceItem> itemsUsed = new ArrayList<>();

    @OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Labour> labour = new ArrayList<>();

}
