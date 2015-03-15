/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.types.encoders.simple;

import org.calrissian.mango.domain.entity.EntityRelationship;
import org.calrissian.mango.types.encoders.AbstractEntityRelationshipEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class EntityRelationshipEncoder extends AbstractEntityRelationshipEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static final String SCHEME = "entity://";

    @Override
    public String encode(EntityRelationship value) throws TypeEncodingException {
        checkNotNull(value, "Null values are not allowed");
        return format("%s%s#%s", SCHEME, value.getType(), value.getId());
    }

    @Override
    public EntityRelationship decode(String value) throws TypeDecodingException {
        checkNotNull(value, "Null values are not allowed");
        checkArgument(value.startsWith(SCHEME) && value.contains("#"), "The value is not a valid encoding");

        String rel = value.substring(SCHEME.length(), value.length());
        int idx = rel.indexOf('#');

        return new EntityRelationship(rel.substring(0, idx), rel.substring(idx + 1));
    }
}
