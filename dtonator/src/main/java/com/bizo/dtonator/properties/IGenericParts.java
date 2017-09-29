package com.bizo.dtonator.properties;

import org.apache.commons.collections4.MultiValuedMap;

public interface IGenericParts {

  String getParamTypeString();

  String getTypeVarString();

  String getArrayTypeString();

  String getLinkedTypeString();

  String getBoundClassString();

  String getFormattedString();

  String getFormattedString(String paramString);

  String toString();

  <T extends IGenericParts> MultiValuedMap<String, T> getLinkedTypes();

  <T extends IGenericParts> MultiValuedMap<String, T> getParamTypeArgs();

  String getParamTypeArgsString();

}
