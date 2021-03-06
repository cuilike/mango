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
package org.calrissian.mango.types.encoders.lexi;


import org.calrissian.mango.types.encoders.AbstractBooleanEncoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class BooleanReverseEncoder extends AbstractBooleanEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static final BooleanEncoder booleanEncoder = new BooleanEncoder();

    @Override
    public String encode(Boolean value) {
        checkNotNull(value, "Null values are not allowed");
        return booleanEncoder.encode(!value);
    }

    @Override
    public Boolean decode(String value) {
        return !booleanEncoder.decode(value);
    }
}
