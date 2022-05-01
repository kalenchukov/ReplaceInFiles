/*
 * Copyright © 2022 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.replaceinfiles;

import dev.kalenchukov.replaceinfiles.modules.FileExpert;
import dev.kalenchukov.replaceinfiles.resources.SpecialRule;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * Класс замены символов в файлах.
 */
public class Replacement implements Replaceable
{
	/**
	 * Локаль.
	 * Для изменения локали необходимо использовать {@link #setLocale(Locale)}.
	 */
	@NotNull
	private Locale locale = new Locale("ru", "RU");

	/**
	 * Устанавливает логгер для данного класса.
	 */
	@NotNull
	private static final Logger LOG = Logger.getLogger(Replacement.class);

	/**
	 * Устанавливает тексты локализации для текстов логирования.
	 */
	@NotNull
	private ResourceBundle localeLogs = ResourceBundle.getBundle("localizations/logs", this.locale);

	/**
	 * Устанавливает тексты локализации для текстов исключений.
	 */
	@NotNull
	private ResourceBundle localeExceptions = ResourceBundle.getBundle("localizations/exceptions", this.locale);

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

			this.localeLogs = ResourceBundle.getBundle("localizations/logs", this.locale);
			this.localeExceptions = ResourceBundle.getBundle("localizations/exceptions", this.locale);
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

				value = value.replaceAll(replace.getKey(), replacement);
			}

			LOG.debug(String.format(
				this.localeLogs.getString("00003"),
				file.getPath()
			));

			this.fileExpert.writeFile(file, value);
		}
	}
}
