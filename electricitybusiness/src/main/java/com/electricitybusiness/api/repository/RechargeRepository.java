package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
} 