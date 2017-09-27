package com.bizo.dtonator.properties;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;

public class GenericParts<T extends Object> {

  public ParameterizedType paramType;
  public TypeVariable typeVar;
  public WildcardType wildType;
  public GenericArrayType arrayType;
  public String operator;
  public Class boundClass;
  public MultiValuedMap<String, GenericParts> linkedTypes;

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
      typeStr = arrayType.getGenericComponentType().getTypeName();
    }
    return typeStr;
  }

  public String getLinkedTypeString() {
    String linkedStr = "";
    if (linkedTypes != null) {

      linkedStr = StringUtils.joinWith(",", linkedTypes.values());
    }
    return linkedStr;
  }

  public String getBoundClassString() {
    String boundString = "";
    if (boundClass != null) {
      boundString = boundClass.getName();
    }
    return boundString;
  }

  public String getFormattedString() {
    return getFormattedString(paramType != null ? getParamTypeString() : null);
  }

  @SuppressWarnings("rawtypes")
  public String getFormattedString(String paramString) {
    StringBuilder sb = new StringBuilder();
    if (paramString != null) {
      sb.append(paramString).append("<");
    }

    if (typeVar != null) {
      sb.append(getTypeVarString());
    }
    if (wildType != null) {
      sb.append("?");
    }
    if (arrayType != null) {
      sb.append(getArrayTypeString()).append("[]");
    }
    if (operator != null) {
      sb.append(" ").append(operator).append(" ");
    }
    if (boundClass != null) {
      sb.append(getBoundClassString());
    }

    if (linkedTypes != null) {
      for (String key : linkedTypes.keySet()) {
        List<GenericParts> parts = (List<GenericParts>) linkedTypes.get(key);
        if (parts != null) {
          for (int i = 0; i < parts.size(); i++) {
            if (i > 0) {
              sb.append(" & ");
            }
            sb.append(parts.get(i).getFormattedString());
          }
        }
      }
    }

    if (paramString != null) {
      sb.append(">");
    }
    return sb.toString();
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
      + ", getBoundClassString()="
      + getBoundClassString()
      + "]";
  }

}
