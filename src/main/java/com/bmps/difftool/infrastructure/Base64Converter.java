package com.bmps.difftool.infrastructure;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * Converts Base64 Encoded data using {@link Base64}
 */
public class Base64Converter {

    private static final String WELL_FORMATTED_JSON_LINE_SEPARATOR = "\n";

    public String decodeToString(byte[] encoded) {
        Objects.requireNonNull(encoded, "JSON Byte array not supplied");
        return new String(Base64.getMimeDecoder().decode(encoded));
    }

    public List<String> decodeToArrayList(byte[] encoded) {
        String decodedToString = decodeToString(encoded);
        return Arrays.asList(decodedToString.split(WELL_FORMATTED_JSON_LINE_SEPARATOR));
    }
}
