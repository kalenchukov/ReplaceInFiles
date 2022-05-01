/*
 * Copyright © 2022 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.replaceinfiles.resources;

import org.jetbrains.annotations.NotNull;

/**
 * Перечисление специальных правил замены.
 */
public enum SpecialRule
{
	/**
	 * Имя файла.
	 */
	FILE_NAME("\\SCFileName"),

	/**
	 * Путь файла.
	 */
	PATH("\\SCPath");

	/**
	 * Специальный знак правила.
	 */
	private final String specialSign;

	/**
	 * Конструктор для {@code SpecialRule}.
	 *
	 * @param specialSign Специальный знак правила
	 */
	SpecialRule(@NotNull final String specialSign)
	{
		this.specialSign = specialSign;
	}

	/**
	 * Возвращает специальный знак правила.
	 *
	 * @return Знак правила.
	 */
	@NotNull
	public String getSpecialSign()
	{
		return this.specialSign;
	}
}
