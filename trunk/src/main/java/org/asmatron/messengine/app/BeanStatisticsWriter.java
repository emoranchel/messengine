package org.asmatron.messengine.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanStatisticsWriter {
	private static final Log LOG = LogFactory.getLog(BeanStatisticsWriter.class);
	private final BeanStatistics beanStatistics;

	public BeanStatisticsWriter(BeanStatistics beanStatistics) {
		this.beanStatistics = beanStatistics;
	}

	public void saveToCSVFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				OutputStream outputStream = new FileOutputStream(file);
				saveToCSV(outputStream);
			} catch (FileNotFoundException e) {
				LOG.error(e, e);
			}
		}
	}

	public void saveToCSV(OutputStream outputStream) {
		CSVWriter writer = new CSVWriter(beanStatistics.getStats());
		writer.writeTo(outputStream);
	}

	public BeanStatisticsWriter deleteFile(String fileName) {
		new File(fileName).delete();
		return this;
	}
}

class CSVWriter {
	private List<CSVColumn> columns = new ArrayList<CSVColumn>();
	private final List<?> objs;

	public CSVWriter(List<?> objs) {
		this.objs = objs;
	}

	public void writeTo(OutputStream outputStream) {
		List<CSVColumn> columns = this.columns;
		if (columns.isEmpty() && !objs.isEmpty()) {
			columns = auto(objs.get(0));
		}
		Writer out = null;
		try {
			out = new OutputStreamWriter(outputStream);
			for (CSVColumn csvColumn : columns) {
				out.write(csvColumn.getHeader());
				out.write(',');
			}
			out.write("\n");
			for (Object obj : objs) {
				for (CSVColumn csvColumn : columns) {
					out.write(csvColumn.get(obj));
					out.write(',');
				}
				out.write("\n");
			}
		} catch (Exception e) {
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	private List<CSVColumn> auto(Object object) {
		List<CSVColumn> columns = new ArrayList<CSVColumn>();
		Method[] declaredMethods = object.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			boolean noArguments = method.getParameterTypes().length == 0;
			boolean isVoid = method.getReturnType().equals(void.class);
			boolean isPublic = (method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC;
			boolean isNotStatic = (method.getModifiers() & Modifier.STATIC) != Modifier.STATIC;
			boolean isGetter = method.getName().startsWith("get");
			if (isGetter && noArguments && isPublic && isNotStatic && !isVoid) {
				columns.add(new CSVColumnMethodReflected(method.getName(), method.getName()));
			}
		}
		return columns;
	}
}

abstract class CSVColumn {
	private final String header;

	public CSVColumn(String header) {
		this.header = header;
	}

	public abstract String get(Object obj);

	public String getHeader() {
		return header;
	}
}

class CSVColumnMethodReflected extends CSVColumn {
	private final String name;

	public CSVColumnMethodReflected(String header, String name) {
		super(header);
		this.name = name;
	}

	@Override
	public String get(Object obj) {
		try {
			Method method = obj.getClass().getDeclaredMethod(name);
			Object invoke = method.invoke(obj);
			return invoke == null ? "" : invoke.toString();
		} catch (Exception e) {
			return "";
		}
	}

}