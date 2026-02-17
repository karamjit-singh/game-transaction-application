package com.gametransaction.service;

import com.gametransaction.entity.AccountTran;
import com.gametransaction.model.SearchCriteria;
import com.gametransaction.repository.AccountTranRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private AccountTranRepository repository;

    /**
     * Search transactions with pagination
     * @param criteria Search criteria including pagination parameters
     * @return Page object with transactions for current page
     */
    public Page<AccountTran> searchTransactions(SearchCriteria criteria) {
        LOGGER.debug("Searching transactions with criteria: {}", criteria);

        Sort sort = buildSort(criteria);
        Pageable pageable = PageRequest.of(
                criteria.getPageNumber(),
                criteria.getPageSize(),
                sort
        );

        LOGGER.debug("Pagination: page={}, size={}, sort={}",
                     criteria.getPageNumber(), criteria.getPageSize(), sort);

        Page<AccountTran> result = repository.findWithAdvancedFilters(
                criteria.getStartDateTime(),
                criteria.getEndDateTime(),
                criteria.getAccountId(),
                criteria.getPlatformTranId(),
                criteria.getGameTranId(),
                criteria.getGameId(),
                criteria.getTranType(),
                pageable
        );

        LOGGER.info("Search completed: found {} total records, page {} of {}", 
                    result.getTotalElements(), 
                    criteria.getPageNumber() + 1, 
                    result.getTotalPages());

        return result;
    }

    /**
     * Build Sort object from search criteria
     * Handles sort field and direction
     *
     * @param criteria Search criteria
     * @return Sort object
     */
    private Sort buildSort(SearchCriteria criteria) {
        String sortBy = criteria.getSortBy() != null ? criteria.getSortBy() : "id";
        String sortOrder = criteria.getSortOrder() != null ? criteria.getSortOrder() : "DESC";

        String sortProperty;

        switch (sortBy) {
            case "accountId":
                sortProperty = "accountId";
                break;
            case "dateTime":
                sortProperty = "dateTime";
                break;
            case "tranType":
                sortProperty = "tranType";
                break;
            case "platformTranId":
                sortProperty = "platformTranId";
                break;
            case "gameTranId":
                sortProperty = "gameTranId";
                break;
            case "gameId":
                sortProperty = "gameId";
                break;
            case "amount":
                sortProperty = "amountReal";
                break;
            case "balance":
                sortProperty = "balanceReal";
                break;
            default:
                sortProperty = "id";
                break;
        }

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(sortOrder)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return Sort.by(direction, sortProperty);
    }
}
