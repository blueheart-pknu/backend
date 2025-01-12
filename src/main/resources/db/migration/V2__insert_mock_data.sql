-- Insert Users
INSERT INTO users (id, username, student_number, role, created_at, deleted_at)
VALUES
    (1, 'user1', '20230001', 'USER', NOW(), NULL),
    (2, 'user2', '20230002', 'USER', NOW(), NULL),
    (3, 'user3', '20230003', 'USER', NOW(), NULL),
    (4, 'admin', '20230000', 'ADMIN', NOW(), NULL);

-- Insert Activities
-- Insert Activities
INSERT INTO activities (id, title, description, place, place_url, status, max_number, creator_id, created_at, expired_at, deleted_at)
VALUES
    (1, 'Activity 1', 'Description 1', 'Place 1', 'http://place1.com', 'PROGRESSING', 10, 1, NOW(), DATEADD('DAY', 7, NOW()), NULL),
    (2, 'Activity 2', 'Description 2', 'Place 2', 'http://place2.com', 'PROGRESSING', 20, 2, NOW(), DATEADD('DAY', 7, NOW()), NULL);

-- Insert Activity Histories
INSERT INTO activity_histories (id, activity_id, user_id, created_at, deleted_at)
VALUES
    (1, 1, 1, DATEADD('DAY', -5, NOW()), NULL),
    (2, 1, 2, DATEADD('DAY', -4, NOW()), NULL),
    (3, 2, 3, DATEADD('DAY', -3, NOW()), NULL),
    (4, 2, 1, DATEADD('DAY', -2, NOW()), DATEADD('DAY', -1, NOW())),
    (5, 1, 3, DATEADD('DAY', -1, NOW()), NULL);

-- Insert Notifications
INSERT INTO notifications (id, sender_id, receiver_id, content, created_at, deleted_at)
VALUES
    (1, 1, 2, 'Notification 1', NOW(), NULL),
    (2, 2, 3, 'Notification 2', NOW(), NULL),
    (3, 3, 1, 'Notification 3', NOW(), NULL);

-- Insert Groups
INSERT INTO groups (id, name, created_at, updated_at, deleted_at)
VALUES
    (1, 'Group 1', NOW(), NOW(), NULL),
    (2, 'Group 2', NOW(), NOW(), NULL);

-- Insert Group Users
INSERT INTO group_users (id, group_id, user_id, created_at, deleted_at)
VALUES
    (1, 1, 1, NOW(), NULL),
    (2, 1, 2, NOW(), NULL),
    (3, 2, 3, NOW(), NULL);