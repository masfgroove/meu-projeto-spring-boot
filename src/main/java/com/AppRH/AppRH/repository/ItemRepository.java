package com.AppRH.AppRH.repository;

import org.springframework.data.repository.CrudRepository;
import com.AppRH.AppRH.models.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
    // Adicione métodos personalizados, se necessário
}
