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

package dev.kalenchukov.replaceinfiles.modules;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Интерфейс для реализации работы с файлами.
 */
public interface FileExperts
{
	/**
	 * Устанавливает локализацию.
	 *
	 * @param locale Локализация.
	 * @return Ссылку на данный объект.
	 */
	@NotNull
	FileExperts setLocale(@NotNull Locale locale);

	/**
	 * Возвращает установленное распространение действия на скрытые файлы.
	 *
	 * @return {@code True}, если действие на скрытые файлы распространяется, иначе {@code false}.
	 */
	boolean canHidden();

	/**
	 * Устанавливает распространение действия на скрытые файлы.
	 *
	 * @param canHidden {@code True}, если действие на скрытые файлы необходимо, иначе {@code false}.
	 * @return Ссылку на данный объект.
	 */
	@NotNull
	FileExperts canHidden(final boolean canHidden);

	/**
	 * Записывает файл.
	 *
	 * @param file Файл.
	 * @param value Строка которую необходимо записать в файл.
	 */
	void writeFile(@NotNull final File file, @NotNull final String value);

	/**
	 * Считывает файл.
	 *
	 * @param file Файл.
	 * @return Содержимое файла в виде строки.
	 */
	@NotNull
	String readFile(@NotNull final File file);

	/**
	 * Очищает файл.
	 *
	 * @param file Файл.
	 */
	void cleanFile(@NotNull final File file);

	/**
	 * Сканирует директорию на наличие файлов.
	 *
	 * @param directory Директория.
	 * @return Коллекцию файлов из указанной директории.
	 */
	@NotNull
	List<File> scanDirectory(@NotNull final File directory);
}
