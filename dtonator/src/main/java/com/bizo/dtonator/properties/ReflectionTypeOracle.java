package com.bizo.dtonator.properties;

import static joist.util.Copy.list;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.*;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;
import ru.vyarus.java.generics.resolver.util.NoGenericException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class ReflectionTypeOracle implements TypeOracle {

  @Override
  public List<Prop> getProperties(final String className, boolean excludeInherited) {

    return getProperties(className, excludeInherited, null);
  }

  @Override
  public List<Prop> getProperties(String className, boolean excludeInherited, List<String> excludedAnnotations) {
    // Do we have to sort these for determinism?
    final List<Prop> ps = list();

    try {
      Class<?> clazz = getClass(className);
      for (final PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(getClass(className))) {
        if (pd.getName().equals("class") || pd.getName().equals("declaringClass")) {
          continue;
        }

        if (!includeAnnotatedField(clazz, pd, excludedAnnotations)) {
          continue;
        }

        boolean isAbstract = false;
        String type = pd.getPropertyType().getName();
        MultiValuedMap<String, GenericParts> genericMethodMap = new ArrayListValuedHashMap<>();
        if (pd.getReadMethod() != null) {

          if (clazz.equals(pd.getReadMethod().getDeclaringClass()) || pd.getReadMethod().getGenericReturnType() instanceof ParameterizedType) {

            type = pd.getReadMethod().getGenericReturnType().toString().replaceAll("^class ", "").replaceAll("^interface ", "");

          }

          if (TypeUtils.containsTypeVariables(pd.getReadMethod().getGenericReturnType())) {

            System.out.println(pd.getName() + " Resolving from " + clazz + " to " + pd.getReadMethod().getDeclaringClass().toString());
            GenericsContext context = GenericsResolver.resolve(clazz).type(pd.getReadMethod().getDeclaringClass());
            Class outer = context.resolveClass(pd.getReadMethod().getGenericReturnType());
            Class inner = null;
            if (!clazz.equals(pd.getReadMethod().getDeclaringClass()) && pd.getReadMethod().getGenericReturnType() instanceof ParameterizedType) {
              System.out.println("Resolving generic...");
              inner = context.resolveGenericOf(pd.getReadMethod().getGenericReturnType());

              String outerString = outer.toString().replaceAll("^class ", "").replaceAll("^interface ", "");
              String innerString = inner.toString().replaceAll("^class ", "").replaceAll("^interface ", "");
              type = outerString + "<" + innerString + ">";
            }
            System.out.println("Resolved outer " + outer + ", inner " + inner);
            System.out.println("");
            genericMethodMap = GenericParser.typeToMap(pd.getReadMethod().getGenericReturnType(), pd.getName());

          }

          isAbstract = Modifier.isAbstract(pd.getReadMethod().getModifiers());
        }
        ps.add(new Prop( //
          pd.getName(),
          type,
          pd.getWriteMethod() == null,
          pd.getReadMethod() == null ? null : pd.getReadMethod().getName(),
          pd.getWriteMethod() == null ? null : pd.getWriteMethod().getName(),
          excludeInherited && className != null && !className.equals(pd.getReadMethod().getDeclaringClass().getName()),
          isAbstract,
          genericMethodMap));
      }

      return ps;

    } catch (final ClassNotFoundException iae) {
      return list();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return list();
    } catch (NoGenericException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return list();
    }

  }

  private boolean includeAnnotatedField(Class<?> clazz, PropertyDescriptor pd, List<String> excludedAnnotations) throws ClassNotFoundException {

    if (excludedAnnotations != null && hasAnnotation(clazz, pd, excludedAnnotations)) {
      return false;
    }

    return true;

  }

  public static boolean hasAnnotation(Class<?> clazz, final PropertyDescriptor pd, List<String> annotations) throws ClassNotFoundException {

    if (annotations != null) {

      for (String annotationName : annotations) {

        Class<? extends Annotation> annotation = (Class<? extends Annotation>) getClass(annotationName);

        List<Field> annotatedFields = FieldUtils.getFieldsListWithAnnotation(clazz, annotation);
        for (Field f : annotatedFields) {
          if (f.getName().equals(pd.getName())) {
            return true;
          }
        }

        List<Method> annotatedMethods = MethodUtils.getMethodsListWithAnnotation(clazz, annotation, true, true);
        for (Method m : annotatedMethods) {
          if (m.getName().equals(pd.getReadMethod().getName())) {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public MultiValuedMap<String, GenericPartsDto> getClassTypes(String className) {

    try {
      MultiValuedMap<String, GenericParts> genericClassMap = new ArrayListValuedHashMap<>();
      genericClassMap = GenericParser.typeToMap(getClass(className));
      return GenericParser.convertGenericMap(genericClassMap);
    } catch (final ClassNotFoundException iae) {
      return null;
    }

  }

  @Override
  public String getClassTypesString(String className) {
    MultiValuedMap<String, GenericPartsDto> partsMap = getClassTypes(className);
    String typeStr = "";
    if (partsMap != null) {

      typeStr = GenericParser.typeToMapString(partsMap);
    }
    return typeStr;
  }

  @Override
  public boolean isEnum(final String className) {
    try {
      return getClass(className).isEnum();
    } catch (final ClassNotFoundException iae) {
      return false; // for primitives like boolean
    }
  }

  @Override
  public boolean isAbstract(String className) {
    try {
      return Modifier.isAbstract(getClass(className).getModifiers());
    } catch (final ClassNotFoundException iae) {
      return false; // for primitives like boolean
    }
  }

  @Override
  public List<String> getEnumValues(final String className) {

    try {
      final List<String> values = list();
      for (final Object o : getClass(className).getEnumConstants()) {
        final Enum<?> e = (Enum<?>) o;
        values.add(e.name());
      }
      return values;
    } catch (final ClassNotFoundException iae) {
      return list(); // for primitives like boolean
    }

  }

  private static Class<?> getClass(final String className) throws ClassNotFoundException {

    if (className != null) {
      return Class.forName(className);
    } else {
      throw new ClassNotFoundException();
    }

  }
}
