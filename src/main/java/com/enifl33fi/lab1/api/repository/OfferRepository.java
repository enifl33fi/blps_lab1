package com.enifl33fi.lab1.api.repository;

import com.enifl33fi.lab1.api.model.offers.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
