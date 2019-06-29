package com.bmps.difftool.infrastructure;

import com.bmps.difftool.domain.ComparatorType;
import org.junit.Assert;
import org.junit.Test;

class JsonByteArrayComparatorFactoryTest {

    @Test
    void createJsonPatchComparatorFactoryTest() {
        JsonByteArrayComparator comparatorFactory = JsonByteArrayComparatorFactory.createComparatorFactory(ComparatorType.JSON_PATCH);
        Assert.assertEquals(JsonPatchByteArrayComparator.class.getSimpleName(), comparatorFactory.getClass().getSimpleName());
    }

    @Test
    void createMyerComparatorFactoryTest() {
        JsonByteArrayComparator comparatorFactory = JsonByteArrayComparatorFactory.createComparatorFactory(ComparatorType.MYER);
        Assert.assertEquals(MyersDiffJsonByteArrayComparator.class.getSimpleName(), comparatorFactory.getClass().getSimpleName());
    }

    @Test(expected = RuntimeException.class)
    void createNullComparatorShouldThrowExceptionTest() {
        JsonByteArrayComparatorFactory.createComparatorFactory(null);
    }
}
