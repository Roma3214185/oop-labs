# Лабораторна робота 1 — Шеф-кухар

Ієрархія овочів, приготування салату, підрахунок калорійності,
сортування інгредієнтів за параметром і пошук овочів у діапазоні
калорійності.

## Структура пакетів

| Пакет | Призначення |
|-------|-------------|
| `ua.chef.model.vegetable` | абстрактний `Vegetable` і категорії: `RootVegetable`, `FruitVegetable`, `LeafyVegetable`, `BulbVegetable` |
| `ua.chef.model.vegetable.kind` | конкретні овочі (`Carrot`, `Tomato`, `Onion`, ...) |
| `ua.chef.model` | `Salad` (калорії, сортування, пошук), `Recipe` |
| `ua.chef.factory` | `VegetableType`, `VegetableFactory` — створення овочів за назвою |
| `ua.chef.sort` | `SortCriterion` — компаратори для сортування |
| `ua.chef.service` | `Chef` — готує салат за рецептом |
| `ua.chef.config` | `AppConfig`, `ConfigLoader` — параметри з `.properties` файлу |
| `ua.chef.view` | `SaladPrinter` — виведення в консоль |

## Запуск

```bash
mvn clean package
java -jar target/chef-salad-1.0.0.jar                  # src/main/resources/salad.properties
java -jar target/chef-salad-1.0.0.jar my-salad.properties
```

Параметри ініціалізації (рецепт, критерій сортування, діапазон
калорійності) задаються у файлі
[salad.properties](src/main/resources/salad.properties).

## Тести

```bash
mvn clean verify   # JUnit 5 + Mockito, звіт JaCoCo: target/site/jacoco/index.html
```
