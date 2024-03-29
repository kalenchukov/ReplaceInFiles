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

package dev.kalenchukov.replaceinfiles.modules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * Класс работы с файлами.
 *
 * @author Алексей Каленчуков
 */
public class FileExpert implements FileExperts
{
	/**
	 * Локализация.
	 */
	@NotNull
	private Locale locale;

	/**
	 * Распространение действия на скрытые файлы.
	 */
	private boolean canHidden;

	/**
	 * Локализованные тексты логирования.
	 */
	@NotNull
	private ResourceBundle localeLogs;

	/**
	 * Локализованные тексты исключений.
	 */
	@NotNull
	private ResourceBundle localeExceptions;

	/**
	 * Логгер для данного класса.
	 */
	@NotNull
	private static final Logger LOG = LogManager.getLogger(FileExpert.class);

	/**
	 * Конструктор для {@code FileExpert}.
	 */
	public FileExpert()
	{
		this.locale = new Locale("ru", "RU");
		this.canHidden = false;
		this.localeLogs = ResourceBundle.getBundle(
			"replaceinfiles/localizations/logs",
			this.locale
		);
		this.localeExceptions = ResourceBundle.getBundle(
			"replaceinfiles/localizations/exceptions",
			this.locale
		);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param locale {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NullPointerException если в качестве {@code locale} передан {@code null}.
	 */
	@NotNull
	@Override
	public FileExpert setLocale(@NotNull final Locale locale)
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

		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean canHidden()
	{
		return this.canHidden;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param canHidden {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	@NotNull
	public FileExperts canHidden(final boolean canHidden)
	{
		this.canHidden = canHidden;

		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param file {@inheritDoc}
	 * @param value {@inheritDoc}
	 * @throws NullPointerException если в качестве {@code file} передан {@code null}.
	 * @throws NullPointerException если в качестве {@code value} передан {@code null}.
	 */
	@Override
	public void writeFile(@NotNull final File file, @NotNull final String value)
	{
		Objects.requireNonNull(file);
		Objects.requireNonNull(value);

		try (FileOutputStream fileOutputStream = new FileOutputStream(file))
		{
			fileOutputStream.write(value.getBytes());

			LOG.debug(String.format(
				this.localeLogs.getString("00006"),
				file.getPath()
			));
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param file {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NullPointerException если в качестве {@code file} передан {@code null}.
	 */
	@Override
	@NotNull
	public String readFile(@NotNull final File file)
	{
		Objects.requireNonNull(file);

		String value = "";

		try (FileInputStream fileInputStream = new FileInputStream(file))
		{
			value = new String(fileInputStream.readAllBytes());

			LOG.debug(String.format(
				this.localeLogs.getString("00007"),
				file.getPath()
			));
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}

		return value;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param file {@inheritDoc}
	 */
	@Override
	public void cleanFile(@NotNull final File file)
	{
		this.writeFile(file, "");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param directory {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NullPointerException если в качестве {@code directory} передан {@code null}.
	 */
	@Override
	@NotNull
	public List<File> scanDirectory(@NotNull final File directory)
	{
		Objects.requireNonNull(directory);

		List<File> foundFiles = new ArrayList<>();

		try
		{
			if (directory.isHidden() && !canHidden) {
				LOG.debug(String.format(
					this.localeLogs.getString("00009"),
					directory.getPath()
				));

				return foundFiles;
			}

			if (!directory.canRead()) {
				LOG.debug(String.format(
					this.localeLogs.getString("00004"),
					directory.getPath()
				));

				return foundFiles;
			}

			File[] files = Objects.requireNonNull(directory.listFiles());

			for (File file : files)
			{
				if (file.isHidden() && !canHidden) {
					LOG.debug(String.format(
						this.localeLogs.getString("00008"),
						file
					));

					continue;
				}

				if (!file.canRead()) {
					LOG.debug(String.format(
						this.localeLogs.getString("00004"),
						file
					));

					continue;
				}

				if (file.isDirectory()) {
					foundFiles.addAll(this.scanDirectory(file));
				}
				else {
					LOG.debug(String.format(
						this.localeLogs.getString("00005"),
						file.getPath()
					));

					foundFiles.add(file);
				}
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}

		return foundFiles;
	}
}
