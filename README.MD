# Справка
## ReplaceInFiles
Поиск и замена текста в файлах.

## Использование
Содержимое файла 'myfile.txt' до замены:

> Текст в файле.

Использование заменителя:
```java
Replaceable replacement = new Replacement();
replacement.addFile(new File("/home/login/Project/myfile.txt"));
replacement.apply("а", "1");
replacement.apply("в", "0");
replacement.replace();
```

Содержимое файла 'myfile.txt' после замены:

> Текст 0 ф1йле.

#### Специальные правила
| Специальный символ |       SpecialRule       |              Действие              |
|:------------------:|:-----------------------:|:----------------------------------:|
|    \\SCFileName    |  SpecialRule.FILE_NAME  |    Замена значения на имя файла    |
|      \\SCPath      |    SpecialRule.PATH     |  Замена значения на путь до файла  |

#### Использование специальных правил
Содержимое файла 'myfile.txt' до замены:

> Текст в файле.

Использование заменителя:
```java
Replaceable replacement = new Replacement();
replacement.addFile(new File("/home/login/Project/myfile.txt"));
replacement.apply("Текст", SpecialRule.FILE_NAME);
replacement.apply("файле", SpecialRule.PATH);
replacement.replace();
```

Содержимое файла 'myfile.txt' после замены:

> myfile.txt в /home/login/Project/myfile.txt.
