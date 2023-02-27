DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
  project_id INT NOT NULL AUTO_INCREMENT, /*primary key*/
  project_name VARCHAR(128) NOT NULL,
  estimated_hours decimal(7,2),
  actual_hours decimal(7,2),
  difficulty INT,
  notes TEXT,
  PRIMARY KEY (project_id)
);

CREATE TABLE material (
  material_id INT NOT NULL AUTO_INCREMENT, /*primary key*/
  project_id INT NOT NULL,
  material_name VARCHAR(128) NOT NULL,
  num_required INT,
  cost decimal(7,2),
  PRIMARY KEY (material_id),
  FOREIGN KEY (project_id) REFERENCES project (project_id)
);

CREATE TABLE step (
  step_id INT NOT null AUTO_INCREMENT, /*primary key*/
  project_id INT NOT NULL,
  step_text TEXT not NULL,
  step_order TEXT NOT NULL,
  PRIMARY KEY (step_id),
  FOREIGN KEY (project_id) references project (project_id)
);

CREATE TABLE category (
  category_id INT not null AUTO_INCREMENT, /*primary key*/
  category_name VARCHAR(128) NOT NULL,
  PRIMARY KEY (category_id)
);

CREATE TABLE project_category (
  project_id INT not null AUTO_INCREMENT, /*primary key*/
  category_id INT not null, /*primary key*/
  FOREIGN KEY (project_id) REFERENCES project (project_id),
  FOREIGN KEY (category_id) REFERENCES category (category_id),
  UNIQUE KEY (category_id),
  UNIQUE KEY (project_id)
);