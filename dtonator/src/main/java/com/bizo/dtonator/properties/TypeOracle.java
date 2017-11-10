package com.bizo.dtonator.properties;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;

/**
 * Abstracts finding metadata about classes.
 * 
 * The main implementation is {@link ReflectionTypeOracle} but tests can use stubs instead.
 */
public interface TypeOracle {

  List<Prop> getProperties(final String className, boolean excludeInherited);

  List<Prop> getProperties(final String className, boolean excludeInherited, final List<String> excludedAnnotations);

  boolean isEnum(final String className);

  boolean isAbstract(final String className);

  List<String> getEnumValues(final String className);

  MultiValuedMap<String, GenericPartsDto> getClassTypes(String className);

  String getClassTypesString(String className);

}
