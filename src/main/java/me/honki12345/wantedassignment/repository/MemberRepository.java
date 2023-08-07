package me.honki12345.wantedassignment.repository;

import me.honki12345.wantedassignment.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
