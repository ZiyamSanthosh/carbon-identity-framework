/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.action.mgt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthType.
 */
public class AuthType {

    /**
     * Action Type Enum.
     */
    public enum TypeEnum {

        NONE("NONE"),
        BEARER("BEARER"),
        API_KEY("API_KEY"),
        BASIC("BASIC");

        private final String value;

        TypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static TypeEnum fromValue(String value) {
            for (TypeEnum b : TypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private TypeEnum type;
    private Map<String, String> properties = null;

    public AuthType() {
    }

    public AuthType(AuthTypeBuilder authTypeBuilder) {

        this.type = authTypeBuilder.type;
        this.properties = authTypeBuilder.properties;
    }

    public TypeEnum getType() {

        return type;
    }

    public void setType(TypeEnum type) {

        this.type = type;
    }

    public Map<String, String> getProperties() {

        return properties;
    }

    public void setProperties(Map<String, String> properties) {

        this.properties = properties;
    }

    /**
     * AuthType builder.
     */
    public static class AuthTypeBuilder {

        private TypeEnum type;
        private Map<String, String> properties = null;

        public AuthTypeBuilder() {
        }

        public AuthTypeBuilder type(TypeEnum type) {

            this.type = type;
            return this;
        }

        public AuthTypeBuilder properties(Map<String, String> properties) {

            this.properties = properties;
            return this;
        }

        public AuthTypeBuilder addProperty(String key, String value) {

            if (this.properties == null) {
                this.properties = new HashMap<>();
            }
            this.properties.put(key, value);
            return this;
        }

        public AuthType build() {

            return new AuthType(this);
        }
    }
}
