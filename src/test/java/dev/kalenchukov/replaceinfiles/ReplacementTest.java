/*
 * Copyright © 2022-2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.kalenchukov.replaceinfiles;

import dev.kalenchukov.replaceinfiles.modules.FileExpert;
import dev.kalenchukov.replaceinfiles.modules.FileExperts;
import dev.kalenchukov.replaceinfiles.resources.SpecialRule;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.*;

public class ReplacementTest
{
	@ClassRule
	public static TemporaryFolder tempDir = new TemporaryFolder();

	public static FileExperts fileExpert = new FileExpert();

	public static File file;

	public static  Replaceable replacement;

	@BeforeClass
	public static void beforeClass() throws Exception
	{
		file = tempDir.newFile("myfile.txt");
	}

	@Before
	public void setUp() throws Exception
	{
		replacement = new Replacement();
		replacement.addFile(file);

		fileExpert.writeFile(file, "text");
	}

	@Test
	public void testReplace1()
	{
		replacement.apply("text", SpecialRule.FILE_NAME)
				   .replace();

		assertEquals("myfile.txt", fileExpert.readFile(file));
	}

	@Test
	public void testReplace2()
	{
		replacement.apply("text", SpecialRule.PATH)
				   .replace();

		assertEquals(file.getPath(), fileExpert.readFile(file));
	}

	@Test
	public void testReplace3()
	{
		replacement.apply("text", "string")
				   .replace();

		assertEquals("string", fileExpert.readFile(file));
	}

	@Test
	public void testReplace4()
	{
		replacement.apply("x", "X")
				   .replace();

		assertEquals("teXt", fileExpert.readFile(file));
	}

	@After
	public void tearDown() throws Exception
	{
		fileExpert.cleanFile(file);
	}

	@AfterClass
	public static void afterClass() throws Exception
	{
		tempDir.delete();
	}
}