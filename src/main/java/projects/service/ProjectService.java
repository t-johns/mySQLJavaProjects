package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService { // project data Service layer
  private ProjectDao projectDao = new ProjectDao(); // project Data access object
  
  public Project addProject(Project project) { // send Project to mySQL
    return projectDao.insertProject(project); // JDBC (java database connectivity)
  }

}
