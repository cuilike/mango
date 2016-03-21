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
package org.calrissian.mango.criteria.domain;

public class LessThanLeaf<T> extends TermValueLeaf<T> {

    public LessThanLeaf(String term, T value, ParentNode parent) {
        super(term, value, parent);
    }

    @Override
    public Node clone(ParentNode node) {
        return new LessThanLeaf<>(getTerm(), getValue(), node);
    }

    @Override
    public String toString() {
        return getTerm() + " < " + getValue();
    }
}
