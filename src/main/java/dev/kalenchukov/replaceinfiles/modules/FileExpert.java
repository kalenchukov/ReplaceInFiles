/*
 * Copyright © 2022 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.replaceinfiles.modules;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * Класс работы с файлами.
 */
public class FileExpert implements FileExperts
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
	private static final Logger LOG = Logger.getLogger(FileExpert.class);

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
	 * Распространение действия на скрытые файлы.
	 */
	private boolean canHidden = false;


	/**
	 * @see FileExpert#setLocale(Locale)
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
	 * @see FileExperts#canHidden()
	 */
	@Override
	public boolean canHidden()
	{
		return this.canHidden;
	}

	/**
	 * @see FileExperts#canHidden(boolean)
	 */
	@Override
	@NotNull
	public FileExperts canHidden(final boolean canHidden)
	{
		this.canHidden = canHidden;

		return this;
	}

	/**
	 * @see FileExperts#writeFile(File, String)
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
	 * @see FileExperts#readFile(File)
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
	 * @see FileExperts#cleanFile(File)
	 */
	@Override
	public void cleanFile(@NotNull final File file)
	{
		this.writeFile(file, "");
	}

	/**
	 * @see FileExperts#scanDirectory(File)
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
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		return foundFiles;
	}
}
