package com.untact.antonym.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.antonym.domain.Antonym;

public interface AntonymRepository extends JpaRepository<Antonym, Long>, AntonymCustomRepository {

}
