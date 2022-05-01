/*
 * Copyright © 2022 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
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