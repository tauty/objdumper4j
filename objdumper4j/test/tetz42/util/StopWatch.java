/*
 * Copyright 2010 tetsuo.ohta[at]gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tetz42.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class provides timer function easy to use.
 * <p>
 * 
 * [sample]<br>
 * StopWatch sw = new StopWatch();<br>
 * sw.start("key1");<br>
 * &emsp;&emsp; :<br>
 * sw.start("key2");<br>
 * &emsp;&emsp; :<br>
 * sw.end("key1");<br>
 * &emsp;&emsp; :<br>
 * sw.start("key1");<br>
 * &emsp;&emsp; :<br>
 * sw.end("key1");<br>
 * sw.end("key2");<br>
 * <br>
 * System.out.println(sw);<br>
 * <br>
 * - result<br>
 * key1(140msec, 2time, average:70.0msec)<br>
 * key2(100msec, 1time, average:100.0msec)<br>
 * Summary:150msec<br>
 * 
 * @version 1.0
 * @author tetz
 */
public class StopWatch {

	private long generated = System.currentTimeMillis();
	private Map<String, Long> startMap = new HashMap<String, Long>();
	private Map<String, Unit> resultMap = new LinkedHashMap<String, Unit>();

	/**
	 * Starts the stop watch.
	 * 
	 * @param key
	 *            - key for mapping 'start' and 'end'
	 */
	public void start(String key) {
		startMap.put(key, System.currentTimeMillis());
	}

	/**
	 * Ends the stop watch.
	 * 
	 * @param key
	 *            - key for mapping 'start' and 'end'
	 */
	public void end(String key) {
		Unit unit = resultMap.get(key);
		if (unit == null)
			resultMap.put(key, unit = new Unit());
		unit.add(System.currentTimeMillis() - startMap.remove(key));
	}

	/**
	 * Show the result.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Unit> e : this.resultMap.entrySet()) {
			sb.append(e.getKey()).append(e.getValue());
		}
		return sb.append("Summary:")
				.append(System.currentTimeMillis() - this.generated)
				.append("msec").toString();
	}

	private class Unit {
		long sum_msec = 0;
		int time = 0;

		Unit add(long msec) {
			sum_msec += msec;
			time++;
			return this;
		}

		@Override
		public String toString() {
			double ave = sum_msec / time;
			return new StringBuilder().append("(").append(sum_msec)
					.append("msec, ").append(time).append("time, average:")
					.append(ave).append("msec)\n").toString();
		}
	}
}
