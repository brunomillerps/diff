package com.bmps.difftool.infrastructure;

import com.bmps.difftool.DiffProvider;
import com.bmps.difftool.rest.DiffOperationResponse;
import com.github.difflib.algorithm.DiffException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Base64;

class MyersDiffJsonByteArrayComparatorTest {

    MyersDiffJsonByteArrayComparator myersDiffComparator = new MyersDiffJsonByteArrayComparator();

    @Test
    public void compareDifferentByteArrayTest() throws DiffException {
        //GIVEN
        String leftBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes());
        String rightBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_RIGHT_STRING.getBytes());

        //WHEN
        DiffOperationResponse diff = myersDiffComparator.compare(leftBytes.getBytes(), rightBytes.getBytes());

        //THEN
        Assert.assertFalse("Data should be different", diff.isMatched());
        Assert.assertEquals(
                "--- json-base64-left.json\n" +
                        "+++ json-base64-right.json\n" +
                        "@@ -3,1 +3,1 @@\n" +
                        "-        \"description\": \"An runtime exception\",\n" +
                        "+        \"description\": \"A runtime exception\",", diff.getData());
    }

    @Test
    public void compareEqualByteArrayTest() throws DiffException {
        //GIVEN
        String leftBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes());
        String rightBytes = Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes());

        //WHEN
        DiffOperationResponse diff = myersDiffComparator.compare(leftBytes.getBytes(), rightBytes.getBytes());

        //THEN
        Assert.assertTrue("Data should be similar", diff.isMatched());
        Assert.assertNull(diff.getData());
    }

    @Test(expected = NullPointerException.class)
    public void compareNullParametersShouldThrowException() throws DiffException {
        //WHEN
        myersDiffComparator.compare(null, null);
    }
}
