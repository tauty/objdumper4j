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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ObjDumper4jTest2 {

	@Test
	public void initIndent() {
		System.out
				.println("--------------------------------------------------");

		final SampleBean bean = new SampleBean();

		System.out.println(dumper(bean).initIndent("    "));

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
		System.out.println(dumper("<<Header>>", map, bean, "<<Hooter>>")
				.initIndent("\t\t").delimiter("\n\n"));

	}

	@Test
	public void showStatic() {
		System.out
				.println("--------------------------------------------------");
		final SampleBean bean = new SampleBean();
		System.out.println(dumper(bean).showStatic());
	}

	@Test
	public void indent() {
		System.out
				.println("--------------------------------------------------");
		final SampleBean bean = new SampleBean();
		System.out.println(dumper(bean).indent("   "));
		System.out.println(dumper(bean).indent("=="));
		System.out.println(dumper(bean).indent("==").initIndent("****"));
	}

	@Test
	public void doSort() {
		System.out.println(dumper("--- Not Sorted ---\n", new SampleBean()));
		System.out.println(dumper("---   sorted   ---\n", new SampleBean())
				.doSort());
		System.out.println(dumper("---   primitiveFirst   ---\n",
				new SampleBean()).primitiveFirst());
		System.out.println(dumper("---   primitiveFirst & doSort   ---\n",
				new SampleBean()).primitiveFirst().doSort());
	}

	@SuppressWarnings("unused")
	static class SampleBean {
		static public int staticValue = 10000;
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
				put("e", "2");
				put("a", "1");
				put("b", "2");
			}
		};
		private String[] emptyAry = new String[0];
		@SuppressWarnings("serial")
		public Map<Object, Object> objMap = new HashMap<Object, Object>() {
			{
				put(100, 20);
				put(99, 2);
				put("9", 999999999);
				put("0", 0);
				put("3", 999);
			}
		};
		private List<String> emptyList = new ArrayList<String>();
		private Map<String, String> emptyMap = new HashMap<String, String>();
		private double aaa = 10.5;
		Object obj;
	}

	class SubBean extends SampleBean {
		String subField = "Are the fields of superclass displayed?";
	}
}
