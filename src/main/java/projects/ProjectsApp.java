package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp { // Menu Driven Application to Insert (Create) new Projects into mySQL DB
  private Scanner scanner = new Scanner(System.in); //scanner for input
  private ProjectService projectService = new ProjectService(); // ProjectService for data layer

  // @formatter:off
  private List<String> operations = List.of( // list of operations to choose
      "1) Add a project"
      );
  // @formatter:on

  public static void main(String[] args) {
    new ProjectsApp().processUserSelections(); // new ProjectsApp object

  }


  private void processUserSelections() { // process user selections
    boolean done = false; // flag for menu

    while (!done) { // while not flag

      try {
        int selection = getUserSelection(); // Main Prompt

        switch (selection) { // Selection
          case -1:
            done = exitMenu(); // exit
            break;
          case 1:
            createProject(); // create and Insert Project object into DB
            break;

          default:
            System.out.println(
                "\n" + selection + " is not a valid selection. Try again."); // choose option
        }

      } catch (Exception e) {
        System.out.println("\nError: " + e + " Try again."); // exception value
      }
    }

  }

  private int getUserSelection() { // Main Prompt
    printOperations();
    
    Integer input = getIntInput("\nEnter a menu selection");
    
    return Objects.isNull(input) ? -1 : input; // (if (input) isNull, -1.exit menu()), else return input
  }
  
  
  private void createProject() { // create User Project
    String projectName = getStringInput("Enter the project name"); // User Input to Rows
    BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours"); // decimal
    BigDecimal actualHours = getDecimalInput("Enter the actual hours"); // decimal
    Integer difficulty = getIntInput("Enter the project difficulty (1-5)"); // Int
    String notes = getStringInput("Enter the project notes"); // string

    Project project = new Project(); // Set Project, new project object
    project.setProjectName(projectName); // project.entity Setters
    project.setEstimatedHours(estimatedHours);
    project.setActualHours(actualHours);
    project.setDifficulty(difficulty);
    project.setNotes(notes); 

    Project dbProject = projectService.addProject(project); // add Object, set to from data layer && 
                                                            // conn. All data

    System.out.println("You have successfully created project: " + dbProject); // yay! (hopefully)
    return;
  }


  private BigDecimal getDecimalInput(String prompt) {
    String input = getStringInput(prompt); // input prompt get string input

    if (Objects.isNull(input)) { // if null
      return null;
    }

    try {
      return new BigDecimal(input).setScale(2); // .00 set as parmeter
    } catch (Exception e) { //exception
      throw new DbException(input + " is not a valid decimal number.");
    }
  }

  private String getStringInput(String prompt) { // input to String
    System.out.print(prompt + ": ");
    String input = scanner.nextLine();

    return input.isBlank() ? null : input.trim(); //if blank null, otherwise trim

  }

  private boolean exitMenu() {
    System.out.println("Exiting the menu.");
    return true;
  }


  private void printOperations() {
    System.out.println(
        "\nThese are the available selections. Press the Enter key to quit:");

    operations.forEach(line -> System.out.println("   " + line)); // for each line in operations List<String>
                                                                  // value, add space + line Lambda for indent
  }

  private Integer getIntInput(String prompt) {
    String input = getStringInput(prompt); //get string input

    if (Objects.isNull(input)) { // if is null
      return null;
    }

    try {
      return Integer.valueOf(input); // Int string to value catch
    } catch (Exception e) {
      throw new DbException(input + " is not a valid number. Try again.");
    }

  }


}
