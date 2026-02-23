package com.garage.management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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


    private LocalDate serviceDate;

    private Double totalCost;

    private String remarks;

    private boolean insuranceClaim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"vehicles", "hibernateLazyInitializer", "handler"})
    private Customer customer;

    @OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ServiceItem> itemsUsed;

    @OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Labour> labour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnoreProperties({"customer", "hibernateLazyInitializer", "handler"})
    private Vehicle vehicle;

    @Transient
    private Long vehicleId;

}
