--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: reserva_salas; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA reserva_salas;


ALTER SCHEMA reserva_salas OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: bloco; Type: TABLE; Schema: reserva_salas; Owner: postgres
--

CREATE TABLE reserva_salas.bloco (
    codigo_bloco character(1) NOT NULL,
    nome_bloco text NOT NULL,
    num_andares smallint
);


ALTER TABLE reserva_salas.bloco OWNER TO postgres;

--
-- Name: materia; Type: TABLE; Schema: reserva_salas; Owner: postgres
--

CREATE TABLE reserva_salas.materia (
    codigo_materia character(6) NOT NULL,
    nome_materia text NOT NULL,
    carga_horaria smallint
);


ALTER TABLE reserva_salas.materia OWNER TO postgres;

--
-- Name: materia_professor; Type: TABLE; Schema: reserva_salas; Owner: postgres
--

CREATE TABLE reserva_salas.materia_professor (
    matricula_professor integer NOT NULL,
    codigo_materia character(6) NOT NULL,
    inicio_periodo date NOT NULL,
    fim_periodo date NOT NULL
);


ALTER TABLE reserva_salas.materia_professor OWNER TO postgres;

--
-- Name: professor; Type: TABLE; Schema: reserva_salas; Owner: postgres
--

CREATE TABLE reserva_salas.professor (
    matricula_professor integer NOT NULL,
    nome_completo text NOT NULL,
    data_nascimento date,
    email character varying(100)
);


ALTER TABLE reserva_salas.professor OWNER TO postgres;

--
-- Name: reserva; Type: TABLE; Schema: reserva_salas; Owner: postgres
--

CREATE TABLE reserva_salas.reserva (
    id_reserva integer NOT NULL,
    codigo_sala character(4) NOT NULL,
    matricula_professor integer NOT NULL,
    codigo_materia character(6) NOT NULL,
    data_inicio date NOT NULL,
    data_fim date NOT NULL,
    hora_inicio time without time zone NOT NULL,
    hora_fim time without time zone NOT NULL
);


ALTER TABLE reserva_salas.reserva OWNER TO postgres;

--
-- Name: reserva_id_reserva_seq; Type: SEQUENCE; Schema: reserva_salas; Owner: postgres
--

CREATE SEQUENCE reserva_salas.reserva_id_reserva_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE reserva_salas.reserva_id_reserva_seq OWNER TO postgres;

--
-- Name: reserva_id_reserva_seq; Type: SEQUENCE OWNED BY; Schema: reserva_salas; Owner: postgres
--

ALTER SEQUENCE reserva_salas.reserva_id_reserva_seq OWNED BY reserva_salas.reserva.id_reserva;


--
-- Name: sala; Type: TABLE; Schema: reserva_salas; Owner: postgres
--

CREATE TABLE reserva_salas.sala (
    codigo_sala character(4) NOT NULL,
    codigo_bloco character(1) NOT NULL,
    nome_sala text NOT NULL,
    andar smallint,
    capacidade smallint
);


ALTER TABLE reserva_salas.sala OWNER TO postgres;

--
-- Name: reserva id_reserva; Type: DEFAULT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.reserva ALTER COLUMN id_reserva SET DEFAULT nextval('reserva_salas.reserva_id_reserva_seq'::regclass);


--
-- Data for Name: bloco; Type: TABLE DATA; Schema: reserva_salas; Owner: postgres
--

INSERT INTO reserva_salas.bloco VALUES ('B', 'Bloco B', 3);
INSERT INTO reserva_salas.bloco VALUES ('A', 'Bloco A2', 4);


--
-- Data for Name: materia; Type: TABLE DATA; Schema: reserva_salas; Owner: postgres
--

INSERT INTO reserva_salas.materia VALUES ('BAN2  ', 'Banco de Dados II', 72);
INSERT INTO reserva_salas.materia VALUES ('BAN1  ', 'Banco de Dados I', 72);


--
-- Data for Name: materia_professor; Type: TABLE DATA; Schema: reserva_salas; Owner: postgres
--

INSERT INTO reserva_salas.materia_professor VALUES (20250001, 'BAN2  ', '2025-02-03', '2025-07-19');
INSERT INTO reserva_salas.materia_professor VALUES (20250001, 'BAN1  ', '2025-05-10', '2025-07-15');


--
-- Data for Name: professor; Type: TABLE DATA; Schema: reserva_salas; Owner: postgres
--

INSERT INTO reserva_salas.professor VALUES (20250001, 'Rebeca Schroeder Freitas', '1997-12-25', 'rebeca.schroeder@postgres.br');
INSERT INTO reserva_salas.professor VALUES (2, 'Teste gabriel', '2000-12-11', 'gabrielzapellaro@gmail.com');


--
-- Data for Name: reserva; Type: TABLE DATA; Schema: reserva_salas; Owner: postgres
--

INSERT INTO reserva_salas.reserva VALUES (0, 'A102', 20250001, 'BAN2  ', '2025-05-08', '2025-05-08', '09:00:00', '11:00:00');


