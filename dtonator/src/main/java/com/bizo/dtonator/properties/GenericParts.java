package com.bizo.dtonator.properties;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GenericParts<T extends Object> {

  public ParameterizedType paramType;
  public TypeVariable typeVar;
  public WildcardType wildType;
  public GenericArrayType arrayType;
  public String operator;
  public List<T> linkedTypes;

  public GenericParts() {
    super();

  }

  public static GenericParts build() {
    return new GenericParts();
  }

  public String getParamTypeString() {
    String paramStr = "";
    if (paramType != null) {
      final Class<?> rawType = (Class<?>) paramType.getRawType();
      paramStr = rawType.getName();
    }

    return paramStr;
  }

  public String getTypeVarString() {
    String typeStr = "";
    if (typeVar != null) {
      typeStr = typeVar.getName();
    }
    return typeStr;
  }

  public String getArrayTypeString() {
    String typeStr = "";
    if (arrayType != null) {
      typeStr = arrayType.getTypeName();
    }
    return typeStr;
  }

  public String getLinkedTypeString() {
    String linkedStr = "";
    if (linkedTypes != null) {

      linkedStr = StringUtils.joinWith(",", linkedTypes);
    }
    return linkedStr;
  }

  @Override
  public String toString() {
    return "GenericParts [operator="
      + operator
      + ", getParamTypeString()="
      + getParamTypeString()
      + ", getTypeVarString()="
      + getTypeVarString()
      + ", getArrayTypeString()="
      + getArrayTypeString()
      + ", getLinkedTypeString()="
      + getLinkedTypeString()
      + "]";
  }

}
