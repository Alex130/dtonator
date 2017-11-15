package com.bizo.dtonator.config;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBetween;

import org.apache.commons.collections4.MultiValuedMap;

import com.bizo.dtonator.Names;
import com.bizo.dtonator.properties.GenericPartsDto;
import com.bizo.dtonator.properties.TypeOracle;

/** A property to map back/forth between DTO/domain object. */
public class DtoProperty {

  private final TypeOracle oracle;
  private final RootConfig config;
  private final DtoConfig dto;
  private final String name;
  private final boolean readOnly;
  private final boolean isChainedId;
  /** could be String, Money (value type), Employer, List<Employer>. */
  private final String domainType;
  /** could be String, Money (value type), EmployerDto, ArrayList<EmployerDto> */
  private final String dtoType;
  private final String getterMethodName;
  private final String setterNameMethod;
  private final boolean inherited;
  private final String genericDomainType;
  private final String genericDtoType;
  private final boolean isAbstract;

  public DtoProperty(
    final TypeOracle oracle,
    final RootConfig config,
    final DtoConfig dto,
    final String name,
    final boolean readOnly,
    final boolean isChainedId,
    final String dtoType,
    final String domainType,
    final String getterMethodName,
    final String setterNameMethod,
    final boolean inherited,
    final boolean isAbstract,
    final String genericDomainType,
    final String genericDtoType) {
    this.oracle = oracle;
    this.config = config;
    this.dto = dto;
    this.name = name;
    this.readOnly = readOnly;
    this.isChainedId = isChainedId;
    this.dtoType = dtoType;
    this.domainType = domainType;
    this.getterMethodName = getterMethodName;
    this.setterNameMethod = setterNameMethod;
    this.inherited = inherited;
    this.isAbstract = isAbstract;
    this.genericDomainType = genericDomainType;
    this.genericDtoType = genericDtoType;
  }

  public DtoConfig getDto() {
    return dto;
  }

  public String getDtoType() {
    return dtoType;
  }

  public String getDtoTypeBoxed() {
    return Primitives.boxIfNecessary(dtoType);
  }

  public boolean isEntity() {
    return DtoConfig.isEntity(oracle, isGenericType() ? genericDomainType : domainType);
  }

  public String getTypeArg() {
    String typeArg = domainType;
    if (isGenericType()) {
      typeArg += " extends " + genericDomainType;
    }
    return typeArg;
  }

  public boolean isGenericType() {
    return genericDomainType != null && !genericDomainType.isEmpty();
  }

  public boolean isDto() {
    return config.getDto(dtoType) != null && !config.getDto(dtoType).isEnum();
  }

  public boolean isList() {
    return dtoType.startsWith("java.util.ArrayList") || dtoType.startsWith("java.util.List");
  }

  public boolean isSet() {
    return dtoType.startsWith("java.util.HashSet") || dtoType.startsWith("java.util.Set");
  }

  public boolean isListOfEntities() {
    return DtoConfig.isListOfEntities(config, isGenericType() ? genericDomainType : domainType);
  }

  public boolean isSetOfEntities() {
    return DtoConfig.isSetOfEntities(config, isGenericType() ? genericDomainType : domainType);
  }

  public boolean isListOfDtos() {
    return DtoConfig.isListOfDtos(config, dtoType);
  }

  public boolean isSetOfDtos() {
    return DtoConfig.isSetOfDtos(config, dtoType);
  }

  public String getSingleDtoType() {
    // assumes isListOfEntities
    return getSingleDtoType(getDtoType());
  }

  public String getSingleDtoType(String type) {
    // assumes isListOfEntities
    return substringBetween(type, "<", ">");
  }

  public String getSimpleSingleDtoType() {
    // assumes isListOfEntities
    return substringAfterLast(getSingleDtoType(), ".");
  }

  public DtoConfig getSingleDto() {
    // assumes isListOfEntities (or manual isListOfDtos)
    return config.getDto(getSimpleSingleDtoType());
  }

  public String getSingleDomainType() {
    // assumes isListOfEntities
    return Names.listType(domainType);
  }

  public boolean isValueType() {
    return getValueTypeConfig() != null;
  }

  public boolean isEnum() {
    if (config.getDto(getDtoType()) != null) {
      return config.getDto(getDtoType()).isEnum();
    }
    return oracle.isEnum(getDomainType());
  }

  public ValueTypeConfig getValueTypeConfig() {
    return config.getValueTypeForDomainType(getDomainType());
  }

  /** only meaningful for non-manual dtos, otherwise everything is manual... */
  public boolean isExtension() {
    return (getterMethodName == null && setterNameMethod == null && !isChainedId()) //
      || dto.getForcedMappers().contains(name);
  }

  @Override
  public String toString() {
    return name;
  }

  public String getName() {
    return name;
  }

  public String getDomainType() {
    return domainType;
  }

  public String getGetterMethodName() {
    return getterMethodName;
  }

  public String getSetterMethodName() {
    return setterNameMethod;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public boolean isChainedId() {
    return isChainedId;
  }

  public boolean isInherited() {
    return inherited;
  }

  public boolean isAbstract() {
    return isAbstract;
  }

  public String getGenericDomainType() {
    return genericDomainType;
  }

  public String getGenericDtoType() {
    return genericDtoType;
  }

}
