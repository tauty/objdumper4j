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

import static tetz42.util.ObjDumper4j.*;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.ReflectPermission;
import java.math.BigDecimal;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import tetz42.util.ObjDumper4j;

public class ObjDumper4jTest {

	@Test
	public void testDump() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SampleBean();

		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("aloha", "hawaii");
				put("hello", "england");
				put("konnichiwa", bean);
			}
		};

		// hooter adding.
		bean.obj = bean;
		System.out.println(dump("<<Header>>", map, bean, "<<Hooter>>"));

	}

	@Test
	public void testDumper() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SampleBean();
		System.out.println(dumper(bean));

		// add header
		System.out.println(dumper("<<Header>>\n", bean));

		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("aloha", "hawaii");
				put("hello", "england");
				put("konnichiwa", bean);
			}
		};
		System.out.println(dumper("<<Header>>\n", map));

		// referenced by another bean
		bean.obj = new SampleBean();
		System.out.println(dumper("<<Header>>\n", map));

		// referenced by itself.
		bean.obj = bean;
		System.out.println(dumper("<<Header>>\n", map));

		// hooter adding.
		bean.obj = bean;
		System.out.println(dumper("<<Header>>\n", map, "\n<<Hooter>>"));

		// Real Object instance.
		bean.obj = new Object();
		System.out.println(dumper("<<Header>>", map, bean, "<<Hooter>>"));
	}

	@Test
	public void testDiv() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SampleBean();
		System.out.println(dumper(bean).delimiter("========================"));

		// adding header
		System.out.println(dumper("<<Header>>\n", bean).delimiter(
				"~~~~~~~~~~~~~~~~~~~~~~~~~"));

		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("aloha", "hawaii");
				put("hello", "england");
				put("konnichiwa", bean);
			}
		};
		System.out.println(dumper("<<Header>>\n", map).delimiter(
				"#########################"));

		// referenced by another bean
		bean.obj = new SampleBean();
		System.out.println(dumper("<<Header>>\n", map).delimiter(
				"%%%%%%%%%%%%%%%%%%%%%%%%%"));

		// referenced by itself.
		bean.obj = bean;
		System.out.println(dumper("<<Header>>\n", map).delimiter(
				"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"));

		// hooter adding.
		bean.obj = bean;
		System.out.println(dumper("<<Header>>\n", map, "\n<<Hooter>>")
				.delimiter(">>\n\n<<"));
	}

	@Test
	public void testSecurityManager() throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		System.out.println("It should be dumped normally.");
		System.out.println(dumper(new SampleBean()));

		Map<Object, Object> srcMap = getSrcMap(ObjDumper4j.primitiveMap);

		final SecurityManager sm = System.getSecurityManager();
		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkPermission(Permission perm) {
				if (perm instanceof ReflectPermission) {
					throw new SecurityException("I hate reflection!");
				}
				if (sm != null)
					sm.checkPermission(perm);
			}
		});
		try {
			// not public field.
			System.out.println("Security Exception should be dumped.");
			System.out.println(dumper(new SampleBean()));

			// not public method specified by primitiveMap.
			srcMap.put(BigDecimal.class.getName(), "audit");
			System.out.println("Security Exception should be dumped.");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("a", 100);
			map.put("b", 200);
			map.put("c", new BigDecimal(1000));
			map.put("d", true);
			System.out.println(dumper(map));
			srcMap.put(BigDecimal.class.getName(), null);

		} finally {
			System.setSecurityManager(sm);
		}

		System.setSecurityManager(new SecurityManager() {

			@Override
			public void checkMemberAccess(Class<?> clazz, int which) {
				if (which == Member.DECLARED)
					throw new SecurityException("I don't like refrection!!");
			}

			@Override
			public void checkPermission(Permission perm) {
				if (sm != null)
					sm.checkPermission(perm);
			}
		});
		try {
			// all fields
			System.out.println("Security Exception should be dumped.");
			System.out.println(dumper(new SampleBean()));

			// Integer value
			srcMap.put(Integer.class.getName(), "floatValue");
			System.out.println("Security Exception should be dumped.");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("a", 100);
			map.put("b", 200);
			map.put("c", new BigDecimal(1000));
			map.put("d", true);
			System.out.println(dumper(map));
			srcMap.put(Integer.class.getName(), null);

		} finally {
			System.setSecurityManager(null);
		}

	}

	@SuppressWarnings("unchecked")
	private Map<Object, Object> getSrcMap(Map<?, ?> umap)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = umap.getClass().getDeclaredField("m");
		f.setAccessible(true);
		return (Map<Object, Object>) f.get(umap);
	}

	@Test
	public void testSubClass() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SubBean();
		System.out.println(dumper(bean));

		// add header
		System.out.println(dumper("<<Header>>\n", bean));

		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("aloha", "hawaii");
				put("hello", "england");
				put("konnichiwa", bean);
			}
		};
		System.out.println(dumper("<<Header>>\n", map));

		// referenced by another bean
		bean.obj = new SampleBean();
		System.out.println(dumper("<<Header>>\n", map));

		// referenced by itself.
		bean.obj = bean;
		System.out.println(dumper("<<Header>>\n", map));

		// hooter adding.
		bean.obj = bean;
		System.out.println(dumper("<<Header>>\n", map, "\n<<Hooter>>"));
	}

	@Test
	public void testStack() {
		Map<String, SampleBean> map = new HashMap<String, SampleBean>();
		SampleBean bean = new SampleBean();
		map.put("key1", bean);
		map.put("key2", bean);
		map.put("key3", bean);
		System.out.println(dumper(map));

		bean.obj = bean;
		System.out.println(dumper(map));
	}

	@Test
	public void testInspect() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SampleBean();

		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("aloha", "hawaii");
				put("hello", "england");
				put("konnichiwa", bean);
			}
		};

		// hooter adding.
		bean.obj = bean;
		System.out.println(inspect("<<Header>>", map, bean, "<<Hooter>>"));

	}

	@Test
	public void testInspecter() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SampleBean();
		System.out.println(inspecter(bean));

		// add header
		System.out.println(inspecter("<<Header>>\n", bean));

		@SuppressWarnings("serial")
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("aloha", "hawaii");
				put("hello", "england");
				put("konnichiwa", bean);
			}
		};
		System.out.println(inspecter("<<Header>>\n", map));

		// referenced by another bean
		bean.obj = new SampleBean();
		System.out.println(inspecter("<<Header>>\n", map));

		// referenced by itself.
		bean.obj = bean;
		System.out.println(inspecter("<<Header>>\n", map));

		// hooter adding.
		bean.obj = bean;
		System.out.println(inspecter("<<Header>>\n", map, "\n<<Hooter>>"));

		// Real Object instance.
		bean.obj = new Object();
		System.out.println(inspecter("<<Header>>", map, bean, "<<Hooter>>"));

		// inspect Integer
		System.out.println(inspecter(100));

		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("1", new Integer(1));
		m.put("2L", new Long(2));
		Map<String, Object> umap = Collections.unmodifiableMap(m);
		System.out.println(inspecter(umap));
		m.put("", new Object());
		System.out.println(umap.get(""));
		System.out.println(inspecter(umap));

		System.out.println(inspecter("foo"));
	}

	@Test
	public void testError() {

		// referenced by itself.(list)
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(list);
		System.out.println(dumper("Success!\n", list).superSafe().toString());
		System.out.println(dumper("StackOverFlow!\n", list).safe().toString());
		System.out
				.println(dumper("StackOverFlow!\n", list).normal().toString());
		System.out.println(dumper("StackOverFlow!\n", list).rapid().toString());
		System.out.println(dumper("StackOverFlow!\n", list).superRapid()
				.toString());

		// referenced by itself.(bean)
		SampleBean bean = new SampleBean();
		bean.obj = bean;
		System.out.println(dumper("Success!\n", bean).superSafe().toString());
		System.out.println(dumper("Success!\n", bean).safe().toString());
		System.out.println(dumper("Success!\n", bean).normal().toString());
		System.out.println(dumper("Success!\n", bean).rapid().toString());
		// try {
		// dumper("OutOfMemory!\n", bean).superRapid().toString();
		// fail();
		// } catch (AssertionError ae) {
		// throw ae;
		// } catch (Throwable t) {
		// t.printStackTrace();
		// }
	}

	@Test
	public void testPerformance() {

		HashMap<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < 1000; i++) {
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("tako" + i, "octopus");
			map1.put("ika" + i, "squid");
			map1.put("namako" + i, "sea cucumber");
			map.put("No." + i + "-1", map1);

			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("octopus" + i, "tako");
			map2.put("squid" + i, "ika");
			map2.put("sea cucumber" + i, "namako");
			map.put("No." + i + "-2", map2);

			map.put("No." + i + "-3", map2);

			map.put("No." + i + "-4", new SampleBean());
		}

		StopWatch sw = new StopWatch();

		sw.start("superSafe");
		dumper(map).superSafe().toString();
		sw.end("superSafe");

		sw.start("safe");
		dumper(map).safe().toString();
		sw.end("safe");

		sw.start("normal");
		dumper(map).normal().toString();
		sw.end("normal");

		sw.start("rapid");
		dumper(map).rapid().toString();
		sw.end("rapid");

		sw.start("superRapid");
		dumper(map).superRapid().toString();
		sw.end("superRapid");

		System.out.println(sw);
	}

	@SuppressWarnings("unused")
	class SampleBean {
		public int intValue = 10;
		public Integer IntgerValue = 10;
		public BigDecimal bigDecimalValue = new BigDecimal(10.81235);
		public boolean booleanValue = true;
		public Boolean BooleanValue = false;
		public char charValue = 'x';
		public Character CharacterValue = 'z';
		public String strValue = "Hanako";
		public String[] strAry = { "tako", "ika", "namako" };
		public List<String> strList = Arrays.asList("octopus", "squid",
				"sea cucumber");
		@SuppressWarnings("serial")
		public Map<String, String> strMap = new HashMap<String, String>() {
			{
				put("a", "1");
				put("b", "2");
			}
		};
		private String[] emptyAry = new String[0];
		private List<String> emptyList = new ArrayList<String>();
		private Map<String, String> emptyMap = new HashMap<String, String>();
		private double aaa = 10.5;
		Object obj;
	}

	class SubBean extends SampleBean {
		String subField = "Are the fields of superclass displayed?";
	}
}
