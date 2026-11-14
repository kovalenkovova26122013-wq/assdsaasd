# Roblox Horns — мод для Minecraft 1.21.11 (Fabric)

Рога из Roblox на голове + огонь между ними. Всё настраивается через **Mod Menu**.

## Что умеет
- **3 вида рогов**: токсичные (зелёный/фиолетовый), ледяные (синий), огненные (красный/оранжевый) — или без рогов.
- **Огонь на выбор**: как у рогов (авто), оранжевый, синий, зелёный, фиолетовый, белые искры, радужный, без огня.
- Сила огня (1–10), размер рогов, огонь от первого лица, показ на других игроках.
- Настройки сохраняются в `.minecraft/config/roblox_horns.json`.

## Как получить .jar (без установки Java и Gradle)
1. Зарегистрируйся на https://github.com и создай новый репозиторий (Create new repository → любое имя → Create).
2. Нажми **uploading an existing file** и перетащи туда ВСЁ содержимое этой папки (включая скрытую папку `.github`).
   Если папку `.github` не получается перетащить — создай файл `.github/workflows/build.yml` вручную (Add file → Create new file) и вставь содержимое из этого проекта.
3. Открой вкладку **Actions** → дождись зелёной галочки у задачи `build` (2–5 минут).
4. Открой выполненную задачу → внизу **Artifacts** → скачай `roblox-horns-jar` → внутри будет `roblox-horns-1.0.0.jar`.

## Как собрать у себя
Нужны JDK 21 и Gradle 9.x:

    gradle build

Готовый файл: `build/libs/roblox-horns-1.0.0.jar`.
(Или открой папку в IntelliJ IDEA — он сам скачает всё нужное, потом Gradle → Tasks → build → build.)

## Установка
1. Установи Fabric Loader для Minecraft 1.21.11: https://fabricmc.net/use/installer/
2. В папку `.minecraft/mods` положи:
   - `roblox-horns-1.0.0.jar`
   - Fabric API (1.21.11)
   - Mod Menu (1.21.11)
3. Запусти игру через профиль Fabric → **Mods** → **Roblox Horns** → кнопка настроек.

## Если сборка ругается на версию
Версии зависимостей лежат в `gradle.properties`. Если Gradle пишет, что какой-то версии нет
(`fabric_api_version`, `modmenu_version`, `yarn_mappings`, `loader_version`), открой https://fabricmc.net/develop,
выбери 1.21.11 и подставь актуальные номера. Для Mod Menu — https://modrinth.com/mod/modmenu/versions.

## Текстуры
Текстуры рогов нарисованы скриптом `tools/gen_horns.py` (нужен Python + Pillow). Хочешь точь-в-точь как в Roblox —
просто замени PNG в `src/main/resources/assets/roblox_horns/textures/entity/` (разметка UV должна остаться той же,
см. `HornsModel.java`).
