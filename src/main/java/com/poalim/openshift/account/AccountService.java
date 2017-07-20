package com.poalim.openshift.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.poalim.openshift.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by osher on 19/7/17.
 */

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    public Account findAccountById(Integer accountId) {
        logger.debug("AccountService-findById: id={}", accountId);
        return this.accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException("Acoount not found:" + accountId));
    }

    public List<Account> findAccountsByName(String name) {
        logger.debug("AccountService-findAccountsByName: fullname={}", name);
        return this.accountRepository.findByFullName(name).orElse(new ArrayList<>());
    }

    public Integer createAccount(Account account) {
        logger.debug("AccountService-createAccount: account: {}", account.toString());
        Account newAccount = new Account();
        newAccount.setUserId(account.getUserId());
        this.updateAccountProperties(newAccount, account);
        return this.save(newAccount).getId();
    }

    public Integer updateAccount(Account account) {
        logger.debug("AccountService-updateAccount: account: {}", account.toString());
        Account toUpdate = this.findAccountById(account.getId());
        this.updateAccountProperties(toUpdate, account);
        return this.save(toUpdate).getId();
    }

    public Account deleteAccount(Account account) {
        logger.debug("AccountService-deleteAccount: account: {}", account.toString());
        Account toUpdate = this.findAccountById(account.getId());
        return this.delete(toUpdate);
    }

    // TODO: Add some checks, obviously permission check, maybe credit limit? no minus?
    @Transactional
    public void addFunds(Integer accountId, BigDecimal amount) {
        logger.debug("AccountService-addFunds: accountId: {}, amount: {}", accountId, amount);
        Account account = this.findAccountById(accountId);
        account.addFunds(amount);
        this.save(account);
    }

    // TODO: Add some checks, obviously permission check, maybe credit limit? no minus?
    @Transactional
    public void withdrawalFunds(Integer accountId, BigDecimal amount) {
        logger.debug("AccountService-withdrawalFunds: account: {}, amount: {}", accountId, amount);
        Account account = this.findAccountById(accountId);
        account.withdrawalFunds(amount);
        this.save(account);
    }

    private void updateAccountProperties(Account toUpdate, Account newData) {
        // Only update allowed properties
        toUpdate.setFullName(newData.getFullName());
        toUpdate.setEmail(newData.getEmail());
        toUpdate.setAddress(newData.getAddress());
    }

    @Transactional
    private Account save(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    private Account delete(Account account) {
        accountRepository.delete(account);
        return account;
    }
}
