INSERT INTO category (id, name) VALUES (0, 'Voorgerecht'),
(1, 'Hoofdgerecht'),
(2, 'Bijgerechten'),
(3, 'Salades'),
(4, 'Soepen'),
(5, 'Vegetarisch'),
(6, 'Veganitisch'),
(7, 'Glutenvrij'),
(8, 'Desserts'),
(9, 'Bakken'),
(10, 'Dranken'),
(11, 'Smoothies'),
(12, 'Internationale keuken'),
(13, 'Snacks'),
(14, 'Gezonde recepten'),
(15, 'Feestelijke gerechten'),
(16, 'Snel en makkelijk'),
(17, 'Budgetvriendelijk'),
(18, 'Seizoensgebonden gerechten'),
(19, 'Kinderrecepten');

INSERT INTO users (id, email, firstname ,lastname, password, role)
VALUES (101, 'admin@admin.com', 'Admin', 'adminUser', '$2a$10$K6amD187nBQpt.2KiARLfuKK2METH6ny0Xz0ZraQUZTsnPUifqhOS', 'ADMIN'),
       (102, 'user@user.com', 'User', 'normalUser', '$2a$10$HbsP9RQzjB0G9XjE.9f8ZenpihkmpUVOj5DUyyh2vvapX9Zide.kW', 'USER');

INSERT INTO review (date, review, user_id, id, recipe_id)
VALUES ('23-03-2024', 5, 102, 100, 42),
       ('23-02-2024', 3, 101, 101, 42);

INSERT INTO recipe (id, name, instructions, duration, difficulty, portion_size, nutritional_information, allergies, category_id, user_id, supplies, ingredients)
VALUES (42, 'Random Recipe', 'Mix all ingredients. Then cook for 30 minutes. Leave for another 5. You are done.', 45, 3, 4,
        'Calories: 300, Protein: 15g, Fat: 10g', 'Gluten, Dairy', 5, 102, 'Mixing bowl, Pan',
        '500g Flour, 4 Eggs, 500ml Milk, 75g Sugar, 25g Salt'),
        (123456789, 'New Recipe Name', 'Combine ingredients and bake at 350°F for 45 minutes.', 30, 5, 2,
        'Calories: 250, Protein: 20g, Fat: 8g', 'Gluten-free, Vegan', 7, 102, 'Baking dish, Mixing bowl, Oven',
        'Flour, Sugar, Baking powder, Salt, Almond milk');