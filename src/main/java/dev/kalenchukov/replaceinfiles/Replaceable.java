/*
 * Copyright © 2022 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.replaceinfiles;

import dev.kalenchukov.replaceinfiles.resources.SpecialRule;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

/**
 * Интерфейс для реализации замены символов в файлах.
 */
public interface Replaceable
{
	/**
	 * Устанавливает локализацию.
	 *
	 * @param locale Локаль.
	 * @return Ссылку на данный объект.
	 */
	@NotNull
	Replacement setLocale(@NotNull Locale locale);

	/**
	 * Распространение действия на скрытые файлы.
	 *
	 * @param canHidden {@code True}, если действие на скрытые файлы необходимо, иначе {@code false}.
	 * @return Ссылку на данный объект.
	 */
	@NotNull
	Replacement canHidden(final boolean canHidden);

	/**
	 * Добавляет файл в котором будет производиться замена.
	 *
	 * @param file Файл.
	 * @return Ссылку на данный объект.
	 * @throws FileNotFoundException Если файла не существует.
	 */
	@NotNull
	Replacement addFile(@NotNull File file) throws FileNotFoundException;

	/**
	 * Применяет правило для замены с указанием строки замены.
	 *
	 * @param regexp Шаблон регулярного выражения.
	 * @param replacement Строка замены.
	 * @return Ссылку на данный объект.
	 */
	@NotNull
	Replaceable apply(@NotNull String regexp, @NotNull String replacement);

	/**
	 * Применяет правило для замены с указанием специального правила.
	 *
	 * @param regexp Шаблон регулярного выражения.
	 * @param specialRule Специальное правило.
	 * @return Ссылку на данный объект.
	 */
	@NotNull
	Replaceable apply(@NotNull String regexp, @NotNull SpecialRule specialRule);

	/**
	 * Выполняет все применённые правила для замены.
	 */
	void replace();
}
