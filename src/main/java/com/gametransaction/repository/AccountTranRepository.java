package com.gametransaction.repository;

import com.gametransaction.entity.AccountTran;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccountTranRepository extends JpaRepository<AccountTran, Long>, JpaSpecificationExecutor<AccountTran> {

    /**
     * Find transactions with advanced filtering and pagination
     *
     * @param startDate      Start date for range
     * @param endDate        End date for range
     * @param accountId      Account ID filter
     * @param platformTranId Platform transaction ID filter
     * @param gameTranId     Game transaction ID filter
     * @param gameId         Game ID filter
     * @param tranType       Transaction type filter
     * @param pageable       Pagination and sorting info
     * @return Page of transactions
     */
    @Query("SELECT a FROM AccountTran a WHERE " +
            "a.dateTime >= :startDate AND a.dateTime <= :endDate " +
            "AND (:accountId IS NULL OR a.accountId = :accountId) " +
            "AND (:platformTranId IS NULL OR a.platformTranId LIKE CONCAT('%', :platformTranId, '%')) " +
            "AND (:gameTranId IS NULL OR a.gameTranId LIKE CONCAT('%', :gameTranId, '%')) " +
            "AND (:gameId IS NULL OR a.gameId LIKE CONCAT('%', :gameId, '%')) " +
            "AND (:tranType IS NULL OR a.tranType = :tranType)")
    Page<AccountTran> findWithAdvancedFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("accountId") Integer accountId,
            @Param("platformTranId") String platformTranId,
            @Param("gameTranId") String gameTranId,
            @Param("gameId") String gameId,
            @Param("tranType") String tranType,
            Pageable pageable);
}
