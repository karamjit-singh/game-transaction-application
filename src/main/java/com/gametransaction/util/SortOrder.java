package com.gametransaction.util;

import java.util.Arrays;

/**
 * Sort Order Enumeration
 * ASC and DESC
 */
public enum SortOrder {
    ASC("ASC", "Ascending"),
    DESC("DESC", "Descending");

    private final String value;
    private final String displayName;

    SortOrder(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public static SortOrder fromString(String value) {
        if (value == null || value.isEmpty()) {
            return ASC;
        }
        return Arrays.stream(SortOrder.values())
                .filter(order -> order.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(ASC);
    }
}
