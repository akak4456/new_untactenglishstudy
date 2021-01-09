package com.untact.vocabularyitem.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.vocabularyitem.domain.VocabularyItem;

public interface VocabularyItemRepository extends JpaRepository<VocabularyItem, Long>, VocabularyItemCustomRepository {

}
