/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package freemarker.core;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Used internally only, might change without notice!
 * Pre-Java-16 implementation of {@link _Java16}.
 */
// We also have a Java 16 versions of this class in freemarker-core16, and we put all versions into
// the jar artifact via "JEP 238: Multi-Release JAR Files".
public final class _Java16Impl implements _Java16 {
    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public boolean isRecord(Class<?> cl) {
        throw newNoRecordSupportException();
    }

    @Override
    public Set<Method> getComponentAccessors(Class<?> recordClass){
        throw newNoRecordSupportException();
    }

    private UnsupportedOperationException newNoRecordSupportException() {
        return new UnsupportedOperationException("Record support needs at least Java 16");
    }
}
