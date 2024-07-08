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

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.test.TemplateTest;

public class ClassicCompatibleBugsTest extends TemplateTest {
    @Override
    protected Configuration createConfiguration() {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_33);
        conf.setClassicCompatible(true);
        conf.setBooleanFormat("c");
        return conf;
    }

    /**
     * FREEMARKER-227
     */
    @Test
    public void testLenientValueComparisonInSeqBuiltIns() throws TemplateException, IOException {
        addToDataModel("seq", List.of(1, "2", 3.0));

        assertOutput("${seq?seq_contains(1)?string}", "true");
        assertOutput("${seq?seq_contains('1')?string}", "true");

        assertOutput("${seq?seq_contains(2)?string}", "true");
        assertOutput("${seq?seq_contains('2')?string}", "true");

        assertOutput("${seq?seq_contains(3)?string}", "true");
        assertOutput("${seq?seq_contains('3')?string}", "true");

        assertOutput("${seq?seq_contains(4)?string}", "false");
        assertOutput("${seq?seq_contains('4')?string}", "false");
    }
}
