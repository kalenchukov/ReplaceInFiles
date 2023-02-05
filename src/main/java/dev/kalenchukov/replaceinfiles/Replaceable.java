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
	 * @param locale локализация.
	 * @return ссылку на данный объект.
	 */
	@NotNull
	Replacement setLocale(@NotNull Locale locale);

	/**
	 * Распространение действия на скрытые файлы.
	 *
	 * @param canHidden {@code true}, если действие на скрытые файлы необходимо, иначе {@code false}.
	 * @return ссылку на данный объект.
	 */
	@NotNull
	Replacement canHidden(boolean canHidden);

	/**
	 * Добавляет файл в котором будет производиться замена.
	 *
	 * @param file файл.
	 * @return ссылку на данный объект.
	 * @throws FileNotFoundException если файла не существует.
	 */
	@NotNull
	Replacement addFile(@NotNull File file) throws FileNotFoundException;

	/**
	 * Применяет правило для замены с указанием строки замены.
	 *
	 * @param regexp шаблон регулярного выражения.
	 * @param replacement строка замены.
	 * @return ссылку на данный объект.
	 */
	@NotNull
	Replaceable apply(@NotNull String regexp, @NotNull String replacement);

	/**
	 * Применяет правило для замены с указанием специального правила.
	 *
	 * @param regexp шаблон регулярного выражения.
	 * @param specialRule специальное правило.
	 * @return ссылку на данный объект.
	 */
	@NotNull
	Replaceable apply(@NotNull String regexp, @NotNull SpecialRule specialRule);

	/**
	 * Выполняет все применённые правила для замены.
	 */
	void replace();
}
