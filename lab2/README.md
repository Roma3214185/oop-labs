# Лабораторна робота 2 — Лікарські препарати (XML / XSD / XSL)

Каталог лікарських препаратів у XML, схема XSD для нього, розбір
документа парсерами DOM, SAX і StAX, сортування через `Comparator`,
валідація за XSD і XSL-перетворення, що розбиває препарати за групами.

## Як це працює

Користувач нічого не вводить: `main()` запускає `MedicineApplication.run()`,
яка проходить кроки по черзі. Усі повідомлення йдуть через Log4j2.

1. **Валідація.** `XmlValidator` завантажує `medicines.xsd` і перевіряє
   `medicines.xml`. Якщо файл невалідний — у лог пишеться рядок, колонка й
   причина, програма зупиняється.
2. **Парсинг.** Документ читається трьома парсерами: `DomDrugParser`,
   `SaxDrugParser`, `StaxDrugParser`. Кожен повертає `List<Drug>`.
3. **Порівняння.** Три списки мають бути однаковими (`equals`), інакше —
   помилка.
4. **Сортування.** Препарати виводяться в лог, відсортовані трьома
   компараторами: за назвою, за групою, за найнижчою ціною.
5. **XSL.** `medicines-by-group.xsl` групує препарати за `group`, результат
   записується в `output/medicines-by-group.xml`.

| Що | Де |
|----|----|
| хід роботи й відсортовані списки | консоль (INFO) |
| те саме + повні об'єкти | `logs/medicine.log` (DEBUG) |
| XML, розбитий за групами | `output/medicines-by-group.xml` |

## Файли XML (`src/main/resources/xml/`)

| Файл | Призначення |
|------|-------------|
| [medicines.xml](src/main/resources/xml/medicines.xml) | 6 препаратів, кореневий елемент `Medicine` |
| [medicines.xsd](src/main/resources/xml/medicines.xsd) | схема документа |
| [medicines-by-group.xsl](src/main/resources/xml/medicines-by-group.xsl) | групування за групою препарату (XSLT 1.0, Мюнхенський метод) |

Файли лежать у ресурсах, тому програма знаходить їх з будь-якої робочої
папки (IDE, `java -jar`).

### Що є в XSD

| Вимога | Як зроблено |
|--------|-------------|
| прості типи | `TitleType`, `PriceType`, `QuantityType`, `AmountType`, `PerDayType`, `CertificateNumberType` |
| комплексні типи | `MedicineType`, `DrugType`, `VersionType`, `ProducerType`, `CertificateType`, `PackageType`, `DosageType`… |
| переліки | група, форма випуску, тип упаковки, **одиниця дозування** (`MG`, `MCG`, `G`, `ML`) |
| граничні значення | ціна > 0 і ≤ 100000 (2 знаки), кількість 1..1000, доза > 0 і ≤ 5000, прийомів 1..12, довжина назв 2..100, шаблон номера свідоцтва `UA/1234/01` |
| обов'язкові атрибути | `use="required"` у `drug`, `version`, `producer`, `certificate`, `package`, `dosage` |
| тип ID | `drug/@id` має тип `xs:ID`; додатково `xs:key` на `id` і `xs:unique` на назву — **дублікати не пройдуть валідацію** |
| порядок елементів | у `drug` і `producer` — `xs:all`, тобто дочірні елементи можна писати в будь-якому порядку; препарати в корені теж у будь-якому порядку |

## Структура пакетів

| Пакет | Призначення |
|-------|-------------|
| `ua.medicine` | `MedicineApplication` — точка входу; `XmlException` — помилка роботи з XML |
| `ua.medicine.model` | класи за схемою: `Drug`, `Version`, `Producer`, записи `Certificate`, `DrugPackage`, `Dosage`; переліки `DrugGroup`, `DrugForm`, `PackageType`, `DosageUnit` |
| `ua.medicine.parser` | абстрактний `DrugParser` і нащадки `DomDrugParser`, `SaxDrugParser`, `StaxDrugParser`; `DrugBuilder` — єдине місце, де теги перетворюються на об'єкти |
| `ua.medicine.comparator` | `DrugNameComparator`, `DrugGroupComparator`, `DrugPriceComparator` — реалізації `Comparator<Drug>` |
| `ua.medicine.validator` | `XmlValidator` — перевірка XML за XSD |
| `ua.medicine.transformer` | `XslTransformer` — XSL-перетворення у файл |

### Три парсери без дублювання коду

| Парсер | Як читає |
|--------|----------|
| DOM | весь документ у дерево в пам'яті, потім рекурсивний обхід |
| SAX | потоком; парсер сам викликає `startElement` / `characters` / `endElement` (push) |
| StAX | потоком; ми самі в циклі просимо `reader.next()` (pull) |

Усі три лише знаходять теги й передають їх у `DrugBuilder`
(`startElement(tag, attributes)` / `endElement(tag, text)`). Як з тегу
зробити об'єкт, написано один раз — у `DrugBuilder`. Спільна обробка
помилок і логування — у базовому класі `DrugParser`.

## Запуск

```bash
mvn clean package
java -jar target/medicine-xml-1.0.0-jar-with-dependencies.jar
```

## Тести

```bash
mvn clean verify   # JUnit 5 + Mockito, звіт JaCoCo: target/site/jacoco/index.html
```
