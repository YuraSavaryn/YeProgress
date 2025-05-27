package com.ccpc.yeprogress.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class MonobankJarScraper implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(MonobankJarScraper.class.getName());

    private WebDriver driver;
    private WebDriverWait wait;

    public MonobankJarScraper(boolean headless) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        Logger.getLogger("org.asynchttpclient").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("webdriver.chrome.verboseLogging", "false");

        setupDriver(headless);
    }

    public MonobankJarScraper() {
        this(true); // headless за замовчуванням
    }

    private void setupDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-logging");
        options.addArguments("--disable-extensions");
        options.addArguments("--silent");
        options.addArguments("--log-level=3");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-logging"));
        options.setExperimentalOption("useAutomationExtension", false);

        try {
            this.driver = new ChromeDriver(options);
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        } catch (Exception e) {
            logger.severe("Не вдалося ініціалізувати ChromeDriver: " + e.getMessage());
            throw new RuntimeException("Помилка ініціалізації WebDriver", e);
        }
    }

    public JarInfo extractJarInfo(String jarUrl) {
        if (jarUrl == null || jarUrl.trim().isEmpty()) {
            logger.warning("Порожній URL для скрапінга");
            return null;
        }

        try {
            logger.info("Починаємо скрапінг URL: " + jarUrl);
            driver.get(jarUrl);

            // Чекаємо, поки зникне лоадер
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("start-loader")));
                logger.info("Лоадер зник, продовжуємо");
            } catch (TimeoutException e) {
                logger.info("Лоадер не знайдено або не зник - продовжуємо");
            }

            // Додаткова пауза для завантаження динамічного контенту
            Thread.sleep(3000);

            // Збираємо всю інформацію
            JarInfo result = parseJarData();
            logger.info("Скрапінг завершено. Результат: " + (result != null ? "успішно" : "невдало"));

            return result;

        } catch (Exception e) {
            logger.warning("Помилка при скрапінгу " + jarUrl + ": " + e.getMessage());
            return null;
        }
    }

    private JarInfo parseJarData() {
        JarInfo jarInfo = new JarInfo();

        try {
            findAmounts(jarInfo);
            logger.info("Знайдено сум - накопичена: " + jarInfo.getAccumulated() + ", ціль: " + jarInfo.getGoal());
        } catch (Exception e) {
            logger.warning("Помилка парсингу даних: " + e.getMessage());
        }

        return jarInfo;
    }

    private void findAmounts(JarInfo jarInfo) {
        try {
            // Шукаємо всі елементи з грошовими сумами
            List<WebElement> moneyElements = driver.findElements(By.xpath("//*[contains(text(), '₴')]"));
            logger.info("Знайдено елементів з ₴: " + moneyElements.size());

            List<AmountInfo> amounts = new ArrayList<>();
            Pattern amountPattern = Pattern.compile("([\\d\\s]+(?:\\.\\d+)?)\\s*₴");

            for (WebElement element : moneyElements) {
                String text = element.getText().trim();
                if (!text.isEmpty() && text.matches(".*\\d.*")) {
                    Matcher matcher = amountPattern.matcher(text);
                    if (matcher.find()) {
                        String amountStr = matcher.group(1).replaceAll("\\s", "");
                        try {
                            double amountValue = Double.parseDouble(amountStr);
                            amounts.add(new AmountInfo(text, amountValue, amountStr));
                            logger.info("Знайдена сума: " + text + " (" + amountValue + ")");
                        } catch (NumberFormatException e) {
                            logger.warning("Не вдалося парсити суму: " + amountStr);
                            continue;
                        }
                    }
                }
            }

            // Сортуємо за сумою (більша сума зазвичай накопичена)
            amounts.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            if (amounts.size() >= 2) {
                // Перша (більша) - накопичена сума
                jarInfo.setAccumulated(amounts.get(0).getText());
                // Друга - ціль збору
                jarInfo.setGoal(amounts.get(1).getText());
                logger.info("Встановлено: накопичена=" + jarInfo.getAccumulated() + ", ціль=" + jarInfo.getGoal());
            } else if (amounts.size() == 1) {
                jarInfo.setAccumulated(amounts.get(0).getText());
                logger.info("Знайдено тільки одну суму: " + jarInfo.getAccumulated());
            } else {
                logger.warning("Не знайдено жодної суми на сторінці");
            }

        } catch (Exception e) {
            logger.warning("Помилка пошуку сум: " + e.getMessage());
        }
    }

    public Map<String, String> getCleanResult(JarInfo jarInfo) {
        Map<String, String> result = new HashMap<>();

        if (jarInfo != null) {
            if (jarInfo.getAccumulated() != null) {
                result.put("accumulated", jarInfo.getAccumulated());
            }
            if (jarInfo.getGoal() != null) {
                result.put("goal", jarInfo.getGoal());
            }
        }

        return result;
    }

    @Override
    public void close() {
        try {
            if (driver != null) {
                driver.quit();
                logger.info("WebDriver закрито");
            }
        } catch (Exception e) {
            logger.warning("Помилка при закритті WebDriver: " + e.getMessage());
        }
    }

    // Основна функція для скрапінгу банки
    public static Map<String, String> scrapeJar(String jarUrl) {
        try (MonobankJarScraper scraper = new MonobankJarScraper(true)) {
            JarInfo jarInfo = scraper.extractJarInfo(jarUrl);

            if (jarInfo != null) {
                return scraper.getCleanResult(jarInfo);
            } else {
                return new HashMap<>();
            }
        } catch (Exception e) {
            Logger.getLogger(MonobankJarScraper.class.getName())
                    .warning("Помилка в scrapeJar: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Спрощена функція для швидкого використання
    public static Map<String, String> getJarAmounts(String jarUrl) {
        return scrapeJar(jarUrl);
    }

    // Приклад використання з виведенням результату
    public static void main(String[] args) {
        String jarUrl = "https://send.monobank.ua/jar/5NFfSYbPbm";
        Map<String, String> result = scrapeJar(jarUrl);

        if (result != null && !result.isEmpty()) {
            System.out.println("Накопичено: " + result.getOrDefault("accumulated", "Не знайдено"));
            System.out.println("Ціль: " + result.getOrDefault("goal", "Не знайдено"));
        } else {
            System.out.println("Не вдалося отримати інформацію про банку");
        }
    }

    // Клас для зберігання інформації про банку
    public static class JarInfo {
        private String accumulated;
        private String goal;

        public JarInfo() {}

        public String getAccumulated() {
            return accumulated;
        }

        public void setAccumulated(String accumulated) {
            this.accumulated = accumulated;
        }

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        @Override
        public String toString() {
            return "JarInfo{" +
                    "accumulated='" + accumulated + '\'' +
                    ", goal='" + goal + '\'' +
                    '}';
        }
    }

    // Клас для зберігання інформації про суму
    public static class AmountInfo {
        private String text;
        private double value;
        private String formatted;

        public AmountInfo(String text, double value, String formatted) {
            this.text = text;
            this.value = value;
            this.formatted = formatted;
        }

        public String getText() {
            return text;
        }

        public double getValue() {
            return value;
        }

        public String getFormatted() {
            return formatted;
        }

        @Override
        public String toString() {
            return "AmountInfo{" +
                    "text='" + text + '\'' +
                    ", value=" + value +
                    ", formatted='" + formatted + '\'' +
                    '}';
        }
    }
}