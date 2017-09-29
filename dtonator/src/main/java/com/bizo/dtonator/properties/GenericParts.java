package com.bizo.dtonator.properties;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;

public class GenericParts implements IGenericParts {

  public ParameterizedType paramType;
  public TypeVariable typeVar;
  public WildcardType wildType;
  public GenericArrayType arrayType;
  public String operator;
  public Class boundClass;
  public MultiValuedMap<String, GenericParts> paramTypeArgs;
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

      for (String key : linkedTypes.keySet()) {
        linkedStr += "{" + key + "=" + StringUtils.joinWith(",", linkedTypes.get(key)) + "}";
      }
    }
    return linkedStr;
  }

  @Override
  public String getParamTypeArgsString() {
    String paramTypeArgsStr = "";
    if (paramTypeArgs != null) {

      for (String key : paramTypeArgs.keySet()) {
        paramTypeArgsStr += "{" + key + "=" + StringUtils.joinWith(",", paramTypeArgs.get(key)) + "}";
      }
    }
    return paramTypeArgsStr;
  }

  public String getBoundClassString() {
    String boundString = "";
    if (boundClass != null) {
      boundString = boundClass.getName();
    }
    return boundString;
  }

  @Override
  public MultiValuedMap<String, GenericParts> getLinkedTypes() {
    return linkedTypes;
  }

  @Override
  public MultiValuedMap<String, GenericParts> getParamTypeArgs() {
    return paramTypeArgs;
  }

  public String getFormattedString() {
    return getFormattedString(paramType != null ? getParamTypeString() : null);
  }

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
      Iterator<String> itr = linkedTypes.keySet().iterator();

      while (itr.hasNext()) {

        String key = itr.next();

        List<GenericParts> parts = (List<GenericParts>) linkedTypes.get(key);
        if (parts != null) {
          ListIterator<GenericParts> partsItr = parts.listIterator();

          while (partsItr.hasNext()) {

            sb.append(partsItr.next().getFormattedString());
            if (partsItr.hasNext()) {

              if (paramString != null) {

                sb.append(", ");
              } else {
                sb.append(" & ");
              }

            }

          }
        }
        if (itr.hasNext()) {
          sb.append(" & ");
        }

      }
    }

    if (paramTypeArgs != null) {
      Iterator<String> itr = paramTypeArgs.keySet().iterator();

      while (itr.hasNext()) {

        String key = itr.next();

        List<GenericParts> parts = (List<GenericParts>) paramTypeArgs.get(key);
        if (parts != null) {
          ListIterator<GenericParts> partsItr = parts.listIterator();

          while (partsItr.hasNext()) {

            sb.append(partsItr.next().getFormattedString());
            if (partsItr.hasNext()) {

              if (paramString != null) {

                sb.append(", ");
              } else {
                sb.append(" & ");
              }

            }

          }
        }
        if (itr.hasNext()) {
          sb.append(" & ");
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
      + ", getBoundClassString()="
      + getBoundClassString()
      + ", getParamTypeArgsString()="
      + getParamTypeArgsString()
      + ", getLinkedTypeString()="
      + getLinkedTypeString()
      + "]";
  }

}
