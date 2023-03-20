package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService { // project data Service layer
  private ProjectDao projectDao = new ProjectDao(); // project Data access object
  
  public Project addProject(Project project) { // send Project to mySQL
    return projectDao.insertProject(project); // JDBC (java database connectivity)
  }

  public List<Project> fetchAllProjects() {
    return projectDao.fetchAllProjects();
  }

  public Project fetchProjectById(Integer projectId) {
    return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
        "Project with projectID=" + projectId + " does not exist."));
  }
  
  public Project fetchProjectId(Integer projectId) {
     return projectDao.fetchProjectById(projectId).orElseThrow(
         () -> new NoSuchElementException("Project with project ID=" + projectId + " does not exist."));

  }

  public void modifyProjectDetails(Project project) {
      if(!projectDao.modifyProjectDetails(project)) {
        throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
        
      }
    }

  public void deleteProject(Integer delProject) {
    if(!projectDao.deleteProject(delProject)) {
      throw new DbException("Oops, project does not exist.");
    }
    
  }
  }


