package com.gametransaction.controller;

import com.gametransaction.entity.AccountTran;
import com.gametransaction.model.SearchCriteria;
import com.gametransaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
    private static final int MAX_PAGE_SIZE = 500;

    @Autowired
    private TransactionService transactionService;

    /**
     * Display search form
     */
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        LOGGER.debug("Displaying search form");
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "search-form";
    }

    /**
     * Generate transactions report
     */
    @GetMapping("/report")
    public String generateReport(
            @RequestParam(value = "startDateTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDateTime,
            @RequestParam(value = "endDateTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDateTime,
            @RequestParam(value = "accountId", required = false) Integer accountId,
            @RequestParam(value = "platformTranId", required = false) String platformTranId,
            @RequestParam(value = "gameTranId", required = false) String gameTranId,
            @RequestParam(value = "gameId", required = false) String gameId,
            @RequestParam(value = "tranType", required = false) String tranType,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize,
            Model model) {

        LOGGER.debug("Generating report with criteria - start: {}, end: {}, account: {}", 
                     startDateTime, endDateTime, accountId);

        try {
            pageSize = Math.min(pageSize, MAX_PAGE_SIZE);  // Max 500 per page
            pageSize = Math.max(pageSize, 1);               // Min 1 per page
            pageNumber = Math.max(pageNumber, 0);           // Min page 0

            // Build search criteria
            SearchCriteria criteria = new SearchCriteria.Builder()
                    .startDateTime(startDateTime)
                    .endDateTime(endDateTime)
                    .accountId(accountId)
                    .platformTranId(isNotEmpty(platformTranId) ? platformTranId : null)
                    .gameTranId(isNotEmpty(gameTranId) ? gameTranId : null)
                    .gameId(isNotEmpty(gameId) ? gameId : null)
                    .tranType(isNotEmpty(tranType) ? tranType : null)
                    .sortBy(sortBy)
                    .sortOrder(sortOrder)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .build();

            // Validate criteria
            if (!validateCriteria(criteria)) {
                LOGGER.warn("Invalid search criteria provided");
                model.addAttribute("errorMessage", "Start date must be before or equal to end date");
                model.addAttribute("searchCriteria", criteria);
                return "search-form";
            }

            Page<AccountTran> result = transactionService.searchTransactions(criteria);

            LOGGER.info("Report generated: found {} transactions, page {} of {}", 
                       result.getTotalElements(), pageNumber + 1, result.getTotalPages());

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

            model.addAttribute("startDateFormatted",
                    criteria.getStartDateTime() != null
                            ? criteria.getStartDateTime().format(fmt)
                            : "");

            model.addAttribute("endDateFormatted",
                    criteria.getEndDateTime() != null
                            ? criteria.getEndDateTime().format(fmt)
                            : "");

            model.addAttribute("searchCriteria", criteria);
            model.addAttribute("result", result);
            model.addAttribute("transactions", result.getContent());
            model.addAttribute("totalElements", result.getTotalElements());
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("totalPages", result.getTotalPages());
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("sortByField", sortBy);
            model.addAttribute("currentSortOrder", sortOrder);
            model.addAttribute("isFirst", result.isFirst());
            model.addAttribute("isLast", result.isLast());
            model.addAttribute("hasNext", result.hasNext());
            model.addAttribute("hasPrevious", result.hasPrevious());

            return "report";
        } catch (Exception e) {
            LOGGER.error("Error generating report", e);
            model.addAttribute("errorMessage", "An error occurred while generating the report: " + e.getMessage());
            model.addAttribute("searchCriteria", new SearchCriteria());
            return "search-form";
        }
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty() && !value.trim().isEmpty();
    }

    /**
     * Validate search criteria
     * Ensures date range is valid
     */
    public boolean validateCriteria(SearchCriteria criteria) {
        if (criteria.getStartDateTime() == null || criteria.getEndDateTime() == null) {
            LOGGER.warn("Invalid criteria: missing date range");
            return false;
        }

        boolean valid = !criteria.getStartDateTime().isAfter(criteria.getEndDateTime());
        if (!valid) {
            LOGGER.warn("Invalid criteria: start date after end date");
        }
        return valid;
    }

    /**
     * Redirect to initial search form
     */
    @GetMapping("/")
    public String index(Model model) {
        LOGGER.debug("Redirecting to search form");
        return "redirect:/transaction/search";
    }
}
