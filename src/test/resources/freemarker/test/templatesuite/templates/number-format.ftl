<#--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->
<#setting number_format = ",##0.##">
<#setting locale = "fr_FR">
${1}
${1?c}
${1234567.886}
${1234567.886?c}
<#setting number_format = "0.00">
${1}
${1?c}
${1234567.886}
${1234567.886?c}
${int?c}
<#setting number_format = "computer">
${100000.5}
<#setting number_format = "c">
${100000.5}
<#setting number_format = ",000.##">
${100000.5}
<@assertEquals expected="1" actual=double?c />
<@assertEquals expected="1.000000000000001" actual=double2?c />
<@assertEquals expected=(iciIntValue gte 2003032)?then("1E-16", "0.0000000000000001") actual=double3?c />
<@assertEquals expected=(iciIntValue gte 2003032)?then("-1E-16", "-0.0000000000000001") actual=double4?c />
<@assertEquals expected="1" actual=bigDecimal?c />
<@assertEquals expected=(iciIntValue gte 2003032)?then("1E-16", "0.0000000000000001") actual=bigDecimal2?c />
<#if iciIntValue gte 2003021>
  <@assertEquals expected="INF" actual="INF"?number?c />
  <@assertEquals expected="INF" actual="INF"?number?c />
  <@assertEquals expected="-INF" actual="-INF"?number?c />
  <@assertEquals expected="-INF" actual="-INF"?number?float?c />
  <@assertEquals expected="NaN" actual="NaN"?number?float?c />
  <@assertEquals expected="NaN" actual="NaN"?number?float?c />
<#else>
  <#setting locale = "en_US">
  <#setting number_format = "0.#">
  <@assertEquals expected="INF"?number?string actual="INF"?number?c />
  <@assertEquals expected="-INF"?number?string actual="-INF"?number?c />
  <@assertEquals expected="NaN"?number?string actual="NaN"?number?c />
</#if>