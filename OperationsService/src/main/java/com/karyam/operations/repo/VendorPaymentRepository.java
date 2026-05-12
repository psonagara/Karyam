package com.karyam.operations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karyam.operations.entity.VendorPayment;

public interface VendorPaymentRepository extends JpaRepository<VendorPayment, Long> {

}
