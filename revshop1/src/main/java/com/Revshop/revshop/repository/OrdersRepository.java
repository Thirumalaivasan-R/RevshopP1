package com.Revshop.revshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Revshop.revshop.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>{

	List<Orders> findOrdersByUser_UserId(Long userId);
}