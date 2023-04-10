package cz.dangelcz.print.grfgen.libs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.dangelcz.print.grfgen.libs.annotations.Ignore;

public class Reflection
{
	public enum FieldCriteria
	{
		BY_NAME
		{
			@Override
			public boolean filter(Field field, Object criteria)
			{
				return criteria instanceof String && field.getName().equals(criteria.toString());
			}
		},

		BY_ANNOTATION
		{
			@Override
			public boolean filter(Field field, Object criteria)
			{
				Class<?> annotation = (Class<?>) criteria;

				if (annotation.isAnnotation())
				{
					return field.isAnnotationPresent((Class<? extends Annotation>) annotation);
				}

				return false;
			}
		},

		BY_CLASS
		{
			@Override
			public boolean filter(Field field, Object criteria)
			{
				return hasParentClass(field.getType(), (Class<?>) criteria);
			}
		},

		BY_SAME_CLASS
		{
			@Override
			public boolean filter(Field field, Object criteria)
			{
				return compareClasses(field.getType(), criteria.getClass());
			}
		};

		public abstract boolean filter(Field field, Object criteria);
	}

	public static List<Field> fieldsOfAnnotation(Class<?> currentClass, Class<? extends Annotation> annotation)
	{
		return findFieldsBy(currentClass, Object.class, annotation, FieldCriteria.BY_ANNOTATION);
	}

	public static List<Field> fieldsOfClass(Class<?> currentClass, Class<?> fieldClass)
	{
		return findFieldsBy(currentClass, Object.class, fieldClass, FieldCriteria.BY_CLASS);
	}

	public static Field findFieldByName(Class<?> objectClass, String fieldName)
	{
		return findFieldByName(objectClass, objectClass, fieldName);
	}

	public static Field findFieldByName(Class<?> objectClass, Class<?> superParentClass, String fieldName)
	{
		return findFieldBy(objectClass, superParentClass, fieldName, FieldCriteria.BY_NAME);
	}

	public static String fieldNameByAnnotation(Class<?> objectClass, Class<?> superParentClass, Class<? extends Annotation> annotation) throws IllegalArgumentException, IllegalAccessException
	{
		Field f = findFieldWithAnnotation(objectClass, superParentClass, annotation);
		return f == null ? null : f.getName();
	}

	public static Field findFieldWithAnnotation(Class<?> objectClass, Class<?> superParentClass, Class<? extends Annotation> annotation)
			throws IllegalArgumentException, IllegalAccessException
	{
		return findFieldBy(objectClass, superParentClass, annotation, FieldCriteria.BY_ANNOTATION);
	}

	public static Field findFieldBy(Class<?> objectClass, Class<?> superParentClass, Object criteriaObject, FieldCriteria criteria)
	{
		for (Field f : objectClass.getDeclaredFields())
		{
			f.setAccessible(true);
			if (criteria.filter(f, criteriaObject))
			{
				return f;
			}
		}

		Class<?> parent = objectClass.getSuperclass();

		if (!objectClass.getName().equalsIgnoreCase(superParentClass.getName()))
		{
			return findFieldBy(parent, superParentClass, criteriaObject, criteria);
		}

		return null;
	}

	public static List<Field> findFieldsBy(Class<?> objectClass, Class<?> superParentClass, Object criteriaObject, FieldCriteria criteria)
	{
		List<Field> fieldsList = new ArrayList<>();

		findFieldsBy(objectClass, superParentClass, criteriaObject, criteria, fieldsList);

		return fieldsList;
	}

	public static void findFieldsBy(Class<?> objectClass, Class<?> superParentClass, Object criteriaObject, FieldCriteria criteria, List<Field> fieldsList)
	{
		for (Field f : objectClass.getDeclaredFields())
		{
			f.setAccessible(true);
			if (criteria.filter(f, criteriaObject))
			{
				fieldsList.add(f);
			}
		}

		Class<?> parent = objectClass.getSuperclass();

		if (!objectClass.getName().equalsIgnoreCase(superParentClass.getName()))
		{
			findFieldsBy(parent, superParentClass, criteriaObject, criteria, fieldsList);
		}
	}

