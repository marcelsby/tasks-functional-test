package br.ce.wcaquino.tasks.functionaltest;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TasksTests {

  @Test
  public void testEnvironment() {
    WebDriver driver = new ChromeDriver();
    driver.get("https://www.google.com");
  }
}
