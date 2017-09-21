package com.bizo.dtonator.properties;

import static joist.util.Copy.list;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.*;

import org.apache.commons.beanutils.PropertyUtils;

public class ReflectionTypeOracle implements TypeOracle {

  @Override
  public List<Prop> getProperties(final String className, boolean excludeInherited) {

    return getProperties(className, excludeInherited, null);
  }

  @Override
  public List<Prop> getProperties(String className, boolean excludeInherited, List<String> excludedAnnotations) {
    // Do we have to sort these for determinism?
    final List<Prop> ps = list();
    for (final PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(getClass(className))) {
      if (pd.getName().equals("class") || pd.getName().equals("declaringClass")) {
        continue;
      }

      if (!includeAnnotatedField(className, pd, excludedAnnotations)) {
        continue;
      }

      String type = pd.getReadMethod() == null ? pd.getPropertyType().getName()
        : pd.getReadMethod().getGenericReturnType().toString().replaceAll("^class ", "");
      Map<String, String> genericMap = new HashMap<String, String>();

      if (TypeUtils.containsTypeVariables(pd.getReadMethod().getGenericReturnType())) {
        Map<TypeVariable<?>, Type> map = TypeUtils.getTypeArguments(getClass(className), pd.getReadMethod().getDeclaringClass());

        if (map != null && !map.isEmpty()) {

          Type t = map.get(map.keySet().iterator().next());
          if (t != null) {
            type = t.getTypeName().replaceAll("^class ", "");
          }

        } else {

          genericMap
            .put(pd.getReadMethod().getReturnType().toString().replaceAll("^class ", ""), pd.getReadMethod().getGenericReturnType().toString());

        }
      }
      ps.add(new Prop( //
        pd.getName(),
        type,
        pd.getWriteMethod() == null,
        pd.getReadMethod() == null ? null : pd.getReadMethod().getName(),
        pd.getWriteMethod() == null ? null : pd.getWriteMethod().getName(),
        excludeInherited && className != null && !className.equals(pd.getReadMethod().getDeclaringClass().getName()),
        genericMap));
    }
    return ps;
  }

  private boolean includeAnnotatedField(String className, PropertyDescriptor pd, List<String> excludedAnnotations) {

    if (excludedAnnotations != null && hasAnnotation(className, pd, excludedAnnotations)) {
      return false;
    }

    return true;

  }

  public static boolean hasAnnotation(final String className, final PropertyDescriptor pd, List<String> annotations) {

    if (annotations != null) {
      for (String annotationName : annotations) {

        Class<?> clazz = getClass(className);
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
  public boolean isEnum(final String className) {
    try {
      return getClass(className).isEnum();
    } catch (final IllegalArgumentException iae) {
      return false; // for primitives like boolean
    }
  }

  @Override
  public boolean isAbstract(String className) {
    try {
      return Modifier.isAbstract(getClass(className).getModifiers());
    } catch (final IllegalArgumentException iae) {
      return false; // for primitives like boolean
    }
  }

  @Override
  public List<String> getEnumValues(final String className) {
    final List<String> values = list();
    for (final Object o : getClass(className).getEnumConstants()) {
      final Enum<?> e = (Enum<?>) o;
      values.add(e.name());
    }
    return values;
  }

  private static Class<?> getClass(final String className) {
    try {
      return Class.forName(className);
    } catch (final ClassNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