	public static boolean hasParentClass(Class<?> testedClass, Class<?> expectedParent)
	{
		if (!compareClasses(testedClass, expectedParent))
		{
			Class<?> parentClass = testedClass.getSuperclass();

			if (parentClass == null || compareClasses(parentClass, Object.class))
			{
				return false;
			}

			return hasParentClass(parentClass, expectedParent);
		}

		return true;
	}

	public static boolean compareClasses(Class<?> class1, Class<?> class2)
	{
		return class1.equals(class2);
	}

	public static void hashMapToObject(Object object, Class<?> superParent, Map<String, Object> data) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> cl = object.getClass();
		String superParentName = superParent.getSimpleName();
		insertToFields(object, cl, superParentName, data);
	}

	public static void insertToFields(Object object, Class<?> objClass, String superParentName, Map<String, Object> data) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> parent = objClass.getSuperclass();

		if (objClass.getSimpleName().compareTo(superParentName) != 0)
		{
			insertToFields(object, parent, superParentName, data);
		}

		for (Field f : objClass.getDeclaredFields())
		{
			if (!Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(Ignore.class))
			{
				String name = f.getName();
				if (data.containsKey(name))
				{
					setFieldValue(f, object, data.get(name));
				}
			}
		}
	}

	public static void setStaticFieldValue(Class<?> classInstance, String fieldName, Object value)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		setStaticFieldValue(classInstance.getField(fieldName), value);
	}

	public static void setStaticFieldValue(Field f, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		setFieldValue(f, (Object) null, value);
	}

	public static void setFieldValue(Field f, Object object, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		f.setAccessible(true);

		String type = f.getType().getName().toLowerCase();

		switch (type)
		{
			case "double":
				if (value instanceof String)
				{
					f.set(object, Double.parseDouble((String) value));
				} else
				{
					f.set(object, value);
				}
				break;
			case "int":
				if (value instanceof String)
				{
					f.set(object, Integer.parseInt((String) value));
				} else
				{
					f.set(object, value);
				}
				break;
			case "boolean":
				if (value instanceof Boolean)
				{
					f.set(object, value);
				} else if (value instanceof String)
				{
					f.set(object, Boolean.parseBoolean((String) value));
				} else
				{
					if (value == null)
					{
						value = 0;
					}

					f.set(object, ((int) value) > 0);
				}
				break;
			default:
				f.set(object, value);
				return;
		}
	}

	public static HashMap<String, Object> objectToHashMap(Object object, Class<?> superParent) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> cl = object.getClass();
		String superParentName = superParent.getSimpleName();
		HashMap<String, Object> data = new HashMap<>();
		fieldsToHashMap(object, cl, superParentName, data);
		return data;
	}

	public static void fieldsToHashMap(Object object, Class<?> objClass, String superParentName, HashMap<String, Object> data)
			throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> parent = objClass.getSuperclass();

		if (objClass.getSimpleName().compareTo(superParentName) != 0)
		{
			// insertToFields(object, parent, superParentName, data);
			fieldsToHashMap(object, parent, superParentName, data);
		}

		for (Field f : objClass.getDeclaredFields())
		{
			if (!Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(Ignore.class))
			{
				String name = f.getName();
				Object value = f.get(object);
				data.put(name, value);
			}
		}
	}

	public static Object getFieldValue(Object instance, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field f = instance.getClass().getField(fieldName);
		f.setAccessible(true);

		return f.get(instance);
	}

	public static Object getStaticFieldValue(Class<?> classInstance, String fieldName)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field f = classInstance.getField(fieldName);
		f.setAccessible(true);

		return f.get(null);
	}

	public static void setFieldValue(Object instance, String fieldName, Object value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field f = instance.getClass().getField(fieldName);
		setFieldValue(f, instance, value);
	}
}
