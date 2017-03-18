DROP TABLE IF EXISTS catalog_items;
DROP TABLE IF EXISTS catalog;
DROP TABLE IF EXISTS item_properties;
DROP TABLE IF EXISTS item_categories;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS property;
DROP TABLE IF EXISTS category;

CREATE USER IF NOT EXISTS store_admin PASSWORD 'qu@d!L3248zR';

CREATE TABLE IF NOT EXISTS category (
  id          BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR2(255)  NOT NULL,
  description VARCHAR2(2048) NOT NULL,
  UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS property (
  id    BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name  VARCHAR2(255)  NOT NULL,
  value VARCHAR2(2048) NOT NULL,
  UNIQUE (name, value)
);

CREATE TABLE IF NOT EXISTS item (
  id          BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR2(255)  NOT NULL,
  description VARCHAR2(2048) NOT NULL,
  price       DOUBLE,
  UNIQUE (title),
  CHECK (price >= 0)
);

CREATE TABLE IF NOT EXISTS item_categories (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  item_id     BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  FOREIGN KEY (item_id) REFERENCES item (id),
  FOREIGN KEY (category_id) REFERENCES category (id),
  UNIQUE (item_id, category_id)
);

CREATE TABLE IF NOT EXISTS item_properties (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  item_id     BIGINT NOT NULL,
  property_id BIGINT NOT NULL,
  FOREIGN KEY (item_id) REFERENCES item (id),
  FOREIGN KEY (property_id) REFERENCES property (id),
  UNIQUE (item_id, property_id)
);

CREATE TABLE IF NOT EXISTS catalog (
  id   BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR2(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS catalog_items (
  id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  catalog_id BIGINT NOT NULL,
  item_id    BIGINT NOT NULL,
  FOREIGN KEY (catalog_id) REFERENCES catalog (id),
  FOREIGN KEY (item_id) REFERENCES item (id),
  UNIQUE (catalog_id, item_id)
);

INSERT INTO category (title, description) VALUES ('Cars', 'Some pretty cars should be here.');
INSERT INTO category (title, description) VALUES ('Cats', 'Some pretty cats should be here.');

INSERT INTO property (name, value) VALUES ('Size', 'Small');
INSERT INTO property (name, value) VALUES ('Size', 'Medium');
INSERT INTO property (name, value) VALUES ('Size', 'Long');
INSERT INTO property (name, value) VALUES ('Size', 'Extra Long');

INSERT INTO item (title, description, price) VALUES ('Skoda', 'Skoda - super car', 99999);
INSERT INTO item (title, description, price) VALUES ('Kotofei', 'Kotofei - super cat!', 1);