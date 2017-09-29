package com.bizo.dtonator.properties;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;

public class GenericPartsDto implements IGenericParts {

  public String paramType;
  public String typeVar;
  public Boolean wildType;
  public String arrayType;
  public String operator;
  public String boundClass;
  public MultiValuedMap<String, GenericPartsDto> paramTypeArgs;
  public MultiValuedMap<String, GenericPartsDto> linkedTypes;

  public GenericPartsDto() {
    super();

  }

  public GenericPartsDto(GenericParts gp) {
    super();
    if (gp.paramType != null) {
      this.paramType = gp.getParamTypeString();
    }
    this.typeVar = gp.getTypeVarString();
    this.wildType = gp.wildType != null;
    this.arrayType = gp.getArrayTypeString();
    this.operator = gp.operator;
    this.boundClass = gp.getBoundClassString();

    MultiValuedMap<String, GenericPartsDto> linksDtoMap = new ArrayListValuedHashMap<>();
    this.linkedTypes = convertGenericMap(gp.getLinkedTypes(), linksDtoMap);
    MultiValuedMap<String, GenericPartsDto> paramsDtoMap = new ArrayListValuedHashMap<>();
    this.paramTypeArgs = convertGenericMap(gp.getParamTypeArgs(), paramsDtoMap);
  }

  public static <T extends IGenericParts> MultiValuedMap<String, GenericPartsDto> convertGenericMap(
    MultiValuedMap<String, T> partsMap,
    MultiValuedMap<String, GenericPartsDto> partsDtoMap) {

    if (partsMap != null) {
      for (String key : partsMap.keySet()) {
        List<T> parts = (List<T>) partsMap.get(key);
        if (parts != null) {
          for (T gp : parts) {
            partsDtoMap.put(key, new GenericPartsDto((GenericParts) gp));

          }
        }
      }
    }
    return partsDtoMap;
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getParamTypeString()
   */
  @Override
  public String getParamTypeString() {

    return paramType;
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getTypeVarString()
   */
  @Override
  public String getTypeVarString() {

    return typeVar;
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getArrayTypeString()
   */
  @Override
  public String getArrayTypeString() {

    return arrayType;
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getLinkedTypeString()
   */
  @Override
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

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getBoundClassString()
   */
  @Override
  public String getBoundClassString() {

    return boundClass;
  }

  @Override
  public MultiValuedMap<String, GenericPartsDto> getLinkedTypes() {
    return linkedTypes;
  }

  @Override
  public MultiValuedMap<String, GenericPartsDto> getParamTypeArgs() {
    return paramTypeArgs;
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getFormattedString()
   */
  @Override
  public String getFormattedString() {
    return getFormattedString(paramType != null ? getParamTypeString() : null);
  }

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#getFormattedString(java.lang.String)
   */
  @Override
  public String getFormattedString(String paramString) {
    StringBuilder sb = new StringBuilder();
    if (paramString != null) {
      sb.append(paramString).append("<");
    }

    if (typeVar != null) {
      sb.append(getTypeVarString());
    }
    if (wildType) {
      sb.append("?");
    }
    if (arrayType != null && !arrayType.isEmpty()) {
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

        List<GenericPartsDto> parts = (List<GenericPartsDto>) linkedTypes.get(key);
        if (parts != null) {
          ListIterator<GenericPartsDto> partsItr = parts.listIterator();

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

        List<GenericPartsDto> parts = (List<GenericPartsDto>) paramTypeArgs.get(key);
        if (parts != null) {
          ListIterator<GenericPartsDto> partsItr = parts.listIterator();

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

  /* (non-Javadoc)
   * @see com.bizo.dtonator.properties.IGnericParts#toString()
   */
  @Override
  public String toString() {
    return "GenericPartsDto [paramType="
      + paramType
      + ", typeVar="
      + typeVar
      + ", wildType="
      + wildType
      + ", arrayType="
      + arrayType
      + ", operator="
      + operator
      + ", boundClass="
      + boundClass
      + ", getLinkedTypeString()="
      + getLinkedTypeString()
      + ", getLinkedTypeString()="
      + getLinkedTypeString()
      + "]";
  }

}
