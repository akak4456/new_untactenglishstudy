package com.untact.phrase.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.phrase.domain.Phrase;

public interface PhraseRepository extends JpaRepository<Phrase, Long>, PhraseCustomRepository {

}
