package com.bizo.detonator.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import com.bizo.detonator.properties.Prop;
import com.bizo.detonator.properties.TypeOracle;

public class DtoConfig {

  private final TypeOracle oracle;
  private final RootConfig root;
  private final String simpleName;
  private final Map<String, Object> map;

  public DtoConfig(final TypeOracle oracle, final RootConfig root, final String simpleName, final Object map) {
    this.oracle = oracle;
    this.root = root;
    this.simpleName = simpleName;
    this.map = YamlUtils.ensureMap(map);
  }

  public String getFullName() {
    return root.getDtoPackage() + "." + getSimpleName();
  }

  public List<DtoProperty> getProperties() {
    final List<DtoProperty> dps = newArrayList();
    for (final Prop p : oracle.getProperties(getFullDomainName())) {
      dps.add(new DtoProperty(oracle, root, p));
    }
    return dps;
  }

  public String getSimpleName() {
    return simpleName;
  }

  public String getFullDomainName() {
    return root.getDomainPackage() + "." + map.get("domain");
  }

  public boolean isEnum() {
    return oracle.isEnum(getFullDomainName());
  }

  public List<String> getEnumValues() {
    return oracle.getEnumValues(getFullDomainName());
  }

}
