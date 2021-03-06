/*
 * Copyright (C) 2016 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.collect.mock;

import com.google.common.io.Closeables;
import org.calrissian.mango.collect.CloseableIterable;

import java.io.IOException;
import java.util.Iterator;

public class MockIterable<T> implements CloseableIterable<T> {
    public Iterable<T> internal;
    private boolean closed = false;

    public MockIterable(Iterable<T> internal) {
        this.internal = internal;
    }

    @Override
    public void closeQuietly() {
        try {
            Closeables.close(this, true);
        } catch (IOException e) {
            // IOException should not have been thrown
        }
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

    public boolean isClosed() {
        return closed;
    }
}
