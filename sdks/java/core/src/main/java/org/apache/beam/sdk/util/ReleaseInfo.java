/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.util;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties pertaining to this release of Apache Beam.
 *
 * <p>Properties will always include a name and version.
 */
@AutoValue
public abstract class ReleaseInfo implements Serializable {
  private static final String PROPERTIES_PATH = "/org/apache/beam/sdk/sdk.properties";

  /**
   * Returns an instance of {@link ReleaseInfo}.
   */
  public static ReleaseInfo getReleaseInfo() {
    return LazyInit.INSTANCE;
  }

  /**
   * Returns an immutable map of all properties pertaining to this release.
   */
  public abstract Map<String, String> getProperties();

  /** Provides the SDK name. */
  public String getName() {
    return getProperties().get("name");
  }

  /** Provides the SDK version. */
  public String getVersion() {
    return getProperties().get("version");
  }

  /////////////////////////////////////////////////////////////////////////
  private static final Logger LOG = LoggerFactory.getLogger(ReleaseInfo.class);
  private static final String DEFAULT_NAME = "Apache Beam SDK for Java";
  private static final String DEFAULT_VERSION = "Unknown";

  private static class LazyInit {
    private static final ReleaseInfo INSTANCE;
    static {
      Properties properties = new Properties();
      try (InputStream in = ReleaseInfo.class.getResourceAsStream(PROPERTIES_PATH)) {
        if (in == null) {
          LOG.warn("Beam properties resource not found: {}", PROPERTIES_PATH);
        } else {
          properties.load(in);
        }
      } catch (IOException e) {
        LOG.warn("Error loading Beam properties resource: ", e);
      }
      if (!properties.containsKey("name")) {
        properties.setProperty("name", DEFAULT_NAME);
      }
      if (!properties.containsKey("version")) {
        properties.setProperty("version", DEFAULT_VERSION);
      }
      INSTANCE = new AutoValue_ReleaseInfo(ImmutableMap.copyOf((Map) properties));
    }
  }
}
