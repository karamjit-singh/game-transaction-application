package com.gametransaction.model;

import java.time.LocalDateTime;

/**
 * Search Criteria Data Model
 * Contains search, filter, sort, and pagination parameters
 */
public class SearchCriteria {
    
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer accountId;
    private String platformTranId;
    private String gameTranId;
    private String gameId;
    private String tranType;
    private String sortBy = "id";
    private String sortOrder = "DESC";
    private int pageNumber = 0;
    private int pageSize = 25;

    public SearchCriteria() {
    }

    public static class Builder {
        private final SearchCriteria criteria = new SearchCriteria();

        public Builder startDateTime(LocalDateTime startDateTime) {
            criteria.startDateTime = startDateTime;
            return this;
        }

        public Builder endDateTime(LocalDateTime endDateTime) {
            criteria.endDateTime = endDateTime;
            return this;
        }

        public Builder accountId(Integer accountId) {
            criteria.accountId = accountId;
            return this;
        }

        public Builder platformTranId(String platformTranId) {
            criteria.platformTranId = platformTranId;
            return this;
        }

        public Builder gameTranId(String gameTranId) {
            criteria.gameTranId = gameTranId;
            return this;
        }

        public Builder gameId(String gameId) {
            criteria.gameId = gameId;
            return this;
        }

        public Builder tranType(String tranType) {
            criteria.tranType = tranType;
            return this;
        }

        public Builder sortBy(String sortBy) {
            criteria.sortBy = sortBy;
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            criteria.sortOrder = sortOrder;
            return this;
        }

        public Builder pageNumber(int pageNumber) {
            criteria.pageNumber = pageNumber;
            return this;
        }

        public Builder pageSize(int pageSize) {
            criteria.pageSize = pageSize;
            return this;
        }

        public SearchCriteria build() {
            return criteria;
        }
    }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    public String getPlatformTranId() { return platformTranId; }
    public void setPlatformTranId(String platformTranId) { this.platformTranId = platformTranId; }
    public String getGameTranId() { return gameTranId; }
    public void setGameTranId(String gameTranId) { this.gameTranId = gameTranId; }
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public String getTranType() { return tranType; }
    public void setTranType(String tranType) { this.tranType = tranType; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public boolean hasDateCriteria() {
        return startDateTime != null && endDateTime != null;
    }

    public boolean hasFilters() {
        return accountId != null || platformTranId != null || gameTranId != null ||
               gameId != null || tranType != null;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
