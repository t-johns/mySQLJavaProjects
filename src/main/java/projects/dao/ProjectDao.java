package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

public class ProjectDao extends DaoBase {
  private static final String CATEGORY_TABLE = "category"; // Constants set
  private static final String MATERIAL_TABLE = "material"; // mySQL to be inserted as Table
  private static final String PROJECT_TABLE = "project";
  private static final String PROJECT_CATEGORY_TABLE = "project_category";
  private static final String STEP_TABLE = "step";
  
  public Project insertProject(Project project) {
    // @formatter: off
    String sql = "" // build and insert sql String statement
        + "INSERT INTO " + PROJECT_TABLE + " "
        + "(project_name, estimated_hours, actual_hours, difficulty, notes) "
        + "VALUES "
        + "(?, ?, ?, ?, ?)";
    // @formatter :on
    
    try (Connection conn = DbConnection.getConnection()) { //try connection
      startTransaction(conn); //start transaction,
                              // provided at DaoBase.java
      
      try (PreparedStatement stmt = conn.prepareStatement(sql)) { // try set prepared statements
        setParameter(stmt, 1, project.getProjectName(), String.class); // start prepared statements and get user input
        setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class); // checks then sets inputType
        setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
        setParameter(stmt, 4, project.getDifficulty(), Integer.class);
        setParameter(stmt, 5, project.getNotes(), String.class); // provided by DaoBase.java
        
        stmt.executeUpdate(); 
        Integer projectId = getLastInsertId(conn, PROJECT_TABLE); // set project table to key value of last
                                                                  // row inserted into given table
        
        commitTransaction(conn); // "write all changes, if any, to database" 
        project.setProjectId(projectId); // project id key
        return project; // complete project object
      }
      catch(Exception e) {
        rollbackTransaction(conn); //rollback transaction from DaoBase.java
        throw new DbException(e);
      }
    }
    catch(SQLException e) {
      throw new DbException(e); // exception
    }
    
  }

}
