package com.lucasrech.confeitandoapi.flavor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FlavorRepository extends JpaRepository<FlavorEntity, Integer> {
    //TODO: Verificar se é necessário adicionar outros métodos personalizados aqui

    FlavorEntity findByTitle(String title);
}
