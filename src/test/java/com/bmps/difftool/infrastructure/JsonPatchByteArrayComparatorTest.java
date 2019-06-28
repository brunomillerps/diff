package com.bmps.difftool.infrastructure;

import com.bmps.difftool.DiffProvider;
import com.bmps.difftool.rest.DiffOperationResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;

public class JsonPatchByteArrayComparatorTest {

    JsonPatchByteArrayComparator jsonPatchComparator = new JsonPatchByteArrayComparator();

    @Test
    public void compareStructuredDifferenceByteArrayTest() throws IOException {
        //GIVEN
        String leftBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes());
        String rightBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_RIGHT_STRING.getBytes());

        //WHEN
        DiffOperationResponse diff = jsonPatchComparator.compare(leftBytes.getBytes(), rightBytes.getBytes());

        //THEN
        Assert.assertFalse("Expect data to be different", diff.isMatched());
        Assert.assertEquals("[{\"op\":\"replace\",\"path\":\"/description\",\"value\":\"A runtime exception\"}]",
                diff.getData().toString());
    }


    /**
     * Unfortunately Jackson does not cover the case where the JSON are similar but some attributes in different order
     * <p> this test is just to show this behavior.</p>
     *
     * @throws IOException
     */
    @Test
    public void compareEqualJsonButElementNotOrderedTest() throws IOException {
        //GIVEN
        String leftBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING_2.getBytes());
        String rightBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_RIGHT_STRING.getBytes());

        //WHEN
        DiffOperationResponse diff = jsonPatchComparator.compare(leftBytes.getBytes(), rightBytes.getBytes());

        //THEN
        Assert.assertFalse("Expect data to be different", diff.isMatched());
        Assert.assertEquals("[{\"op\":\"replace\",\"path\":\"/description\",\"value\":\"A runtime exception\"}]",
                diff.getData().toString());
    }


    @Test
    public void compareEqualByteArrayTest() throws IOException {
        //GIVEN
        String leftBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes());
        String rightBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes());

        //WHEN
        DiffOperationResponse diff = jsonPatchComparator.compare(leftBytes.getBytes(), rightBytes.getBytes());

        //THEN
        Assert.assertTrue("Data should be similar", diff.isMatched());
        Assert.assertNull(diff.getData());
    }

    @Test
    public void compareNullParametersShouldThrowException() {
        //WHEN
        Assertions.assertThrows(NullPointerException.class, () -> jsonPatchComparator.compare(null, null));
    }

}
