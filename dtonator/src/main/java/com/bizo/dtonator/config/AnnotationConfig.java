package com.bizo.dtonator.config;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.uncapitalize;

import java.util.Map;

public class AnnotationConfig {

  public final String name;
  public final String domainType;
  public final boolean exclude;

  public AnnotationConfig(final Map.Entry<Object, Object> e) {
    domainType = (String) e.getKey();
    exclude = (boolean) e.getValue();
    name = uncapitalize(substringAfterLast(domainType, "."));
  }

}
