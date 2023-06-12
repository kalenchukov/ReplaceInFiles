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
	private static final String TEMP_FILE_NAME = "tempFile";

	private static File tempFile;

	private static Replaceable replacement;

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
	public void replaceString() throws IOException
	{
		replacement.apply("text", "string")
				   .replace();

		String actualString = ReplacementTest.readFile();

		assertEquals("string", actualString);
	}

	/**
	 * Проверка метода {@link Replacement#apply(String, SpecialRule)} со специальным
	 * правилом {@link SpecialRule#FILE_NAME}.
	 */
	@Test
	public void replaceSpecialRuleFileName() throws IOException
	{
		replacement.apply("text", SpecialRule.FILE_NAME)
				   .replace();

		String actualString = ReplacementTest.readFile();

		assertEquals(TEMP_FILE_NAME, actualString);
	}

	/**
	 * Проверка метода {@link Replacement#apply(String, SpecialRule)} со специальным
	 * правилом {@link SpecialRule#PATH}.
	 */
	@Test
	public void replaceSpecialRulePath() throws IOException
	{
		replacement.apply("text", SpecialRule.PATH)
				   .replace();

		String actualString = ReplacementTest.readFile();

		assertEquals(tempFile.getPath(), actualString);
	}

	private static String readFile() throws IOException
	{
		String value;
		Path path = Paths.get(tempFile.toURI());

		value = Files.readAllLines(path, StandardCharsets.UTF_8).get(0);

		return value;
	}

	private static void writeFile(final String value) throws IOException
	{
		Path path = Paths.get(tempFile.toURI());

		Files.write(path, value.getBytes());
	}
}