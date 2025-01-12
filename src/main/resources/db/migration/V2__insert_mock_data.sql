-- Insert Users
INSERT INTO users (username, student_number, role, created_at, deleted_at)
VALUES
    ('user1', '20230001', 'USER', NOW(), NULL),
    ('user2', '20230002', 'USER', NOW(), NULL),
    ('user3', '20230003', 'USER', NOW(), NULL),
    ('admin', '20230000', 'ADMIN', NOW(), NULL);

-- Insert Activities
INSERT INTO activities (title, description, place, place_url, status, max_number, creator_id, created_at, expired_at, deleted_at)
VALUES
    ('Activity 1', 'Description 1', 'Place 1', 'http://place1.com', 'PROGRESSING', 10, 1, NOW(), DATEADD('DAY', 7, NOW()), NULL),
    ('Activity 2', 'Description 2', 'Place 2', 'http://place2.com', 'PROGRESSING', 20, 2, NOW(), DATEADD('DAY', 7, NOW()), NULL);

-- Insert Activity Histories
INSERT INTO activity_histories (activity_id, user_id, created_at, deleted_at)
VALUES
    (1, 1, DATEADD('DAY', -5, NOW()), NULL),
    (1, 2, DATEADD('DAY', -4, NOW()), NULL),
    (2, 3, DATEADD('DAY', -3, NOW()), NULL),
    (2, 1, DATEADD('DAY', -2, NOW()), DATEADD('DAY', -1, NOW())),
    (1, 3, DATEADD('DAY', -1, NOW()), NULL);

-- Insert Notifications
INSERT INTO notifications (sender_id, receiver_id, content, created_at, deleted_at)
VALUES
    (1, 2, 'Notification 1', NOW(), NULL),
    (2, 3, 'Notification 2', NOW(), NULL),
    (3, 1, 'Notification 3', NOW(), NULL);

-- Insert Groups
INSERT INTO groups (name, created_at, updated_at, deleted_at)
VALUES
    ('Group 1', NOW(), NOW(), NULL),
    ('Group 2', NOW(), NOW(), NULL);

-- Insert Group Users
INSERT INTO group_users (group_id, user_id, created_at, deleted_at)
VALUES
    (1, 1, NOW(), NULL),
    (1, 2, NOW(), NULL),
    (2, 3, NOW(), NULL);