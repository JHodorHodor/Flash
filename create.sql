--DROP SCHEMA
DROP SCHEMA IF EXISTS flash CASCADE;

--DROP TABLES
DROP TABLE IF EXISTS FCwords CASCADE;
DROP TABLE IF EXISTS FClanguages CASCADE;
DROP TABLE IF EXISTS FCusers CASCADE;
DROP TABLE IF EXISTS FCwords_users CASCADE;
DROP TABLE IF EXISTS FClanguages_users CASCADE;
DROP TABLE IF EXISTS FCstats CASCADE;

--DROP TRIGGERS
DROP TRIGGER IF EXISTS delete_language ON FClanguages_users;

--DROP FUNCTIONS
DROP FUNCTION IF EXISTS delete_language();


CREATE SCHEMA flash;


--TABLES

CREATE TABLE FClanguages ( 
	language             varchar  PRIMARY KEY ,
	image                varchar  NOT NULL ,
	special_symbols      varchar ,
	quantity             integer NOT NULL
 );

CREATE TABLE FCwords ( 
	id                   SERIAL PRIMARY KEY ,
	language             varchar  NOT NULL REFERENCES FClanguages ,
	original             varchar   NOT NULL ,
	polish               varchar  NOT NULL ,

	CONSTRAINT unique_word UNIQUE(original,polish)
 );


CREATE TABLE FCusers ( 
	login                varchar PRIMARY KEY ,
	password             integer  NOT NULL ,
	start                date DEFAULT NOW() ,
	words_daily          integer   DEFAULT 10 ,

	CHECK(words_daily >= 0)
);

CREATE TABLE FCwords_users ( 
	login                varchar REFERENCES FCusers ,
	id                   integer  REFERENCES FCwords ,
	state                integer DEFAULT 0 ,
	history              varchar ,
	to_delete            boolean DEFAULT FALSE ,

	CONSTRAINT unique_word_users UNIQUE(login,id),
	CHECK(state >= 0 AND state < 17)
);

CREATE TABLE FClanguages_users ( 
	login                varchar REFERENCES FCusers ,
	language             varchar  REFERENCES FClanguages ,
	start                date DEFAULT NOW() ,

	CONSTRAINT unique_languages_users UNIQUE(login,language)
);

CREATE TABLE FCstats ( 
	login                varchar REFERENCES FCusers ,
	day             	 date DEFAULT NOW() ,
	language             varchar  REFERENCES FClanguages ,
	good                 integer ,
	aall                 integer ,
	ttime                integer ,

	CONSTRAINT unique_stats UNIQUE(login,day,language)
);

CREATE OR REPLACE FUNCTION delete_language() RETURNS trigger AS $$
BEGIN

	DELETE FROM FCstats WHERE login = OLD.login AND language = OLD.language;
	DELETE FROM FCwords_users wu WHERE login = OLD.login AND 
		(SELECT language FROM FCwords w WHERE w.id = wu.id) = OLD.language;
	RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_language BEFORE DELETE ON FClanguages_users
FOR EACH ROW EXECUTE PROCEDURE delete_language();

INSERT INTO FClanguages VALUES
('''DANISH''', 'dan', 'å Å æ Æ é É ø Ø', 4),
('''DUTCH''', 'dut', 'é É ë Ë ï Ï ó Ó ö Ö ü Ü', 6),
('''ENGLISH''', 'eng', '', 0),
('''ESPERANTO''', 'esp', 'ĉ Ĉ ĝ Ĝ ĥ Ĥ ĵ Ĵ ŝ Ŝ ŭ Ŭ', 6),
('''FINNISH''', 'fin', 'ä Ä å Å ö Ö', 3),
('''FRENCH''', 'fre', 'à À â Â æ Æ ç Ç é É è È ê Ê ë Ë ï Ï î Î ô Ô œ Œ ù Ù û Û ü Ü ÿ Ÿ', 0),
('''GERMAN''', 'ger', 'ß ß ä Ä ö Ö ü Ü', 4),
('''HUNGARIAN''', 'hun', 'á Á é É í Í ö Ö ó Ó ő Ő ü Ü ú Ú ű Ű', 9),
('''ICELANDIC''', 'ice', 'á Á æ Æ ð Ð é É í Í ö Ö þ Þ ú Ú ý Ý', 9),
('''ITALIAN''', 'ita', 'à À è È é É ì Ì ò Ò ó Ó ù Ù', 7),
('''NORWEGIAN''', 'nor', 'å Å æ Æ â Â é É è È ê Ê ø Ø ò Ò ô Ô', 9),
('''PORTUGUESE''', 'por', 'ã Ã á Á à À â Â ç Ç é É ê Ê í Í õ Õ ó Ó ô Ô ú Ú ü Ü', 0),
('''ROMANIAN''', 'rom', 'ă Ă â Â î Î ș Ș ş Ş ț Ț ţ Ţ', 7),
('''SPANISH''', 'spa', 'ñ Ñ á Á é É í Í ó Ó ú Ú', 6),
('''SWEDISH''', 'swe', 'ä Ä å Å é É ö Ö', 4),
('''TURKISH''', 'tur', 'â Â ç Ç ğ Ğ ı I İ İ î Î ö Ö ş Ş ü Ü û û', 0),
('''WELSH''', 'wel', 'â Â ê Ê î Î ô Ô û Û ŵ Ŵ ŷ Ŷ', 7);

