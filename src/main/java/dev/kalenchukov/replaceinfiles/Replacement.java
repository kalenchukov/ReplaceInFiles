/*
 * Copyright © 2022 Алексей Каленчуков
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

import dev.kalenchukov.notation.converting.NotationConverter;
import dev.kalenchukov.replaceinfiles.modules.FileExpert;
import dev.kalenchukov.replaceinfiles.resources.SpecialRule;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс замены символов в файлах.
 */
public class Replacement implements Replaceable
{
	/**
	 * Локализация.
	 */
	@NotNull
	private Locale locale = new Locale("ru", "RU");

	/**
	 * Логгер для данного класса.
	 */
	@NotNull
	private static final Logger LOG = Logger.getLogger(Replacement.class);

	/**
	 * Локализованные тексты логирования.
	 */
	@NotNull
	private ResourceBundle localeLogs = ResourceBundle.getBundle(
		"replaceinfiles/localizations/logs",
		this.locale
	);

	/**
	 * Локализованные тексты исключений.
	 */
	@NotNull
	private ResourceBundle localeExceptions = ResourceBundle.getBundle(
		"replaceinfiles/localizations/exceptions",
		this.locale
	);

	/**
	 * Объект для работы с файлами.
	 */
	@NotNull
	private final FileExpert fileExpert = new FileExpert().setLocale(this.locale);

	/**
	 * Коллекция файлов для замены.
	 */
	@NotNull
	private final List<@NotNull File> files = new ArrayList<>();

	/**
	 * Коллекция правил замены.
	 */
	@NotNull
	private final Map<@NotNull String, @NotNull String> replacing = new LinkedHashMap<>();

	/**
	 * @see Replaceable#setLocale(Locale)
	 */
	@NotNull
	@Override
	public Replacement setLocale(@NotNull final Locale locale)
	{
		Objects.requireNonNull(locale);

		if (!this.locale.equals(locale))
		{
			this.locale = locale;

			this.localeLogs = ResourceBundle.getBundle(
				"replaceinfiles/localizations/logs",
				this.locale
			);

			this.localeExceptions = ResourceBundle.getBundle(
				"replaceinfiles/localizations/exceptions",
				this.locale
			);
		}

		this.fileExpert.setLocale(this.locale);

		return this;
	}

	/**
	 * @see Replaceable#canHidden(boolean)
	 */
	@NotNull
	@Override
	public Replacement canHidden(final boolean canHidden)
	{
		this.fileExpert.canHidden(canHidden);

		return this;
	}

	/**
	 * @see Replaceable#addFile(File)
	 */
	@NotNull
	@Override
	public Replacement addFile(@NotNull final File file) throws FileNotFoundException
	{
		Objects.requireNonNull(file);

		if (!file.exists()) {
			throw new FileNotFoundException(String.format(
				this.localeExceptions.getString("40001"),
				file.getPath()
			));
		}

		if (file.isFile()) {
			this.files.add(file);
		}
		else {
			this.files.addAll(this.fileExpert.scanDirectory(file));
		}

		LOG.debug(String.format(
			this.localeLogs.getString("00001"),
			file.getPath()
		));

		return this;
	}

	/**
	 * @see Replaceable#apply(String, String)
	 */
	@NotNull
	@Override
	public Replacement apply(@NotNull final String regexp, @NotNull final String replacement)
	{
		Objects.requireNonNull(regexp);
		Objects.requireNonNull(replacement);

		this.replacing.put(regexp, replacement);

		LOG.debug(String.format(
			this.localeLogs.getString("00010"),
			regexp,
			replacement
		));

		return this;
	}

	/**
	 * @see Replaceable#apply(String, SpecialRule)
	 */
	@NotNull
	@Override
	public Replacement apply(@NotNull final String regexp, @NotNull final SpecialRule specialRule)
	{
		Objects.requireNonNull(regexp);
		Objects.requireNonNull(specialRule);

		return this.apply(regexp, specialRule.getSpecialSign());
	}

	/**
	 * @see Replaceable#replace()
	 */
	@Override
	public void replace()
	{
		FileExpert fileText = new FileExpert().setLocale(this.locale);
		String valueText = fileText.readFile(new File("/home/sadmin/Projects/methods.conf"));

		for (File file : this.files)
		{
			String value = this.fileExpert.readFile(file);

			for (Map.Entry<String, String> replace : this.replacing.entrySet())
			{

				String replacement = replace.getValue();

				if (replace.getValue().equals(SpecialRule.FILE_NAME.getSpecialSign())) {
					replacement = file.getName();
				}

				if (replace.getValue().equals(SpecialRule.PATH.getSpecialSign())) {
					replacement = file.getPath();
				}

				final Pattern pattern = Pattern.compile("(?<digit>7[0-9]{4})");
				final Matcher matcher = pattern.matcher(value);

				String group = "2343465677898798";

				while (matcher.find()) {
					group = matcher.group("digit");
				}

				System.out.println(group);

				value = value.replaceAll(replace.getKey(), NotationConverter.toSnakeCase(replacement));
				valueText = valueText.replaceAll(group, NotationConverter.toSnakeCase(replacement));

			}

			LOG.debug(String.format(
				this.localeLogs.getString("00003"),
				file.getPath()
			));

			this.fileExpert.writeFile(file, value);
			fileText.writeFile(new File("/home/sadmin/Projects/methods.conf"), valueText);
		}
	}
}
