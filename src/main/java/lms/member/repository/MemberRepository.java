package lms.member.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lms.member.domain.Member;
import lms.member.domain.SocialType;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional

public class MemberRepository {

    private final EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    private Optional<Member> findSingleResult(String jpql, String paramName, Object paramValue) {
        TypedQuery<Member> query = em.createQuery(jpql, Member.class).setParameter(paramName, paramValue);
        List<Member> resultList = query.getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    public Optional<Member> findById(String id) {
        String jpql = "SELECT u FROM Member u WHERE u.id = :id";
        return findSingleResult(jpql, "id", id);
    }

    public Optional<Member> findByEmail(String email) {
        String jpql = "SELECT u FROM Member u WHERE u.email = :email";
        return findSingleResult(jpql, "email", email);
    }

    public Optional<Member> findByName(String name) {
        String jpql = "SELECT u FROM Member u WHERE u.name = :name";
        return findSingleResult(jpql, "name", name);
    }


    public Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        String jpql = "SELECT u FROM Member u WHERE u.socialType = :socialType AND u.socialId = :socialId";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class)
                                     .setParameter("socialType", socialType)
                                     .setParameter("socialId", socialId);
        List<Member> resultList = query.getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }
}
