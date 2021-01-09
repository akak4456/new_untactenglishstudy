package com.untact.member.persistent;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.member.domain.EmailCheck;
import com.untact.member.domain.MemberEntity;

public interface MemberEntityRepository extends JpaRepository<MemberEntity, Long>, MemberEntityCustomRepository {
	public Optional<MemberEntity> findByEmail(String email);
	
	@Modifying
	@Transactional
	@Query("UPDATE MemberEntity m SET m.authKey=:authKey  where m.email=:email")
	void updateAuthKeyByEmail(@Param("authKey")String authKey,@Param("email")String email);
	
	@Modifying
	@Transactional
	@Query("UPDATE MemberEntity m SET m.emailCheck=:emailCheck  where m.email=:email")
	void updateEmailCheckByEmail(@Param("emailCheck")EmailCheck emailCheck,@Param("email")String email);
}
