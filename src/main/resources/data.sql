INSERT INTO `user`(name, nickname, email, token, birthday, password, profile_image, gender)
VALUES ('최희윤', '트리', 'tree@gmail.com', 'token', '1991-11-09', 'password', '/image/tree.jpg', 1);
INSERT INTO `user`(name, nickname, email, token, birthday, password, profile_image, gender)
VALUES ('정인호', '이노', 'enolj76@gmail.com', 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIn0.ysfQimdEO_LZwRgZEEPDI0dxQKlvnIXSWQgpZHnJqRg', '1996-02-27', '$2a$10$TAK8zT78BdabREx5L6ap4OI6R1AMPdj1oFiYrzjOcKSZoZfhSJpWm', '/image/eno.jpg', 0);
/*password: 1234*/

INSERT INTO `user_roles`(user_id, roles)
VALUES (2, 'ROLE_USER');
