package com.untact.thesaurus.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.thesaurus.domain.Thesaurus;

public interface ThesaurusRepository extends JpaRepository<Thesaurus, Long>, ThesaurusCustomRepository {

}
