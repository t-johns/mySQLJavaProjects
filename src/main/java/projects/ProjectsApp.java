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
  private Project curProject;

  // @formatter:off
  private List<String> operations = List.of( // list of operations to choose
      "1) Add a project",
      "2) List projects",
      "3) Select a project",
      "4) Update projects details",
      "5) Delete a project"
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
          case 2:
            listProjects();
            break;
          case 3:
            selectProject();
            break;
          case 4:
            updateProjectDetails();
            break;
          case 5:
            deleteProject();
            break;

          default:
            System.out.println(
                "\n" + selection + " is not a valid selection. Try again."); // choose option
        }

      } catch (Exception e) {
        System.out.println("\nError: " + e + " Try again."); // exception value
        //e.printStackTrace();
      }
    }

  }

  private void deleteProject() {
    listProjects();
    
    Integer projectId = getIntInput("Enter Id of project you wish to delete: ");
    projectService.deleteProject(projectId);
    
    System.out.println("Project " + projectId + " was deleted successfully.");
    
    if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
      curProject = null;
    }
    
  }


  private void updateProjectDetails() {
    
    if (Objects.isNull(curProject)) { // curProject isNull, leave
      System.out.println("\nFirst please select valid project.");
      return;
    }
    Integer projectId = null; //projectDetails
    String projectName = null;
    BigDecimal estimatedHours= null;
    BigDecimal actualHours= null;
    Integer difficulty = null;
    String notes = null;
    
    Boolean updating = true; // set flag to get update input
    while (updating) {
      
    projectName = 
        getStringInput("Enter the project name [Current: "
            + curProject.getProjectName() + "]");
    
    estimatedHours = 
        getDecimalInput("Enter estimated hours [Current: "
            + curProject.getEstimatedHours() + "]");
    
    actualHours = 
        getDecimalInput("Enter actual hours [Current: "
            + curProject.getActualHours() + "]");
           
    difficulty =
        getIntInput("Enter difficulty 1-5 [Current: "
            + curProject.getDifficulty() + "]");

    notes = 
        getStringInput("Enter project notes [Current: " + curProject.getNotes() + "]");
    
    updating = false; // leave loop
    break;
    }   
    
    Project project = new Project();
    
    project.setProjectName(Objects.isNull(projectName) //setProjectName, if isNull
        ? curProject.getProjectName() : projectName); // curProj.getPN(), else projectName
    
    project.setEstimatedHours(Objects.isNull(estimatedHours) //setProjectName, if isNull
        ? curProject.getEstimatedHours() : estimatedHours);
    
    project.setActualHours(Objects.isNull(actualHours) //setProjectName, if isNull
        ? curProject.getActualHours() : actualHours);
    
    project.setDifficulty(Objects.isNull(difficulty) //setProjectName, if isNull
        ? curProject.getDifficulty() : difficulty);
    
    project.setNotes(Objects.isNull(notes) //setProjectName, if isNull
        ? curProject.getNotes() : notes);
    
    projectId = curProject.getProjectId();
    project.setProjectId(projectId);
    
    projectService.modifyProjectDetails(project);
    curProject = projectService.fetchProjectById(curProject.getProjectId());
    
  }


  private void selectProject() {
    listProjects();
    Integer projectId = getIntInput("Enter a project ID to select a project");
    curProject = null; // unselect project
    
    curProject = projectService.fetchProjectById(projectId);
    if (curProject == null) {
      System.out.println("Invalid project ID Selected");
    }
    
  }


  private void listProjects() {
    List<Project> projects = projectService.fetchAllProjects();
    System.out.println("\nProjects:");
    
    projects.forEach(project -> System.out.println(
        "   " + project.getProjectId() 
        + ": " + project.getProjectName()));    
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
    if (curProject == null) {
      System.out.println("\nYou are not working with a project.");
    } else {
      System.out.println("\nYou are currently working with project: " + curProject);
    }
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
