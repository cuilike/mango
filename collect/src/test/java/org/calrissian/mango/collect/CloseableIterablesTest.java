package org.calrissian.mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 */
public class CloseableIterablesTest {

    @Test
    public void testTransform() throws IOException {
        MockIterable<Integer> closeableIterable = mockCloseableIterable();

        //add one
        CloseableIterable<Integer> addOne = CloseableIterables.transform(closeableIterable, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        });

        //multiply by ten
        CloseableIterable<Integer> multTen = CloseableIterables.transform(addOne, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 10;
            }
        });

        Iterables.elementsEqual(Lists.newArrayList(20, 30, 40, 50, 60), multTen);

        multTen.close();

        //make sure closed
        try {
            Iterator<Integer> iterator = multTen.iterator();
            fail();
        } catch (IllegalStateException ise) {
        }

    }

    @Test
    public void testLimit() throws IOException {
        MockIterable<Integer> closeableIterable = mockCloseableIterable();

        //add one
        CloseableIterable<Integer> firstThree = CloseableIterables.limit(closeableIterable, 3);
        assertEquals(3, Iterables.size(firstThree));
        firstThree.close();
    }

    @Test
    public void testFilter() throws IOException {
        MockIterable<Integer> closeableIterable = mockCloseableIterable();

        //filter odd
        CloseableIterable<Integer> odd = CloseableIterables.filter(closeableIterable, new Predicate<Integer>() {
            @Override
            public boolean apply(java.lang.Integer input) {
                return input % 2 == 0;
            }
        });

        Iterables.elementsEqual(Lists.newArrayList(2, 4), odd);

        odd.close();

        //make sure closed
        try {
            Iterator<Integer> iterator = odd.iterator();
            fail();
        } catch (IllegalStateException ise) {
        }

    }

    @Test
    public void testAutoClose() throws Exception {

        MockIterable<Integer> iterable = mockCloseableIterable();
        CloseableIterable<Integer> closeableIterable = CloseableIterables.autoClose(iterable);
        closeableIterable.close();
        assertTrue(iterable.isClosed());

        //if consumed close
        iterable = mockCloseableIterable();
        closeableIterable = CloseableIterables.autoClose(iterable);
        Iterator<Integer> iterator = closeableIterable.iterator();
        Iterators.size(iterator);
        assertTrue(iterable.isClosed());

        //if exception thrown
        iterable = mockExceptionThrowingCloseableIterable();
        closeableIterable = CloseableIterables.autoClose(iterable);
        iterator = closeableIterable.iterator();
        try {
            iterator.next();
            fail();
        } catch (RuntimeException re) {
        }
        assertTrue(iterable.isClosed());
    }

    @Test
    public void testDistinct() throws Exception {
        MockIterable<Integer> integers = new MockIterable<Integer>(Lists.newArrayList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7));
        CloseableIterable<Integer> distinct = CloseableIterables.distinct(integers);
        assertEquals(7, Iterables.size(distinct));
    }

    private MockIterable<Integer> mockCloseableIterable() {
        final ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        return new MockIterable<Integer>(list);
    }

    private MockIterable<Integer> mockExceptionThrowingCloseableIterable() {
        return new MockIterable<Integer>(mockCloseableIterable()) {

            @Override
            public Iterator<Integer> iterator() {
                return new AbstractIterator<Integer>() {

                    @Override
                    protected Integer computeNext() {
                        throw new RuntimeException("I throw because I want to");
                    }
                };
            }
        };
    }

    private static class MockIterable<T> implements CloseableIterable<T> {
        boolean closed = false;
        Iterable<T> internal;

        private MockIterable(Iterable<T> internal) {
            this.internal = internal;
        }

        @Override
        public void closeQuietly() {
            Closeables.closeQuietly(this);
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public Iterator<T> iterator() {
            if (closed) throw new IllegalStateException("Iterable is already closed");
            return internal.iterator();
        }

        private boolean isClosed() {
            return closed;
        }
    }
}
