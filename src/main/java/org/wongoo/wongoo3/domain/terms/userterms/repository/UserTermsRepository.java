package org.wongoo.wongoo3.domain.terms.userterms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.terms.userterms.UserTerms;
import org.wongoo.wongoo3.domain.user.User;

import java.util.List;

public interface UserTermsRepository extends JpaRepository<UserTerms, Long> {
    List<UserTerms> findByUser(User user);
}
