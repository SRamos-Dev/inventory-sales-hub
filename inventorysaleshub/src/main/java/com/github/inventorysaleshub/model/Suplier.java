package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Suplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String contactInfo;

    @OneToMany(mappedBy = "suplier")
    private List<Product> products;

}
