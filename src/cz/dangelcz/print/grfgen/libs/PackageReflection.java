package cz.dangelcz.print.grfgen.libs;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class PackageReflection
{
	public enum ClassCriteria
	{
		BY_ANNOTATION
		{
			@SuppressWarnings("unchecked")
			@Override
			public boolean filter(Class<?> clazz, Object criteria)
			{
				Class<?> annotation = (Class<?>) criteria;

				if (annotation.isAnnotation())
				{
					return clazz.isAnnotationPresent((Class<? extends Annotation>) annotation);
				}

				return false;
			}
		},

		BY_PARENT_CLASS
		{
			@Override
			public boolean filter(Class<?> clazz, Object criteria)
			{
				return Reflection.hasParentClass(clazz, (Class<?>) criteria);
			}
		},

		BY_INTERFACE
		{
			@Override
			public boolean filter(Class<?> clazz, Object criteria)
			{
				Class<?>[] interfaces = clazz.getInterfaces();
				Class<?> criteriaClass = (Class<?>) criteria;

				for (int j = 0; j < interfaces.length; j++)
				{
					if (interfaces[j].getName().equals(criteriaClass.getName()))
					{
						return true;
					}
				}

				return false;
			}
		};

		public abstract boolean filter(Class<?> clazz, Object criteria);
	}

	public static List<Class<?>> getClassesOfPackage(String packageName) throws ClassNotFoundException, URISyntaxException, IOException
	{
		return getClassesOfPackage(packageName, null, null);
	}

	public static List<Class<?>> getClassesOfPackage(String packageName, ClassCriteria criteria, Object criteriaObject) throws ClassNotFoundException, URISyntaxException, IOException
	{
		List<Class<?>> classList = new ArrayList<Class<?>>();

		if (GeneralHelper.INL(packageName))
		{
			return classList;
		}

		packageName = toSlashesPackageName(packageName);
		String dotPackageName = toDotPackageName(packageName);

		for (String fullClassName : getClassNamesOfPackage(packageName))
		{
			String className = getClassName(fullClassName);

			if (GeneralHelper.INL(className))
			{
				continue;
			}

			String classNamePath = dotPackageName + "." + className;

			Class<?> repoClass = Class.forName(classNamePath);

			if (criteria == null || criteria.filter(repoClass, criteriaObject))
			{
				classList.add(repoClass);
			}
		}

		return classList;
	}

	private static String getClassName(String fullClassName)
	{
		int classTypeIndex = fullClassName.indexOf(".");

		if (classTypeIndex == -1)
		{
			return null;
		}

		int pathEndIndex = fullClassName.lastIndexOf("/");

		if (pathEndIndex == -1)
		{
			pathEndIndex = fullClassName.lastIndexOf("\\");
		}

		if (pathEndIndex != -1)
		{
			fullClassName = fullClassName.substring(pathEndIndex + 1, classTypeIndex);
		}

		return fullClassName;
	}

	protected static String toSlashesPackageName(String packageName)
	{
		packageName = packageName.replace('.', '/');

		if (!packageName.startsWith("/"))
		{
			packageName = "/" + packageName;
		}
		return packageName;
	}

	protected static String toDotPackageName(String packageName)
	{
		packageName = packageName.replace('/', '.');

		if (packageName.startsWith("."))
		{
			packageName = packageName.substring(1);
		}

		return packageName;
	}

	private static List<String> getClassNamesOfPackage(String packageName) throws URISyntaxException, IOException
	{
		List<String> classList = new ArrayList<>();

		URI uri;
		Path myPath;
		FileSystem fileSystem = null;

		uri = PackageReflection.class.getResource(packageName).toURI();
		if (uri.getScheme().equals("jar"))
		{
			fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object> emptyMap());
			myPath = fileSystem.getPath(packageName);
		} else
		{
			myPath = Paths.get(uri);
		}

		Stream<Path> walk = Files.walk(myPath, 1);

		for (Iterator<Path> it = walk.iterator(); it.hasNext();)
		{
			classList.add(it.next().toString());
		}

		walk.close();

		if (fileSystem != null && fileSystem.isOpen())
		{
			try
			{
				fileSystem.close();
			} catch (IOException e)
			{
			}
		}

		return classList;
	}
}
