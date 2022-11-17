package br.ce.wcaquino.tasks.functionaltest;

import java.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TasksTests {

  @Test
  public void deveSalvarTarefaComSucesso() {
    WebDriver driver = acessarAplicacao();

    driver.findElement(By.id("addTodo")).click();

    driver.findElement(By.id("task")).sendKeys("Tarefa salva com sucesso - via Selenium");
    driver.findElement(By.id("dueDate")).sendKeys("10/10/2999");

    driver.findElement(By.id("saveButton")).click();

    String message = driver.findElement(By.id("message")).getText();
    Assert.assertEquals("Success!", message);

    driver.quit();
  }

  @Test
  public void naoDeveSalvarTarefaSemPrazo() {
    WebDriver driver = acessarAplicacao();

    driver.findElement(By.id("addTodo")).click();

    driver.findElement(By.id("task")).sendKeys("Tarefa sem prazo");

    driver.findElement(By.id("saveButton")).click();

    String message = driver.findElement(By.id("message")).getText();
    Assert.assertEquals("Fill the due date", message);

    driver.quit();
  }

  @Test
  public void naoDeveSalvarTarefaSemDescricao() {
    WebDriver driver = acessarAplicacao();

    driver.findElement(By.id("addTodo")).click();

    driver.findElement(By.id("dueDate")).sendKeys("10/10/2010");

    driver.findElement(By.id("saveButton")).click();

    String message = driver.findElement(By.id("message")).getText();
    Assert.assertEquals("Fill the task description", message);

    driver.quit();
  }

  @Test
  public void naoDeveSalvarTarefaComDataPassada() {
    WebDriver driver = acessarAplicacao();

    driver.findElement(By.id("addTodo")).click();

    driver.findElement(By.id("task")).sendKeys("Erro com data passada");
    driver.findElement(By.id("dueDate")).sendKeys("10/10/2010");

    driver.findElement(By.id("saveButton")).click();

    String message = driver.findElement(By.id("message")).getText();
    Assert.assertEquals("Due date must not be in past", message);

    driver.quit();
  }

  private WebDriver acessarAplicacao() {
    WebDriver driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    String baseUrl = System.getProperty("app.baseurl");
    driver.get(baseUrl);

    return driver;
  }
}
