package br.ce.wcaquino.tasks.functionaltest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

  @Test
  public void deveExcluirTarefaComSucesso() throws IOException {
    WebDriver driver = acessarAplicacao();

    try {
      salvarTarefaPelaApi();

      WebElement deleteTaskButton = driver.findElement(By.cssSelector("#todoTable > tbody > tr:last-child > td:last-child > a"));
      deleteTaskButton.click();
      String message = driver.findElement(By.id("message")).getText();
      Assert.assertEquals("Success!", message);
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

  private void salvarTarefaPelaApi() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String backendBaseUrl = System.getProperty("app.backendbaseurl");

    MediaType JSON = MediaType.get("application/json; charset=utf-8");
    RequestBody newTaskJson = RequestBody
        .create("{\"task\":\"Tarefa criada pela API - via Teste Funcional\",\"dueDate\":\"5999-01-01\"}",
            JSON);

    Request request = new Request.Builder()
        .url(backendBaseUrl.concat("/todo"))
        .post(newTaskJson)
        .build();

    client.newCall(request).execute();
  }
}
