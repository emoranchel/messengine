package org.asmatron.messengine.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanStatistics {
	private final long startTime = System.currentTimeMillis();
	private Long firstTime = null;
	private long endTime = System.currentTimeMillis();
	Map<String, BeanStatistic> beans = new HashMap<String, BeanStatistic>();
	private static final BeanStatistic EMPTY = BeanStatistic.create().instantiate("", Object.class);

	public void add(BeanStatistic beanStat) {
		initTime();
		if (!beans.containsKey(beanStat.id())) {
			beans.put(beanStat.id(), beanStat);
		}
	}

	public BeanStatistic get(String name, Class<?> clazz) {
		initTime();
		String key = BeanStatistic.toId(name, clazz);
		BeanStatistic stat = beans.get(key);
		if (stat == null) {
			return EMPTY;
		}
		return stat;
	}

	private void initTime() {
		if (firstTime == null) {
			firstTime = System.currentTimeMillis();
		}
	}

	public void end() {
		initTime();
		endTime = System.currentTimeMillis();
	}

	public List<BeanStatistic> getStats() {
		List<BeanStatistic> stats = new ArrayList<BeanStatistic>(beans.values());
		Collections.sort(stats);
		return stats;
	}

	public long limboTime() {
		initTime();
		return firstTime - startTime;
	}

	public long totalTime() {
		return endTime - startTime;
	}

	public String getDescription() {
		List<BeanStatistic> stats = getStats();
		TimeData beansInstantiation = new TimeData(stats.size());
		TimeData beansInitialization = new TimeData(stats.size());
		TimeData beansTotal = new TimeData(stats.size());
		TimeData beansAutowiring = new TimeData(stats.size());

		StringBuilder str = new StringBuilder();
		str.append("--- BEAN STATISTICS ---\n");
		for (BeanStatistic beanStatistic : stats) {
			beansInstantiation.add(beanStatistic.getInstantiationTime());
			beansInitialization.add(beanStatistic.getInitializationTime());
			beansAutowiring.add(beanStatistic.getAutowireTime());
			beansTotal.add(beanStatistic.getTotalTime());
			str.append(beanStatistic.toString());
			str.append("\n");
		}

		str.append("TOTAL: ");
		str.append(totalTime());
		str.append(" IN LIMBO=");
		str.append(limboTime());

		str.append(" BEANS[");
		str.append(stats.size());
		str.append("]");

		str.append("\n -=TOTALS=-                 ");
		str.append("TOTAL TIME: ");
		str.append(beansTotal.getTotalStr());
		str.append(" BEANS INSTANTIATION: ");
		str.append(beansInstantiation.getTotalStr());
		str.append(" BEANS AUTOWIRING: ");
		str.append(beansAutowiring.getTotalStr());
		str.append(" BEANS INITIALIZATION: ");
		str.append(beansInitialization.getTotalStr());

		str.append("\n -=AVERAGE=-                ");
		str.append("TOTAL TIME: ");
		str.append(beansTotal.getAverageStr());
		str.append(" BEANS INSTANTIATION: ");
		str.append(beansInstantiation.getAverageStr());
		str.append(" BEANS AUTOWIRING: ");
		str.append(beansAutowiring.getAverageStr());
		str.append(" BEANS INITIALIZATION: ");
		str.append(beansInitialization.getAverageStr());

		str.append("\n -=MAXIMUMS=-               ");
		str.append("TOTAL TIME: ");
		str.append(beansTotal.getMaximumStr());
		str.append(" BEANS INSTANTIATION: ");
		str.append(beansInstantiation.getMaximumStr());
		str.append(" BEANS AUTOWIRING: ");
		str.append(beansAutowiring.getMaximumStr());
		str.append(" BEANS INITIALIZATION: ");
		str.append(beansInitialization.getMaximumStr());

		str.append("\n -=ABOVE / BELOW AVERAGE =- ");
		str.append("TOTAL TIME: ");
		str.append(beansTotal.getAboveAverageStr() + "/" + beansTotal.getBelowAverageStr());
		str.append(" BEANS INSTANTIATION: ");
		str.append(beansInstantiation.getAboveAverageStr() + "/" + beansInstantiation.getBelowAverageStr());
		str.append(" BEANS AUTOWIRING: ");
		str.append(beansAutowiring.getAboveAverageStr() + "/" + beansAutowiring.getBelowAverageStr());
		str.append(" BEANS INITIALIZATION: ");
		str.append(beansInitialization.getAboveAverageStr() + "/" + beansInitialization.getBelowAverageStr());

		str.append("\n");
		return str.toString();

	}
}
