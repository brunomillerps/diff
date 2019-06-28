package com.bmps.difftool.infrastructure;

import com.bmps.difftool.rest.DiffOperationResponse;
import com.github.difflib.algorithm.DiffException;

import java.io.IOException;

public interface JsonByteArrayComparator {

    DiffOperationResponse compare(byte[] left, byte[] right) throws IOException, DiffException;
}
