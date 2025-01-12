-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL,
                                     student_number VARCHAR(20),
                                     role VARCHAR(20),
                                     created_at TIMESTAMP NOT NULL,
                                     deleted_at TIMESTAMP
);

-- Create Activities Table
CREATE TABLE IF NOT EXISTS activities (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          title VARCHAR(100) NOT NULL,
                                          description TEXT,
                                          place VARCHAR(100),
                                          place_url VARCHAR(255),
                                          status VARCHAR(20),
                                          max_number INT,
                                          creator_id BIGINT,
                                          created_at TIMESTAMP NOT NULL,
                                          expired_at TIMESTAMP,
                                          deleted_at TIMESTAMP
);

-- Create Activity Histories Table
CREATE TABLE IF NOT EXISTS activity_histories (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  activity_id BIGINT,
                                                  user_id BIGINT,
                                                  created_at TIMESTAMP NOT NULL,
                                                  deleted_at TIMESTAMP
);

-- Create Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             sender_id BIGINT,
                                             receiver_id BIGINT,
                                             content TEXT,
                                             created_at TIMESTAMP NOT NULL,
                                             deleted_at TIMESTAMP
);

-- Create Groups Table
CREATE TABLE IF NOT EXISTS groups (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100),
                                      created_at TIMESTAMP NOT NULL,
                                      updated_at TIMESTAMP NOT NULL,
                                      deleted_at TIMESTAMP
);

-- Create Group Users Table
CREATE TABLE IF NOT EXISTS group_users (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           group_id BIGINT,
                                           user_id BIGINT,
                                           created_at TIMESTAMP NOT NULL,
                                           deleted_at TIMESTAMP
);