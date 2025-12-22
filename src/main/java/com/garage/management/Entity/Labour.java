package com.garage.management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "labour")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Labour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore  // IMPORTANT: Avoid circular JSON
    @JoinColumn(name = "service_id")
    private ServiceRecord serviceRecord;

    private String labourDescription;

    private Double amount;
}