--
-- Data for Name: sala; Type: TABLE DATA; Schema: reserva_salas; Owner: postgres
--

INSERT INTO reserva_salas.sala VALUES ('A102', 'A', 'Sala A102', 1, 36);
INSERT INTO reserva_salas.sala VALUES ('A103', 'A', 'Sala A103', 1, 50);
INSERT INTO reserva_salas.sala VALUES ('A101', 'A', 'Sala A101', 1, 30);


--
-- Name: reserva_id_reserva_seq; Type: SEQUENCE SET; Schema: reserva_salas; Owner: postgres
--

SELECT pg_catalog.setval('reserva_salas.reserva_id_reserva_seq', 1, true);


--
-- Name: bloco bloco_pkey; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.bloco
    ADD CONSTRAINT bloco_pkey PRIMARY KEY (codigo_bloco);


--
-- Name: materia materia_pkey; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.materia
    ADD CONSTRAINT materia_pkey PRIMARY KEY (codigo_materia);


--
-- Name: materia_professor materia_professor_pkey; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.materia_professor
    ADD CONSTRAINT materia_professor_pkey PRIMARY KEY (matricula_professor, codigo_materia);


--
-- Name: professor professor_email_key; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.professor
    ADD CONSTRAINT professor_email_key UNIQUE (email);


--
-- Name: professor professor_pkey; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.professor
    ADD CONSTRAINT professor_pkey PRIMARY KEY (matricula_professor);


--
-- Name: reserva reserva_pkey; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.reserva
    ADD CONSTRAINT reserva_pkey PRIMARY KEY (id_reserva);


--
-- Name: sala sala_pkey; Type: CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.sala
    ADD CONSTRAINT sala_pkey PRIMARY KEY (codigo_sala);


--
-- Name: materia_nome_materia_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX materia_nome_materia_idx ON reserva_salas.materia USING btree (nome_materia);


--
-- Name: materia_professor_inicio_periodo_fim_periodo_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX materia_professor_inicio_periodo_fim_periodo_idx ON reserva_salas.materia_professor USING btree (inicio_periodo, fim_periodo);


--
-- Name: professor_nome_completo_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX professor_nome_completo_idx ON reserva_salas.professor USING btree (nome_completo);


--
-- Name: reserva_codigo_materia_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX reserva_codigo_materia_idx ON reserva_salas.reserva USING btree (codigo_materia);


--
-- Name: reserva_codigo_sala_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX reserva_codigo_sala_idx ON reserva_salas.reserva USING btree (codigo_sala);


--
-- Name: reserva_data_inicio_data_fim_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX reserva_data_inicio_data_fim_idx ON reserva_salas.reserva USING btree (data_inicio, data_fim);


--
-- Name: reserva_matricula_professor_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX reserva_matricula_professor_idx ON reserva_salas.reserva USING btree (matricula_professor);


--
-- Name: sala_andar_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX sala_andar_idx ON reserva_salas.sala USING btree (andar);


--
-- Name: sala_codigo_bloco_idx; Type: INDEX; Schema: reserva_salas; Owner: postgres
--

CREATE INDEX sala_codigo_bloco_idx ON reserva_salas.sala USING btree (codigo_bloco);


--
-- Name: materia_professor materia_professor_codigo_materia_fkey; Type: FK CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.materia_professor
    ADD CONSTRAINT materia_professor_codigo_materia_fkey FOREIGN KEY (codigo_materia) REFERENCES reserva_salas.materia(codigo_materia);


--
-- Name: materia_professor materia_professor_matricula_professor_fkey; Type: FK CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.materia_professor
    ADD CONSTRAINT materia_professor_matricula_professor_fkey FOREIGN KEY (matricula_professor) REFERENCES reserva_salas.professor(matricula_professor);


--
-- Name: reserva reserva_codigo_materia_fkey; Type: FK CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.reserva
    ADD CONSTRAINT reserva_codigo_materia_fkey FOREIGN KEY (codigo_materia) REFERENCES reserva_salas.materia(codigo_materia);


--
-- Name: reserva reserva_codigo_sala_fkey; Type: FK CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.reserva
    ADD CONSTRAINT reserva_codigo_sala_fkey FOREIGN KEY (codigo_sala) REFERENCES reserva_salas.sala(codigo_sala);


--
-- Name: reserva reserva_matricula_professor_fkey; Type: FK CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.reserva
    ADD CONSTRAINT reserva_matricula_professor_fkey FOREIGN KEY (matricula_professor) REFERENCES reserva_salas.professor(matricula_professor);


--
-- Name: sala sala_codigo_bloco_fkey; Type: FK CONSTRAINT; Schema: reserva_salas; Owner: postgres
--

ALTER TABLE ONLY reserva_salas.sala
    ADD CONSTRAINT sala_codigo_bloco_fkey FOREIGN KEY (codigo_bloco) REFERENCES reserva_salas.bloco(codigo_bloco);


--
-- PostgreSQL database dump complete
--

