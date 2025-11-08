#!/usr/bin/env bash
set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # no color

echo -e "${YELLOW}=== Установка окружения для Java backend ===${NC}"

# --- Проверка sudo
if [ "$EUID" -ne 0 ]; then
  echo -e "${YELLOW}Для установки пакетов могут потребоваться sudo-права.${NC}"
fi

# --- Определение дистрибутива
DISTRO=$(grep '^ID=' /etc/os-release | cut -d'=' -f2 | tr -d '"')
echo -e "${YELLOW}Определён дистрибутив: ${DISTRO}${NC}"

# --- Установка базовых пакетов
echo -e "${YELLOW}Устанавливаю системные утилиты...${NC}"
case "$DISTRO" in
  ubuntu|debian)
    sudo apt update -y && sudo apt install -y curl unzip zip git ca-certificates
    ;;
  fedora)
    sudo dnf install -y curl unzip zip git ca-certificates
    ;;
  arch)
    sudo pacman -Sy --noconfirm curl unzip zip git ca-certificates
    ;;
  *)
    echo -e "${RED} Неизвестный дистрибутив — установи curl и git вручную.${NC}"
    ;;
esac

# --- SDKMAN
if [ ! -d "$HOME/.sdkman" ]; then
  echo -e "${YELLOW}Устанавливаю SDKMAN...${NC}"
  curl -s "https://get.sdkman.io" | bash
else
  echo -e "${GREEN}SDKMAN уже установлен.${NC}"
fi

# --- Активация SDKMAN
source "$HOME/.sdkman/bin/sdkman-init.sh"

# --- Проверка и установка Java 21
JAVA_CURRENT=$(sdk current java | grep -Eo '21\.[0-9]+-tem' || true)

if [ -z "$JAVA_CURRENT" ]; then
  echo -e "${YELLOW}Java 21 не найдена. Устанавливаю Java 21.0.5-tem...${NC}"
  sdk install java 21.0.5-tem || true
  sdk use java 21.0.5-tem
  sdk default java 21.0.5-tem || true
else
  echo -e "${GREEN}Java 21 уже установлена: $JAVA_CURRENT${NC}"
  sdk use java "$JAVA_CURRENT"
  sdk default java "$JAVA_CURRENT" || true
fi

# --- Проверка и установка Gradle
if ! command -v gradle >/dev/null 2>&1; then
  echo -e "${YELLOW}Gradle не найден. Устанавливаю Gradle...${NC}"
  sdk install gradle
else
  echo -e "${GREEN}Gradle уже установлен.${NC}"
fi

# --- Настройка JAVA_HOME для текущей сессии и bashrc
JAVA_PATH=$(readlink -f "$(which java)" || true)
if [ -n "$JAVA_PATH" ]; then
  JAVA_HOME_PATH=$(dirname "$(dirname "$JAVA_PATH")")
  export JAVA_HOME="$JAVA_HOME_PATH"
  export PATH="$JAVA_HOME/bin:$PATH"
  echo -e "${GREEN}JAVA_HOME экспортирован для текущей сессии: $JAVA_HOME${NC}"

  # Добавляем в ~/.bashrc, если ещё не прописан
  if ! grep -q 'export JAVA_HOME=' "$HOME/.bashrc"; then
    echo "export JAVA_HOME=\"$JAVA_HOME_PATH\"" >> "$HOME/.bashrc"
    echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> "$HOME/.bashrc"
    echo -e "${GREEN}JAVA_HOME и PATH добавлены в ~/.bashrc${NC}"
  fi
else
  echo -e "${RED} Не удалось определить путь к Java${NC}"
  exit 1
fi

# --- Проверка установки
echo -e "${YELLOW}Проверяю установку...${NC}"

if java -version >/dev/null 2>&1; then
  echo -e "${GREEN}Java работает: $(java -version 2>&1 | head -n 1)${NC}"
else
  echo -e "${RED}Java не найдена! Проверь установку.${NC}"
  exit 1
fi

if gradle -v >/dev/null 2>&1; then
  echo -e "${GREEN}Gradle работает: $(gradle -v | grep Gradle)${NC}"
else
  echo -e "${RED}Gradle не найден!${NC}"
  exit 1
fi

echo -e "${GREEN}\n=== Всё готово! ===${NC}"
echo -e "JAVA_HOME: $JAVA_HOME"
echo -e "Java path: $(which java)"
echo -e "Gradle path: $(which gradle)"
echo -e "\nТеперь можно запускать backend"

