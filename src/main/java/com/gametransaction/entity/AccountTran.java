package com.gametransaction.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * AccountTran Entity
 * Maps to account_tran table
 */
@Entity
@Table(name = "account_tran", indexes = {
    @Index(name = "idx_account_datetime", columnList = "ACCOUNT_ID,DATETIME"),
    @Index(name = "idx_datetime", columnList = "DATETIME"),
    @Index(name = "idx_tran_type", columnList = "TRAN_TYPE"),
    @Index(name = "idx_game_id", columnList = "GAME_ID")
})
public class AccountTran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private Integer accountId;

    @Column(name = "DATETIME", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "TRAN_TYPE", nullable = false)
    private String tranType;

    @Column(name = "PLATFORM_TRAN_ID")
    private String platformTranId;

    @Column(name = "GAME_TRAN_ID")
    private String gameTranId;

    @Column(name = "GAME_ID")
    private String gameId;

    @Column(name = "AMOUNT_REAL")
    private BigDecimal amountReal;

    @Column(name = "BALANCE_REAL")
    private BigDecimal balanceReal;

    @Column(name = "PLATFORM_ID")
    private Integer platformId;

    @Column(name = "AMOUNT_RELEASED_BONUS")
    private BigDecimal amountReleasedBonus = BigDecimal.ZERO;

    @Column(name = "AMOUNT_PLAYABLE_BONUS")
    private BigDecimal amountPlayableBonus = BigDecimal.ZERO;

    @Column(name = "BALANCE_RELEASED_BONUS")
    private BigDecimal balanceReleasedBonus = BigDecimal.ZERO;

    @Column(name = "BALANCE_PLAYABLE_BONUS")
    private BigDecimal balancePlayableBonus = BigDecimal.ZERO;

    @Column(name = "AMOUNT_UNDERFLOW")
    private BigDecimal amountUnderflow = BigDecimal.ZERO;

    @Column(name = "AMOUNT_FREE_BET")
    private BigDecimal amountFreeBet = BigDecimal.ZERO;

    public AccountTran() {
    }

    public AccountTran(Integer accountId, LocalDateTime dateTime, String tranType) {
        this.accountId = accountId;
        this.dateTime = dateTime;
        this.tranType = tranType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getTranType() { return tranType; }
    public void setTranType(String tranType) { this.tranType = tranType; }
    public String getPlatformTranId() { return platformTranId; }
    public void setPlatformTranId(String platformTranId) { this.platformTranId = platformTranId; }
    public String getGameTranId() { return gameTranId; }
    public void setGameTranId(String gameTranId) { this.gameTranId = gameTranId; }
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public BigDecimal getAmountReal() { return amountReal; }
    public void setAmountReal(BigDecimal amountReal) { this.amountReal = amountReal; }
    public BigDecimal getBalanceReal() { return balanceReal; }
    public void setBalanceReal(BigDecimal balanceReal) { this.balanceReal = balanceReal; }
    public Integer getPlatformId() { return platformId; }
    public void setPlatformId(Integer platformId) { this.platformId = platformId; }
    public BigDecimal getAmountReleasedBonus() { return amountReleasedBonus; }
    public void setAmountReleasedBonus(BigDecimal amountReleasedBonus) { this.amountReleasedBonus = amountReleasedBonus; }
    public BigDecimal getAmountPlayableBonus() { return amountPlayableBonus; }
    public void setAmountPlayableBonus(BigDecimal amountPlayableBonus) { this.amountPlayableBonus = amountPlayableBonus; }
    public BigDecimal getBalanceReleasedBonus() { return balanceReleasedBonus; }
    public void setBalanceReleasedBonus(BigDecimal balanceReleasedBonus) { this.balanceReleasedBonus = balanceReleasedBonus; }
    public BigDecimal getBalancePlayableBonus() { return balancePlayableBonus; }
    public void setBalancePlayableBonus(BigDecimal balancePlayableBonus) { this.balancePlayableBonus = balancePlayableBonus; }
    public BigDecimal getAmountUnderflow() { return amountUnderflow; }
    public void setAmountUnderflow(BigDecimal amountUnderflow) { this.amountUnderflow = amountUnderflow; }
    public BigDecimal getAmountFreeBet() { return amountFreeBet; }
    public void setAmountFreeBet(BigDecimal amountFreeBet) { this.amountFreeBet = amountFreeBet; }

    public BigDecimal getTotalAmount() {
        return BigDecimal.ZERO
                .add(amountReal != null ? amountReal : BigDecimal.ZERO)
                .add(amountReleasedBonus != null ? amountReleasedBonus : BigDecimal.ZERO)
                .add(amountPlayableBonus != null ? amountPlayableBonus : BigDecimal.ZERO)
                .add(amountUnderflow != null ? amountUnderflow : BigDecimal.ZERO)
                .add(amountFreeBet != null ? amountFreeBet : BigDecimal.ZERO);
    }

    public BigDecimal getTotalBalance() {
        return BigDecimal.ZERO
                .add(balanceReal != null ? balanceReal : BigDecimal.ZERO)
                .add(balanceReleasedBonus != null ? balanceReleasedBonus : BigDecimal.ZERO)
                .add(balancePlayableBonus != null ? balancePlayableBonus : BigDecimal.ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountTran that = (AccountTran) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AccountTran{id=" + id + ", accountId=" + accountId + ", tranType='" + tranType + "'}";
    }
}
