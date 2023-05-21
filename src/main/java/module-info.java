/**
 * Определяет API для поиска и замены текста в файлах.
 */
module dev.kalenchukov.replaceinfiles
{
	requires org.jetbrains.annotations;
	requires org.apache.logging.log4j;

	exports dev.kalenchukov.replaceinfiles;
	exports dev.kalenchukov.replaceinfiles.resources;
	exports dev.kalenchukov.replaceinfiles.modules;
}