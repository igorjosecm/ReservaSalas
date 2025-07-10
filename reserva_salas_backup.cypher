:begin
CREATE CONSTRAINT constraint_9132c24f FOR (node:Sala) REQUIRE (node.codigo_sala) IS UNIQUE;
CREATE CONSTRAINT constraint_96c938cb FOR (node:Materia) REQUIRE (node.codigo_materia) IS UNIQUE;
CREATE CONSTRAINT constraint_bb804735 FOR (node:Reserva) REQUIRE (node.id_reserva) IS UNIQUE;
CREATE CONSTRAINT constraint_da96deba FOR (node:Professor) REQUIRE (node.matricula_professor) IS UNIQUE;
CREATE CONSTRAINT constraint_ecd40aff FOR (node:Bloco) REQUIRE (node.codigo_bloco) IS UNIQUE;
CREATE CONSTRAINT UNIQUE_IMPORT_NAME FOR (node:`UNIQUE IMPORT LABEL`) REQUIRE (node.`UNIQUE IMPORT ID`) IS UNIQUE;
:commit
CALL db.awaitIndexes(300);
:begin
UNWIND [{codigo_bloco:"B", properties:{num_andares:3, nome_bloco:"Bloco B"}}, {codigo_bloco:"A", properties:{num_andares:3, nome_bloco:"Bloco A"}}] AS row
CREATE (n:Bloco{codigo_bloco: row.codigo_bloco}) SET n += row.properties;
UNWIND [{codigo_sala:"A102", properties:{andar:1, nome_sala:"Sala A102", capacidade:36}}, {codigo_sala:"A103", properties:{andar:1, nome_sala:"Sala A103", capacidade:50}}, {codigo_sala:"A101", properties:{andar:1, nome_sala:"Sala A101", capacidade:30}}, {codigo_sala:"A201", properties:{andar:2, nome_sala:"Sala A201", capacidade:36}}] AS row
CREATE (n:Sala{codigo_sala: row.codigo_sala}) SET n += row.properties;
UNWIND [{matricula_professor:20250001, properties:{data_nascimento:date('1997-12-25'), email:"rebeca.schroeder@postgres.br", nome_completo:"Rebeca Schroeder Freitas"}}] AS row
CREATE (n:Professor{matricula_professor: row.matricula_professor}) SET n += row.properties;
UNWIND [{_id:12, properties:{current_id:1, name:"reserva_id"}}] AS row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Sequence;
UNWIND [{codigo_materia:"BAN2", properties:{carga_horaria:72, nome_materia:"Banco de Dados II"}}, {codigo_materia:"BAN1", properties:{carga_horaria:72, nome_materia:"Banco de Dados"}}] AS row
CREATE (n:Materia{codigo_materia: row.codigo_materia}) SET n += row.properties;
UNWIND [{id_reserva:1, properties:{fim_reserva:localdatetime('2025-07-10T22:00:00'), inicio_reserva:localdatetime('2025-07-10T19:00:00')}}, {id_reserva:0, properties:{fim_reserva:localdatetime('2025-05-08T11:00:00'), inicio_reserva:localdatetime('2025-05-08T09:00:00')}}] AS row
CREATE (n:Reserva{id_reserva: row.id_reserva}) SET n += row.properties;
:commit
:begin
UNWIND [{start: {id_reserva:1}, end: {codigo_materia:"BAN2"}, properties:{}}, {start: {id_reserva:0}, end: {codigo_materia:"BAN2"}, properties:{}}] AS row
MATCH (start:Reserva{id_reserva: row.start.id_reserva})
MATCH (end:Materia{codigo_materia: row.end.codigo_materia})
CREATE (start)-[r:REFERENTE_A]->(end) SET r += row.properties;
UNWIND [{start: {matricula_professor:20250001}, end: {codigo_materia:"BAN2"}, properties:{fim_periodo:date('2026-12-03'), inicio_periodo:date('2026-06-03')}}] AS row
MATCH (start:Professor{matricula_professor: row.start.matricula_professor})
MATCH (end:Materia{codigo_materia: row.end.codigo_materia})
CREATE (start)-[r:LECIONA]->(end) SET r += row.properties;
UNWIND [{start: {codigo_sala:"A102"}, end: {codigo_bloco:"A"}, properties:{}}, {start: {codigo_sala:"A103"}, end: {codigo_bloco:"A"}, properties:{}}, {start: {codigo_sala:"A101"}, end: {codigo_bloco:"A"}, properties:{}}, {start: {codigo_sala:"A201"}, end: {codigo_bloco:"A"}, properties:{}}] AS row
MATCH (start:Sala{codigo_sala: row.start.codigo_sala})
MATCH (end:Bloco{codigo_bloco: row.end.codigo_bloco})
CREATE (start)-[r:LOCALIZADA_EM]->(end) SET r += row.properties;
UNWIND [{start: {id_reserva:1}, end: {codigo_sala:"A101"}, properties:{}}, {start: {id_reserva:0}, end: {codigo_sala:"A102"}, properties:{}}] AS row
MATCH (start:Reserva{id_reserva: row.start.id_reserva})
MATCH (end:Sala{codigo_sala: row.end.codigo_sala})
CREATE (start)-[r:FEITA_PARA]->(end) SET r += row.properties;
UNWIND [{start: {id_reserva:1}, end: {matricula_professor:20250001}, properties:{}}, {start: {id_reserva:0}, end: {matricula_professor:20250001}, properties:{}}] AS row
MATCH (start:Reserva{id_reserva: row.start.id_reserva})
MATCH (end:Professor{matricula_professor: row.end.matricula_professor})
CREATE (start)-[r:REALIZADA_POR]->(end) SET r += row.properties;
:commit
:begin
MATCH (n:`UNIQUE IMPORT LABEL`)  WITH n LIMIT 20000 REMOVE n:`UNIQUE IMPORT LABEL` REMOVE n.`UNIQUE IMPORT ID`;
:commit
:begin
DROP CONSTRAINT UNIQUE_IMPORT_NAME;
:commit
