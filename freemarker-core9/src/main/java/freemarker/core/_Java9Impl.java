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

import freemarker.ext.beans.BeansWrapper;

/**
 * Used internally only, might change without notice!
 * Java 9 implementation of {@link _Java9}.
 */
// We also have a pre-Java-9 versions of this class in freemarker-core, and we put all versions into
// the jar artifact via "JEP 238: Multi-Release JAR Files".
@SuppressWarnings("Since15") // For IntelliJ inspection
public class _Java9Impl implements _Java9 {
    private static final Module ACCESSOR_MODULE = BeansWrapper.class.getModule();

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean isAccessibleAccordingToModuleExports(Class<?> accessedClass) {
        Module accessedModule = accessedClass.getModule();
        Package accessedPackage = accessedClass.getPackage(); // null for arrays!
        if (accessedPackage == null) {
            return true;
        }

        return accessedModule.isExported(accessedPackage.getName(), ACCESSOR_MODULE);
    }
}
