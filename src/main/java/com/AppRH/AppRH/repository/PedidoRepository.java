package com.AppRH.AppRH.repository;

import org.springframework.data.repository.CrudRepository;

import com.AppRH.AppRH.models.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {
    Pedido findByCodigo(long codigo);
}