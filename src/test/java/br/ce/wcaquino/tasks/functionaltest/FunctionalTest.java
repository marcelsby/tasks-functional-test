package br.ce.wcaquino.tasks.functionaltest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FunctionalTest {

  @Test
  public void deveSalvarTarefaComSucesso() throws MalformedURLException {
    WebDriver driver = acessarAplicacao();

    try {
      driver.findElement(By.id("addTodo")).click();

      driver.findElement(By.id("task")).sendKeys("Tarefa salva com sucesso - via Selenium");
      driver.findElement(By.id("dueDate")).sendKeys("10/10/2999");

      driver.findElement(By.id("saveButton")).click();

      String message = driver.findElement(By.id("message")).getText();
      Assert.assertEquals("Success!", message);
    } finally {
      driver.quit();
    }
  }

  @Test
  public void naoDeveSalvarTarefaSemPrazo() throws MalformedURLException {
    WebDriver driver = acessarAplicacao();

    try {
      driver.findElement(By.id("addTodo")).click();

      driver.findElement(By.id("task")).sendKeys("Tarefa sem prazo");

      driver.findElement(By.id("saveButton")).click();

      String message = driver.findElement(By.id("message")).getText();
      Assert.assertEquals("Fill the due date", message);
    } finally {
      driver.quit();
    }
  }

  @Test
  public void naoDeveSalvarTarefaSemDescricao() throws MalformedURLException {
    WebDriver driver = acessarAplicacao();

    try {
      driver.findElement(By.id("addTodo")).click();

      driver.findElement(By.id("dueDate")).sendKeys("10/10/2010");

      driver.findElement(By.id("saveButton")).click();

      String message = driver.findElement(By.id("message")).getText();
      Assert.assertEquals("Fill the task description", message);
    } finally {
      driver.quit();
    }
  }

  @Test
  public void naoDeveSalvarTarefaComDataPassada() throws MalformedURLException {
    WebDriver driver = acessarAplicacao();

    try {
      driver.findElement(By.id("addTodo")).click();

      driver.findElement(By.id("task")).sendKeys("Erro com data passada");
      driver.findElement(By.id("dueDate")).sendKeys("10/10/2010");

      driver.findElement(By.id("saveButton")).click();

      String message = driver.findElement(By.id("message")).getText();
      Assert.assertEquals("Due date must not be in past", message);
    } finally {
      driver.quit();
    }
  }

  private WebDriver acessarAplicacao() throws MalformedURLException {
    ChromeOptions chromeOptions = new ChromeOptions();

    boolean isDebugChrome = Boolean.parseBoolean(System.getProperty("app.chrome.debug"));

    if (!isDebugChrome) {
      chromeOptions.addArguments("--no-sandbox");
      chromeOptions.setHeadless(true);
    }

    WebDriver driver = new RemoteWebDriver(new URL(System.getProperty("app.selenium.hub.url")), chromeOptions);

    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

    String baseUrl = System.getProperty("app.baseurl");
    driver.get(baseUrl);

    return driver;
  }
}
