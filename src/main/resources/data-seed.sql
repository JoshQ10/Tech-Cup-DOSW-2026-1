-- ============================================================
-- TechCup DOSW 2026-1 - Datos de prueba para todas las tablas
-- Ejecutar en PostgreSQL (techcup_db)
-- ============================================================

-- 1. Limpiar datos existentes (orden inverso de dependencias)
DELETE FROM sanctions;
DELETE FROM elimination_brackets;
DELETE FROM lineup_players;
DELETE FROM lineups;
DELETE FROM match_events;
DELETE FROM match_possession_heatmap;
DELETE FROM match_possessions;
DELETE FROM matches;
DELETE FROM courts;
DELETE FROM standings;
DELETE FROM payments;
DELETE FROM team_invitations;
DELETE FROM team_players;
DELETE FROM teams;
DELETE FROM tournament_rules_confirmations;
DELETE FROM tournament_dates;
DELETE FROM tournaments;
DELETE FROM sport_profiles;
DELETE FROM verification_tokens;
DELETE FROM password_reset_tokens;
DELETE FROM revoked_refresh_tokens;
DELETE FROM role_permissions;
DELETE FROM permissions;
DELETE FROM roles;
DELETE FROM users;

-- 2. Reiniciar secuencias
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE sport_profiles_id_seq RESTART WITH 1;
ALTER SEQUENCE tournaments_id_seq RESTART WITH 1;
ALTER SEQUENCE tournament_dates_id_seq RESTART WITH 1;
ALTER SEQUENCE teams_id_seq RESTART WITH 1;
ALTER SEQUENCE team_invitations_id_seq RESTART WITH 1;
ALTER SEQUENCE payments_id_seq RESTART WITH 1;
ALTER SEQUENCE courts_id_seq RESTART WITH 1;
ALTER SEQUENCE matches_id_seq RESTART WITH 1;
ALTER SEQUENCE match_events_id_seq RESTART WITH 1;
ALTER SEQUENCE lineups_id_seq RESTART WITH 1;
ALTER SEQUENCE lineup_players_id_seq RESTART WITH 1;
ALTER SEQUENCE standings_id_seq RESTART WITH 1;
ALTER SEQUENCE elimination_brackets_id_seq RESTART WITH 1;
ALTER SEQUENCE sanctions_id_seq RESTART WITH 1;

