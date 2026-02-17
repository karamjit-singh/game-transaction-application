package com.gametransaction.service;

import com.gametransaction.entity.AccountTran;
import com.gametransaction.model.SearchCriteria;
import com.gametransaction.repository.AccountTranRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountTranRepository repository;

    private SearchCriteria criteria;

    @BeforeEach
    void setup() {
        criteria = new SearchCriteria();
        criteria.setPageNumber(0);
        criteria.setPageSize(10);
        criteria.setStartDateTime(LocalDateTime.now().minusDays(1));
        criteria.setEndDateTime(LocalDateTime.now());
        criteria.setAccountId(456565656);
        criteria.setPlatformTranId("PT1");
        criteria.setGameTranId("GT1");
        criteria.setGameId("GAME-1");
        criteria.setTranType("GAME_BET");
    }

    @Test
    void searchTransactions_shouldUseDefaultSort_whenSortNotProvided() {
        criteria.setSortBy(null);
        criteria.setSortOrder(null);

        Page<AccountTran> page = new PageImpl<>(Collections.singletonList(new AccountTran()));

        when(repository.findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        Page<AccountTran> result = transactionService.searchTransactions(criteria);

        assertThat(result.getContent()).hasSize(1);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), pageableCaptor.capture()
        );

        Pageable pageable = pageableCaptor.getValue();
        Sort.Order order = pageable.getSort().iterator().next();

        assertThat(order.getProperty()).isEqualTo("id");
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void searchTransactions_shouldSortByAccountId_ASC() {
        criteria.setSortBy("accountId");
        criteria.setSortOrder("ASC");

        Page<AccountTran> page = new PageImpl<>(Collections.emptyList());

        when(repository.findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        transactionService.searchTransactions(criteria);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), pageableCaptor.capture()
        );

        Pageable pageable = pageableCaptor.getValue();
        Sort.Order order = pageable.getSort().iterator().next();

        assertThat(order.getProperty()).isEqualTo("accountId");
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void searchTransactions_shouldSortByDateTime_DESC() {
        criteria.setSortBy("dateTime");
        criteria.setSortOrder("DESC");

        when(repository.findWithAdvancedFilters(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        transactionService.searchTransactions(criteria);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), captor.capture()
        );

        Sort.Order order = captor.getValue().getSort().iterator().next();
        assertThat(order.getProperty()).isEqualTo("dateTime");
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void searchTransactions_shouldMapAmountToAmountReal() {
        criteria.setSortBy("amount");
        criteria.setSortOrder("ASC");

        when(repository.findWithAdvancedFilters(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        transactionService.searchTransactions(criteria);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), captor.capture()
        );

        Sort.Order order = captor.getValue().getSort().iterator().next();
        assertThat(order.getProperty()).isEqualTo("amountReal");
    }

    @Test
    void searchTransactions_shouldMapBalanceToBalanceReal() {
        criteria.setSortBy("balance");
        criteria.setSortOrder("DESC");

        when(repository.findWithAdvancedFilters(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        transactionService.searchTransactions(criteria);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), captor.capture()
        );

        Sort.Order order = captor.getValue().getSort().iterator().next();
        assertThat(order.getProperty()).isEqualTo("balanceReal");
    }

    @Test
    void searchTransactions_shouldFallbackToId_forUnknownSortField() {
        criteria.setSortBy("unknownField");
        criteria.setSortOrder("ASC");

        when(repository.findWithAdvancedFilters(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        transactionService.searchTransactions(criteria);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), captor.capture()
        );

        Sort.Order order = captor.getValue().getSort().iterator().next();
        assertThat(order.getProperty()).isEqualTo("id");
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void searchTransactions_shouldPassAllFiltersToRepository() {
        when(repository.findWithAdvancedFilters(
                any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(Page.empty());

        transactionService.searchTransactions(criteria);

        verify(repository).findWithAdvancedFilters(
                eq(criteria.getStartDateTime()),
                eq(criteria.getEndDateTime()),
                eq(criteria.getAccountId()),
                eq(criteria.getPlatformTranId()),
                eq(criteria.getGameTranId()),
                eq(criteria.getGameId()),
                eq(criteria.getTranType()),
                any(Pageable.class)
        );
    }
}
