# Лабораторна робота 2 — Лікарські препарати (XML / XSD / XSLT)

## Файли даних (`data/`)

| Файл | Призначення |
|------|-------------|
| [medicines.xml](data/medicines.xml) | каталог препаратів, кореневий елемент `Medicine` |
| [medicines.xsd](data/medicines.xsd) | схема: прості й комплексні типи, переліки (`group`, `form`, `package/@type`, `currency`, `unit`), граничні значення (ціна, кількість, дозування, прийомів на день), шаблони (номер свідоцтва), обов'язкові атрибути та `xs:ID` для `drug/@id` |
| [medicines-by-group.xsl](data/medicines-by-group.xsl) | XSLT 1.0: розбиває список препаратів на підсписки за групою (`<MedicineByGroup><group name="…">…`) |

## Структура пакетів

| Пакет | Призначення |
|-------|-------------|
| `ua.medicine.model` | класи, що відповідають схемі: `Drug`, `Version`, `Producer`, `Certificate`, `DrugPackage`, `Dosage` та переліки |
| `ua.medicine.parser` | `SaxDrugParser`, `StaxDrugParser`, `DomDrugParser`; спільний `AbstractDrugParser` (template method) і `DrugAssembler` — відображення XML → об'єкти написане один раз для всіх трьох парсерів |
| `ua.medicine.validator` | `XmlValidator` — валідація XML за XSD зі збором усіх помилок |
| `ua.medicine.transform` | `XslTransformer` — XSL-перетворення |
| `ua.medicine.comparator` | `DrugComparator` — реалізації `Comparator<Drug>` (за id, назвою, групою, мінімальною ціною) |

Логування — Log4j2 ([log4j2.xml](src/main/resources/log4j2.xml)):
INFO у консоль, DEBUG у файл `logs/medicine.log` з ротацією.

## Запуск

```bash
mvn clean package
java -jar target/medicine-xml-1.0.0-jar-with-dependencies.jar
# або з власними файлами:
java -jar target/medicine-xml-1.0.0-jar-with-dependencies.jar <xml> <xsd> <xsl> <output>
```

Програма валідує документ, розбирає його SAX, StAX і DOM парсерами,
перевіряє що результати однакові, сортує препарати різними
компараторами і записує результат XSLT у `output/medicines-by-group.xml`.

## Тести

```bash
mvn clean verify   # JUnit 5 + Mockito, звіт JaCoCo: target/site/jacoco/index.html
```
