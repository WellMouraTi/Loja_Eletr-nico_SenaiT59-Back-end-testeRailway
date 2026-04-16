package com.senai.lojaeletronicos.repository;

import com.senai.lojaeletronicos.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}