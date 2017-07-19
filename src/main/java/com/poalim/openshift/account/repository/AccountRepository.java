package com.poalim.openshift.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.poalim.openshift.account.domain.Account;

/**
 * Created by osher on 19/7/17.
 */

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Optional<Account> findById(Integer id);
    List<Account> findByName(String name);
}

