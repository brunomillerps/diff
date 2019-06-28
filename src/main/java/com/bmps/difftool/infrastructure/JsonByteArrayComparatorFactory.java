package com.bmps.difftool.infrastructure;

import com.bmps.difftool.domain.ComparatorType;

import java.util.Objects;

/**
 * Provide the implementation of the diff
 *
 * @author Bruno Miller
 */
public final class JsonByteArrayComparatorFactory {

    public static JsonByteArrayComparator createComparatorFactory(ComparatorType comparatorType) {
        Objects.requireNonNull(comparatorType, "A ComparatorType is required.");

        switch (comparatorType) {
            case MYER:
                return new MyersDiffJsonByteArrayComparator();
            case JSON_PATCH:
                return new JsonPatchByteArrayComparator();
        }
        throw new IllegalArgumentException("Missing implementation for the comparator type " + comparatorType.name());
    }
}
