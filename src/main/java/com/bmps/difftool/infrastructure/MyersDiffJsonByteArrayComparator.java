package com.bmps.difftool.infrastructure;

import com.bmps.difftool.rest.DiffOperationResponse;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;

import java.util.List;
import java.util.Objects;

public class MyersDiffJsonByteArrayComparator implements JsonByteArrayComparator {

    @Override
    public DiffOperationResponse compare(byte[] left, byte[] right) throws DiffException {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);

        Base64Converter base64Converter = new Base64Converter();

        List<String> jsonSourceLines = base64Converter.decodeToArrayList(left);
        List<String> jsonTargetLines = base64Converter.decodeToArrayList(right);

        //generating diff information.
        Patch<String> diff = DiffUtils.diff(jsonSourceLines, jsonTargetLines);

        //generating unified diff format
        List<String> unifiedDiffRepresentation = UnifiedDiffUtils.generateUnifiedDiff(
                "json-base64-left.json", "json-base64-right.json", jsonSourceLines, diff, 0);

        if (unifiedDiffRepresentation.size() == 0) {
            return new DiffOperationResponse().matched(true);
        }

        return new DiffOperationResponse()
                .data(String.join("\n", unifiedDiffRepresentation))
                .matched(false);
    }
}