-- ============================================================
-- TABLA 1: users (13 usuarios - 5 roles)
-- Password: "password123" encriptado con BCrypt
-- Columnas: id, first_name, last_name, username, email, password,
--           identification, role, user_type, program, active, created_at
-- ============================================================
INSERT INTO users (id, first_name, last_name, username, email, password, identification, role, user_type, program, active, created_at) VALUES
-- ADMINISTRADOR (1)
(1, 'Juan', 'Lopez', 'juan.admin', 'admin@escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '1001234567', 'ADMINISTRATOR', 'PROFESSOR', 'SYSTEMS_ENGINEERING', true, '2026-01-15 08:00:00'),

-- ARBITROS (2)
(2, 'Carlos', 'Perez', 'carlos.arbitro', 'arbitro1@escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '1009876543', 'REFEREE', 'PROFESSOR', 'ECONOMICS', true, '2026-01-20 10:00:00'),
(3, 'Ana', 'Rodriguez', 'ana.arbitro', 'arbitro2@escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '1005551234', 'REFEREE', 'ADMINISTRATIVE', 'BUSINESS_ADMINISTRATION', true, '2026-01-20 10:30:00'),

-- CAPITANES (2) - tambien son jugadores
(4, 'Pedro', 'Garcia', 'pedro.capitan', 'capitan.ingenieros@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2020134001', 'CAPTAIN', 'STUDENT', 'SYSTEMS_ENGINEERING', true, '2026-02-01 09:00:00'),
(5, 'Maria', 'Torres', 'maria.capitan', 'capitan.byte@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2020134002', 'CAPTAIN', 'STUDENT', 'ARTIFICIAL_INTELLIGENCE_ENGINEERING', true, '2026-02-01 09:30:00'),

-- JUGADORES Equipo 1: Ingenieros FC (4 jugadores)
(6, 'Andres', 'Ruiz', 'andres.ruiz', 'andres.ruiz@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2021134001', 'PLAYER', 'STUDENT', 'SYSTEMS_ENGINEERING', true, '2026-02-05 14:00:00'),
(7, 'Luis', 'Diaz', 'luis.diaz', 'luis.diaz@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2021134002', 'PLAYER', 'STUDENT', 'CYBERSECURITY_ENGINEERING', true, '2026-02-05 14:30:00'),
(8, 'Sofia', 'Martinez', 'sofia.martinez', 'sofia.martinez@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2022134001', 'PLAYER', 'STUDENT', 'STATISTICS_ENGINEERING', true, '2026-02-06 08:00:00'),
(9, 'Diego', 'Gomez', 'diego.gomez', 'diego.gomez@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2022134002', 'PLAYER', 'STUDENT', 'SYSTEMS_ENGINEERING', true, '2026-02-06 08:30:00'),

-- JUGADORES Equipo 2: Byte United (4 jugadores)
(10, 'Valentina', 'Rios', 'valentina.rios', 'valentina.rios@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2020134003', 'PLAYER', 'STUDENT', 'ARTIFICIAL_INTELLIGENCE_ENGINEERING', true, '2026-02-07 10:00:00'),
(11, 'Sebastian', 'Castro', 'sebastian.castro', 'sebastian.castro@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2021134003', 'PLAYER', 'STUDENT', 'CYBERSECURITY_ENGINEERING', true, '2026-02-07 10:30:00'),
(12, 'Camila', 'Herrera', 'camila.herrera', 'camila.herrera@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2022134003', 'PLAYER', 'STUDENT', 'STATISTICS_ENGINEERING', true, '2026-02-07 11:00:00'),
(13, 'Felipe', 'Mora', 'felipe.mora', 'felipe.mora@mail.escuelaing.edu.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '2023134001', 'PLAYER', 'STUDENT', 'ARTIFICIAL_INTELLIGENCE_ENGINEERING', true, '2026-02-07 11:30:00');

SELECT setval('users_id_seq', 13);

-- ============================================================
-- TABLA 2: sport_profiles (10 perfiles - capitanes + jugadores)
-- ============================================================
INSERT INTO sport_profiles (id, user_id, position, program, jersey_number, photo_url, available, semester, gender, age) VALUES
-- Capitan Ingenieros FC
(1, 4, 'MIDFIELDER', 'SYSTEMS_ENGINEERING', 10, 'https://storage.techcup.co/photos/pedro_garcia.jpg', true, 6, 'M', 22),
-- Capitan Byte United
(2, 5, 'FORWARD', 'ARTIFICIAL_INTELLIGENCE_ENGINEERING', 9, 'https://storage.techcup.co/photos/maria_torres.jpg', true, 5, 'F', 21),
-- Jugadores Ingenieros FC
(3, 6, 'FORWARD', 'SYSTEMS_ENGINEERING', 11, 'https://storage.techcup.co/photos/andres_ruiz.jpg', true, 4, 'M', 20),
(4, 7, 'MIDFIELDER', 'CYBERSECURITY_ENGINEERING', 8, 'https://storage.techcup.co/photos/luis_diaz.jpg', true, 5, 'M', 21),
(5, 8, 'DEFENDER', 'STATISTICS_ENGINEERING', 3, 'https://storage.techcup.co/photos/sofia_martinez.jpg', true, 3, 'F', 19),
(6, 9, 'GOALKEEPER', 'SYSTEMS_ENGINEERING', 1, 'https://storage.techcup.co/photos/diego_gomez.jpg', true, 4, 'M', 20),
-- Jugadores Byte United
(7, 10, 'FORWARD', 'ARTIFICIAL_INTELLIGENCE_ENGINEERING', 7, 'https://storage.techcup.co/photos/valentina_rios.jpg', true, 6, 'F', 22),
(8, 11, 'MIDFIELDER', 'CYBERSECURITY_ENGINEERING', 6, 'https://storage.techcup.co/photos/sebastian_castro.jpg', true, 4, 'M', 20),
(9, 12, 'DEFENDER', 'STATISTICS_ENGINEERING', 4, 'https://storage.techcup.co/photos/camila_herrera.jpg', true, 3, 'F', 19),
(10, 13, 'GOALKEEPER', 'ARTIFICIAL_INTELLIGENCE_ENGINEERING', 1, 'https://storage.techcup.co/photos/felipe_mora.jpg', true, 2, 'M', 18);

SELECT setval('sport_profiles_id_seq', 10);

-- ============================================================
-- TABLA 3: tournaments (1 torneo)
-- ============================================================
INSERT INTO tournaments (id, name, start_date, end_date, team_count, cost_per_team, rules, inscription_close_date, created_by, status) VALUES
(1, 'TechCup Futbol 2026-1', '2026-04-01', '2026-06-15', 8, 150000.00,
 'REGLAMENTO TECHCUP 2026-1:
1. Cada equipo debe tener minimo 5 y maximo 12 jugadores inscritos.
2. Los partidos se juegan en dos tiempos de 25 minutos cada uno.
3. Tarjeta roja = suspension de 2 partidos. Doble amarilla en el mismo partido = roja.
4. Acumulacion de 3 amarillas en el torneo = 1 partido de suspension.
5. Solo pueden participar miembros activos de la Escuela Colombiana de Ingenieria.
6. El capitan es el unico representante oficial del equipo ante la organizacion.
7. Los pagos deben realizarse antes del cierre de inscripciones.
8. El formato del torneo sera fase de grupos + eliminacion directa.',
 '2026-03-25', 1, 'IN_PROGRESS');

SELECT setval('tournaments_id_seq', 1);

-- ============================================================
-- TABLA 4: tournament_dates (fechas importantes del torneo)
-- ============================================================
INSERT INTO tournament_dates (id, tournament_id, description, event_date) VALUES
(1, 1, 'Apertura de inscripciones', '2026-02-01'),
(2, 1, 'Cierre de inscripciones', '2026-03-25'),
(3, 1, 'Sorteo de grupos', '2026-03-28'),
(4, 1, 'Inicio fase de grupos', '2026-04-01'),
(5, 1, 'Fin fase de grupos', '2026-05-01'),
(6, 1, 'Inicio eliminacion directa', '2026-05-10'),
(7, 1, 'Gran Final', '2026-06-15');

SELECT setval('tournament_dates_id_seq', 7);

-- ============================================================
-- TABLA 5: teams (2 equipos)
-- ============================================================
INSERT INTO teams (id, name, shield_url, uniform_colors, tournament_id, captain_id) VALUES
(1, 'Ingenieros FC', 'https://storage.techcup.co/shields/ingenieros_fc.png', 'Azul y Blanco', 1, 4),
(2, 'Byte United', 'https://storage.techcup.co/shields/byte_united.png', 'Rojo y Negro', 1, 5);

SELECT setval('teams_id_seq', 2);

-- ============================================================
-- TABLA 6: team_players (jugadores en cada equipo)
-- ============================================================
-- Ingenieros FC: capitan (4) + 4 jugadores (6,7,8,9)
INSERT INTO team_players (team_id, player_id) VALUES
(1, 4),   -- Pedro (Capitan)
(1, 6),   -- Andres
(1, 7),   -- Luis
(1, 8),   -- Sofia
(1, 9),   -- Diego
-- Byte United: capitan (5) + 4 jugadores (10,11,12,13)
(2, 5),   -- Maria (Capitan)
(2, 10),  -- Valentina
(2, 11),  -- Sebastian
(2, 12),  -- Camila
(2, 13);  -- Felipe

-- ============================================================
-- TABLA 7: team_invitations (invitaciones de equipo)
-- ============================================================
INSERT INTO team_invitations (id, team_id, player_id, status, sent_at, responded_at) VALUES
-- Invitaciones aceptadas (jugadores ya en el equipo)
(1, 1, 6, 'ACCEPTED', '2026-02-10 09:00:00', '2026-02-10 12:00:00'),
(2, 1, 7, 'ACCEPTED', '2026-02-10 09:05:00', '2026-02-10 15:30:00'),
(3, 1, 8, 'ACCEPTED', '2026-02-10 09:10:00', '2026-02-11 08:00:00'),
(4, 1, 9, 'ACCEPTED', '2026-02-10 09:15:00', '2026-02-11 10:00:00'),
(5, 2, 10, 'ACCEPTED', '2026-02-12 14:00:00', '2026-02-12 16:00:00'),
(6, 2, 11, 'ACCEPTED', '2026-02-12 14:05:00', '2026-02-13 09:00:00'),
(7, 2, 12, 'ACCEPTED', '2026-02-12 14:10:00', '2026-02-13 11:00:00'),
(8, 2, 13, 'ACCEPTED', '2026-02-12 14:15:00', '2026-02-13 14:00:00'),
-- Invitacion rechazada
(9, 1, 10, 'REJECTED', '2026-02-11 08:00:00', '2026-02-11 20:00:00'),
-- Invitacion pendiente sin respuesta
(10, 2, 8, 'PENDING', '2026-02-14 10:00:00', NULL);

SELECT setval('team_invitations_id_seq', 10);

-- ============================================================
-- TABLA 8: payments (pagos de inscripcion)
-- ============================================================
INSERT INTO payments (id, team_id, receipt_url, status, reviewed_by, comments, created_at, updated_at) VALUES
-- Pago aprobado de Ingenieros FC
(1, 1, 'https://storage.techcup.co/receipts/ingenieros_fc_receipt.pdf', 'APPROVED', 1, 'Pago verificado correctamente. Monto completo recibido.', '2026-03-01 10:00:00', '2026-03-02 09:00:00'),
-- Pago en revision de Byte United
(2, 2, 'https://storage.techcup.co/receipts/byte_united_receipt.jpg', 'IN_REVIEW', NULL, NULL, '2026-03-05 15:00:00', '2026-03-05 15:00:00');

SELECT setval('payments_id_seq', 2);

-- ============================================================
-- TABLA 9: courts (canchas)
-- ============================================================
INSERT INTO courts (id, name, location, tournament_id) VALUES
(1, 'Cancha Principal', 'Bloque deportivo - Cancha sintetica norte', 1),
(2, 'Cancha Auxiliar', 'Bloque deportivo - Cancha sintetica sur', 1);

SELECT setval('courts_id_seq', 2);

-- ============================================================
-- TABLA 10: matches (partidos)
-- ============================================================
INSERT INTO matches (id, tournament_id, home_team_id, away_team_id, referee_id, court_id, match_date, match_time, phase, home_score, away_score, played) VALUES
-- Partido 1: Ya jugado (Ingenieros FC 2 - 1 Byte United)
(1, 1, 1, 2, 2, 1, '2026-04-01', '14:00:00', 'GROUP', 2, 1, true),
-- Partido 2: Programado (Byte United vs Ingenieros FC - vuelta)
(2, 1, 2, 1, 3, 2, '2026-04-15', '16:00:00', 'GROUP', NULL, NULL, false),
-- Partido 3: Futuro semifinal
(3, 1, 1, 2, 2, 1, '2026-05-10', '15:00:00', 'SEMI_FINAL', NULL, NULL, false);

SELECT setval('matches_id_seq', 3);

-- ============================================================
-- TABLA 11: match_events (eventos del partido 1)
-- FIX: columna correcta es event_minute (no minute)
-- ============================================================
INSERT INTO match_events (id, match_id, player_id, team_id, event_type, event_minute) VALUES
-- Gol de Andres (Ingenieros FC) min 15
(1, 1, 6, 1, 'GOAL', 15),
-- Tarjeta amarilla a Sebastian (Byte United) min 22
(2, 1, 11, 2, 'YELLOW_CARD', 22),
-- Gol de Valentina (Byte United) min 30 - empate
(3, 1, 10, 2, 'GOAL', 30),
-- Gol de Pedro Capitan (Ingenieros FC) min 42 - gol de la victoria
(4, 1, 4, 1, 'GOAL', 42),
-- Tarjeta amarilla a Luis (Ingenieros FC) min 45
(5, 1, 7, 1, 'YELLOW_CARD', 45),
-- Tarjeta roja a Sebastian (Byte United) min 48 - segunda amarilla
(6, 1, 11, 2, 'RED_CARD', 48);

SELECT setval('match_events_id_seq', 6);

-- ============================================================
-- TABLA 12: lineups (alineaciones del partido 1)
-- ============================================================
INSERT INTO lineups (id, match_id, team_id, formation) VALUES
(1, 1, 1, '1-2-1-1'),  -- Ingenieros FC formacion
(2, 1, 2, '1-2-1-1');  -- Byte United formacion

SELECT setval('lineups_id_seq', 2);

-- ============================================================
-- TABLA 13: lineup_players (jugadores en la alineacion)
-- FIX: columnas correctas son position_x, position_y (no positionx, positiony)
-- ============================================================
INSERT INTO lineup_players (id, lineup_id, player_id, position_x, position_y, starter) VALUES
-- Alineacion Ingenieros FC (lineup_id = 1)
(1, 1, 9, 50, 10, true),   -- Diego (Portero)
(2, 1, 8, 30, 30, true),   -- Sofia (Defensa izq)
(3, 1, 4, 70, 30, true),   -- Pedro Capitan (Defensa der)
(4, 1, 7, 50, 55, true),   -- Luis (Mediocampo)
(5, 1, 6, 50, 80, true),   -- Andres (Delantero)
-- Alineacion Byte United (lineup_id = 2)
(6, 2, 13, 50, 10, true),  -- Felipe (Portero)
(7, 2, 12, 30, 30, true),  -- Camila (Defensa izq)
(8, 2, 5, 70, 30, true),   -- Maria Capitan (Defensa der)
(9, 2, 11, 50, 55, true),  -- Sebastian (Mediocampo)
(10, 2, 10, 50, 80, true); -- Valentina (Delantera)

SELECT setval('lineup_players_id_seq', 10);

-- ============================================================
-- TABLA 14: standings (tabla de posiciones - fase de grupos)
-- ============================================================
INSERT INTO standings (id, tournament_id, team_id, played, won, drawn, lost, goals_for, goals_against, goal_difference, points) VALUES
-- Ingenieros FC: 1 jugado, 1 ganado, GF:2, GC:1, DG:+1, Pts:3
(1, 1, 1, 1, 1, 0, 0, 2, 1, 1, 3),
-- Byte United: 1 jugado, 0 ganados, 1 perdido, GF:1, GC:2, DG:-1, Pts:0
(2, 1, 2, 1, 0, 0, 1, 1, 2, -1, 0);

SELECT setval('standings_id_seq', 2);

-- ============================================================
-- TABLA 15: elimination_brackets (cuadro de eliminacion)
-- ============================================================
INSERT INTO elimination_brackets (id, tournament_id, round, match_position, match_id) VALUES
-- Semifinal 1 programada
(1, 1, 'SEMIFINAL', 1, 3);

SELECT setval('elimination_brackets_id_seq', 1);

-- ============================================================
-- TABLA 16: sanctions (sanciones)
-- ============================================================
INSERT INTO sanctions (id, tournament_id, player_id, match_id, description, sanction_date, suspended_matches) VALUES
-- Sebastian recibio roja -> 2 partidos de suspension
(1, 1, 11, 1, 'Expulsion por doble tarjeta amarilla en el partido Ingenieros FC vs Byte United. Conducta antideportiva.', '2026-04-01', 2);

SELECT setval('sanctions_id_seq', 1);

-- ============================================================
-- VERIFICACION: Contar registros en cada tabla
-- ============================================================
SELECT 'users' AS tabla, COUNT(*) AS registros FROM users
UNION ALL SELECT 'sport_profiles', COUNT(*) FROM sport_profiles
UNION ALL SELECT 'tournaments', COUNT(*) FROM tournaments
UNION ALL SELECT 'tournament_dates', COUNT(*) FROM tournament_dates
UNION ALL SELECT 'teams', COUNT(*) FROM teams
UNION ALL SELECT 'team_players', COUNT(*) FROM team_players
UNION ALL SELECT 'team_invitations', COUNT(*) FROM team_invitations
UNION ALL SELECT 'payments', COUNT(*) FROM payments
UNION ALL SELECT 'courts', COUNT(*) FROM courts
UNION ALL SELECT 'matches', COUNT(*) FROM matches
UNION ALL SELECT 'match_events', COUNT(*) FROM match_events
UNION ALL SELECT 'lineups', COUNT(*) FROM lineups
UNION ALL SELECT 'lineup_players', COUNT(*) FROM lineup_players
UNION ALL SELECT 'standings', COUNT(*) FROM standings
UNION ALL SELECT 'elimination_brackets', COUNT(*) FROM elimination_brackets
UNION ALL SELECT 'sanctions', COUNT(*) FROM sanctions
ORDER BY tabla;
