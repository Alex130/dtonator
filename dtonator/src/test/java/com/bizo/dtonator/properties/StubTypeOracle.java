package com.bizo.dtonator.properties;

import static joist.util.Copy.list;
import static org.apache.commons.lang.StringUtils.capitalize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class StubTypeOracle implements TypeOracle {

  private final Map<String, List<Prop>> properties = new HashMap<String, List<Prop>>();
  private final Map<String, List<String>> enumValues = new HashMap<String, List<String>>();
  private final Set<String> abstractDomains = new HashSet<String>();
  private final Map<String, MultiValuedMap<String, GenericPartsDto>> classTypes = new HashMap<String, MultiValuedMap<String, GenericPartsDto>>();

  @Override
  public List<Prop> getProperties(final String className, boolean excludeInherited) {
    return getProperties(className, excludeInherited, null);
  }

  @Override
  public List<Prop> getProperties(String className, boolean excludeInherited, List<String> excludedAnnotations) {
    final List<Prop> properties = this.properties.get(className);
    if (properties == null) {
      return list();
    }
    return properties;
  }

  @Override
  public boolean isEnum(final String className) {
    return enumValues.containsKey(className);
  }

  @Override
  public boolean isAbstract(String className) {
    return abstractDomains.contains(className);
  }

  @Override
  public List<String> getEnumValues(final String className) {
    return enumValues.get(className);
  }

  public void addProperty(final String className, final String name, final String type) {
    addProperty(className, name, type, null);
  }

  public void addProperty(final String className, final String name, final String type, final Map<String, String> generics) {
    addProperty(className, name, type, false, false, generics);
  }

  public void addProperty(final String className, final String name, final String type, final boolean inherited, final boolean isAbstract) {
    addProperty(className, name, type, inherited, isAbstract, null);
  }

  public void addProperty(
    final String className,
    final String name,
    final String type,
    final boolean inherited,
    final boolean isAbstract,
    final Map<String, String> generics) {
    List<Prop> properties = this.properties.get(className);
    if (properties == null) {
      properties = list();
      this.properties.put(className, properties);
    }
    properties.add(new Prop(name, type, false, "get" + capitalize(name), "set" + capitalize(name), inherited, isAbstract, generics));
  }

  public void setProperties(final String className, final List<Prop> properties) {
    this.properties.put(className, properties);
  }

  public void setEnumValues(final String className, final List<String> enumValues) {
    this.enumValues.put(className, enumValues);
  }

  public void addAbstractDomain(String className) {
    this.abstractDomains.add(className);
  }

  public void addClassType(String className, String typeVar, String operator, String boundClass) {
    MultiValuedMap<String, GenericPartsDto> results;
    results = classTypes.get(className);
    if (results == null) {
      results = new ArrayListValuedHashMap<>();
      classTypes.put(className, results);
    }

    GenericPartsDto gp = new GenericPartsDto(typeVar, operator, boundClass);
    results.put(className, gp);

  }

  @Override
  public MultiValuedMap<String, GenericPartsDto> getClassTypes(String className) {

    MultiValuedMap<String, GenericPartsDto> results;
    results = classTypes.get(className);
    if (results == null) {
      return new ArrayListValuedHashMap<>();
    } else {
      return results;
    }
  }

}
