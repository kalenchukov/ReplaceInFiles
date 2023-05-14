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

import dev.kalenchukov.replaceinfiles.resources.SpecialRule;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс проверки методов класса {@link Replacement}.
 *
 * @author Алексей Каленчуков
 */
public class ReplacementTest
{
	public static final String TEMP_FILE_NAME = "tempFile";

	public static File tempFile;

	public static Replaceable replacement;

	@BeforeAll
	public static void beforeAll(@TempDir File tempDir)
	{
		tempFile = new File(tempDir, TEMP_FILE_NAME);
	}

	@BeforeEach
	public void beforeEach() throws Exception
	{
		ReplacementTest.writeFile("text");

		replacement = new Replacement();
		replacement.addFile(tempFile);
	}

	/**
	 * Проверка метода {@link Replacement#apply(String, String)}.
	 */
	@Test
	public void testReplaceString()
	{
		replacement.apply("text", "string")
				   .replace();

		assertEquals("string", ReplacementTest.readFile());
	}

	/**
	 * Проверка метода {@link Replacement#apply(String, SpecialRule)} со специальным
	 * правилом {@link SpecialRule#FILE_NAME}.
	 */
	@Test
	public void testReplaceSpecialRuleFileName()
	{
		replacement.apply("text", SpecialRule.FILE_NAME)
				   .replace();

		assertEquals(TEMP_FILE_NAME, ReplacementTest.readFile());
	}

	/**
	 * Проверка метода {@link Replacement#apply(String, SpecialRule)} со специальным
	 * правилом {@link SpecialRule#PATH}.
	 */
	@Test
	public void testReplaceSpecialRulePath()
	{
		replacement.apply("text", SpecialRule.PATH)
				   .replace();

		assertEquals(tempFile.getPath(), ReplacementTest.readFile());
	}

	private static String readFile()
	{
		String value;
		Path path = Paths.get(tempFile.toURI());

		try {
			value = Files.readAllLines(path, StandardCharsets.UTF_8).get(0);
		}
		catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		return value;
	}

	private static void writeFile(final String value)
	{
		Path path = Paths.get(tempFile.toURI());

		try {
			Files.write(path, value.getBytes());
		}
		catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}