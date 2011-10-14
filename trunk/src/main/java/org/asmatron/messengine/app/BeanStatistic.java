package org.asmatron.messengine.app;

public class BeanStatistic implements Comparable<BeanStatistic> {
	private final long declareTime;
	private final long instantiationTime;
	private Long initTime = null;
	private Long endTime = null;
	private final String id;
	private final String name;
	private final String className;
	private Long beginAutowireTime;
	private Long endAutowireTime;

	private BeanStatistic(String name, Class<?> clazz, long startTime, long instantiationTime) {
		this.declareTime = startTime;
		this.instantiationTime = instantiationTime;
		this.className = clazz.getName();
		this.name = name;
		this.id = toId(name, clazz);
	}

	public void beginInitialization() {
		if (initTime == null) {
			initTime = System.currentTimeMillis();
		}
	}

	public void endInitialization() {
		if (endTime == null) {
			endTime = System.currentTimeMillis();
		}
	}

	@Override
	public int compareTo(BeanStatistic o) {
		if (o == null) {
			return 1;
		}
		return (int) (getTotalTime() - o.getTotalTime());
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("+ TotalTime: ");
		str.append(pad(getTotalTime()));
		str.append(" - Instantiaton: ");
		str.append(pad(getInstantiationTime()));
		str.append(" - Autowire: ");
		str.append(pad(getAutowireTime()));
		str.append(" - limbo: ");
		str.append(pad(getLimboTime()));
		str.append(" - initialiazation: ");
		str.append(pad(getInitializationTime()));
		str.append(" >> ");
		str.append(getClassName());
		str.append(" :: ");
		str.append(getName());
		return str.toString();
	}

	public String id() {
		return id;
	}

	public String getClassName() {
		return className;
	}

	private String pad(long totalTime) {
		return String.format("%5d", totalTime);
	}

	public long getAutowireTime() {
		return endAutowireTime() - beginAutowireTime();
	}

	public long getInitializationTime() {
		return endTime() - initTime();
	}

	public long getLimboTime() {
		return (beginAutowireTime() - instantiationTime) + (initTime() - endAutowireTime());
	}

	public long getInstantiationTime() {
		return instantiationTime - declareTime;
	}

	public long getTotalTime() {
		return endTime() - declareTime - getLimboTime() - getAutowireTime();
	}

	private long initTime() {
		if (initTime == null) {
			return instantiationTime;
		}
		return initTime.longValue();
	}

	private long endTime() {
		if (endTime == null) {
			return initTime();
		}
		return endTime.longValue();
	}

	private long beginAutowireTime() {
		if (beginAutowireTime == null) {
			return instantiationTime;
		}
		return beginAutowireTime.longValue();
	}

	private long endAutowireTime() {
		if (endAutowireTime == null) {
			return instantiationTime;
		}
		return endAutowireTime.longValue();
	}

	public String getName() {
		return name;
	}

	public static String toId(String name, Class<?> clazz) {
		return clazz.getName() + "::" + name;
	}

	public static BeanStatisticFactory create() {
		return new BeanStatisticFactory();
	}

	public static class BeanStatisticFactory {
		private long declaredFTime = System.currentTimeMillis();

		public BeanStatistic instantiate(String name, Class<?> clazz) {
			return new BeanStatistic(name, clazz, declaredFTime, System.currentTimeMillis());
		}
	}

	public void beginAutowire() {
		if (beginAutowireTime == null) {
			beginAutowireTime = System.currentTimeMillis();
		}
	}

	public void endAutowire() {
		if (endAutowireTime == null) {
			endAutowireTime = System.currentTimeMillis();
		}
	}
}
