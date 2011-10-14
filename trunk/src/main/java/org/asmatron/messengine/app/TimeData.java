package org.asmatron.messengine.app;

public class TimeData {
	private boolean calculated = false;
	private final long[] stats;
	int index = 0;
	private long total;
	private long average;
	private int aboveAverage;
	private int belowAverage;
	private long maximum;

	public TimeData(int size) {
		stats = new long[size];
	}

	public void add(long totalTime) {
		calculated = false;
		stats[index] = totalTime;
		index++;
	}

	private void calculate() {
		if (stats.length == 0) {
			return;
		}
		maximum = 0;
		aboveAverage = 0;
		belowAverage = 0;
		total = 0;
		for (long l : stats) {
			total += l;
			maximum = Math.max(maximum, l);
		}
		average = total / stats.length;
		for (long l : stats) {
			if (l < average) {
				belowAverage++;
			} else {
				aboveAverage++;
			}
		}
		calculated = true;
	}

	public String getTotalStr() {
		return String.format("%6d", getTotal());
	}

	public String getAverageStr() {
		return String.format("%6d", getAverage());
	}

	public String getAboveAverageStr() {
		return String.format("%6d", getAboveAverage());
	}

	public String getBelowAverageStr() {
		return String.format("%6d", getBelowAverage());
	}

	public String getMaximumStr() {
		return String.format("%6d", getMaximum());
	}

	public long getTotal() {
		if (!calculated) {
			calculate();
		}
		return total;
	}

	public long getAverage() {
		if (!calculated) {
			calculate();
		}
		return average;
	}

	public int getAboveAverage() {
		if (!calculated) {
			calculate();
		}
		return aboveAverage;
	}

	public int getBelowAverage() {
		if (!calculated) {
			calculate();
		}
		return belowAverage;
	}

	public long getMaximum() {
		if (!calculated) {
			calculate();
		}
		return maximum;
	}

}