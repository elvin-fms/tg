CREATE USER IF NOT EXISTS SA SALT '68597c0f06e84117' HASH 'adbc2b70d7ff92396e46d1259eeb7a99f0e4516e10d8cd5b9b3a02d91ab5a510' ADMIN;            
CREATE SEQUENCE SYSTEM_SEQUENCE_RMA_BOGIE_WSET_CLASS_XREF START WITH 1000 BELONGS_TO_TABLE;   
CREATE SEQUENCE SYSTEM_SEQUENCE_RMA_WODET START WITH 1000 BELONGS_TO_TABLE; 
CREATE SEQUENCE SYSTEM_SEQUENCE_RMA_WAGON_BOGIE_CLASS_XREF START WITH 1000 BELONGS_TO_TABLE;  
CREATE SEQUENCE SYSTEM_SEQUENCE_RMA_WAGONCLASS START WITH 1000 BELONGS_TO_TABLE;   
CREATE SEQUENCE SYSTEM_SEQUENCE_RMA_ROTCLASS START WITH 1000 BELONGS_TO_TABLE;   
CREATE SEQUENCE COMMON_RMA_SEQUENCE START WITH 10000;

CREATE TABLE UNIQUE_ID
(
    _ID INT NOT NULL PRIMARY KEY,
	NEXT_VALUE INT NOT NULL,
	_VERSION INT NOT NULL DEFAULT 0
);

CREATE TABLE NUMBERS
(
    _ID INT NOT NULL IDENTITY PRIMARY KEY,
    _VERSION INT NOT NULL DEFAULT 0,
    NUMBKEY varchar(255),
    WONOINC varchar(255)
);
CREATE UNIQUE INDEX UI_NUMBERS ON NUMBERS(NUMBKEY);

CREATE TABLE INSPECTED_ENTITIES
(
	ID_COLUMN INT NOT NULL IDENTITY PRIMARY KEY,
	KEY VARCHAR(20) NOT NULL,
	DESC VARCHAR(255),
	INT_PROPERTY INT,
	DECIMAL_PROPERTY NUMERIC,
	MONEY_PROPERTY NUMERIC,
	MONEY_PROPERTY_CURRENCY VARCHAR(3),
	DATE_PROPERTY TIMESTAMP,
	BOOLEAN_PROPERTY CHAR(1) NOT NULL DEFAULT 'Y',
	ENTITY_PROPERTY_ONE INT,
	ENTITY_PROPERTY_TWO INT
);

CREATE TEMPORARY TABLE MONEY_CLASS_TABLE
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(20) NOT NULL,
	DESC_ VARCHAR(255),
	MONEY_AMOUNT NUMERIC,
	MONEY_TAXAMOUNT NUMERIC,
	MONEY_CURRENCY VARCHAR(3),
	DATE_TIME TIMESTAMP,
	TRANS_DATE_TIME TIMESTAMP
);

CREATE TEMPORARY TABLE SIMPLE_MONEY_CLASS_TABLE
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(20) NOT NULL,
	DESC_ VARCHAR(255),
	MONEY NUMERIC
);

CREATE TEMPORARY TABLE SIMPLE_TAX_MONEY_CLASS_TABLE
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(20) NOT NULL,
	DESC_ VARCHAR(255),
	MONEY_AMOUNT NUMERIC,
	MONEY_TAXAMOUNT NUMERIC
);

CREATE TEMPORARY TABLE SIMPLE_TAX_AND_EX_TAX_MONEY_CLASS_TABLE
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(20) NOT NULL,
	DESC_ VARCHAR(255),
	MONEY_EXTAXAMOUNT NUMERIC,
	MONEY_TAXAMOUNT NUMERIC
);

CREATE TEMPORARY TABLE ENTITY_WITH_COMPOSITE_KEY
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_PART_ONE VARCHAR(20) NOT NULL,
	MONEY_CLASS_ID INT NOT NULL,
	DESC_ VARCHAR(255)
);

CREATE TEMPORARY TABLE RMA_ROTCLASS(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR SYSTEM_SEQUENCE_RMA_ROTCLASS) NOT NULL NULL_TO_DEFAULT SEQUENCE SYSTEM_SEQUENCE_RMA_ROTCLASS,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	EQCLASS VARCHAR(8),
    EQCLASS_DESC VARCHAR(60),
    CLASS_TYPE VARCHAR(2),
    TONNAGE BIGINT NOT NULL
);       

CREATE TEMPORARY TABLE RMA_BOGIE_WSET_CLASS_XREF(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR SYSTEM_SEQUENCE_RMA_BOGIE_WSET_CLASS_XREF) NOT NULL NULL_TO_DEFAULT SEQUENCE SYSTEM_SEQUENCE_RMA_BOGIE_WSET_CLASS_XREF,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    BOGIE_CLASS BIGINT NOT NULL,
    WSET_CLASS BIGINT NOT NULL,
    RECORD_STATUS VARCHAR(1)
);     

CREATE UNIQUE INDEX PRIMARY_KEY_29 ON RMA_BOGIE_WSET_CLASS_XREF(ID_COLUMN);  

CREATE TEMPORARY TABLE RMA_WAGONCLASS(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR SYSTEM_SEQUENCE_RMA_WAGONCLASS) NOT NULL NULL_TO_DEFAULT SEQUENCE SYSTEM_SEQUENCE_RMA_WAGONCLASS,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    EQCLASS VARCHAR(8),
    EQCLASS_DESC VARCHAR(60),
    EQUIP_TYPE VARCHAR(8),
    NO_OF_BOGIES BIGINT,
    NO_OF_WHEELSETS BIGINT,
    TONNAGE BIGINT NOT NULL
);

CREATE TEMPORARY TABLE RMA_WAGON_BOGIE_CLASS_XREF(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR SYSTEM_SEQUENCE_RMA_WAGON_BOGIE_CLASS_XREF) NOT NULL NULL_TO_DEFAULT SEQUENCE SYSTEM_SEQUENCE_RMA_WAGON_BOGIE_CLASS_XREF,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    WAGON_CLASS BIGINT NOT NULL,
    BOGIE_CLASS BIGINT NOT NULL,
    RECORD_STATUS VARCHAR(1)
);   

CREATE TEMPORARY TABLE RMA_WAGON(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR COMMON_RMA_SEQUENCE) NOT NULL NULL_TO_DEFAULT SEQUENCE COMMON_RMA_SEQUENCE,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    EQUIPNO VARCHAR(16) NOT NULL,
    EQ_DESC VARCHAR(60),
    EQCLASS BIGINT NOT NULL,
    SERIALNO VARCHAR(6),
    TAB_SEL_FLG VARCHAR(1),
    EQUIP_STATUS VARCHAR(4),
    EQUIP_LOCATION VARCHAR(3),
    EQUIP_CATEGORY VARCHAR(6),
    EQUIP_TYPE VARCHAR(6),
    CHANGE_BUSINESS_UNIT VARCHAR(1),
    GAUGE_TYPE VARCHAR(8)
);     

CREATE TEMPORARY TABLE RMA_WAGON_SLOT(
	ID_COLUMN BIGINT NOT NULL,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	WAGON BIGINT NOT NULL,
	POSITION INT NOT NULL,
	BOGIE BIGINT
);
CREATE UNIQUE INDEX UI_WAGON_SLOT ON RMA_WAGON_SLOT(WAGON, POSITION);  

CREATE TEMPORARY TABLE RMA_BOGIE_SLOT(
	ID_COLUMN BIGINT NOT NULL,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	BOGIE BIGINT NOT NULL,
	POSITION INT NOT NULL,
	WHEELSET BIGINT
);
CREATE UNIQUE INDEX UI_BOGIE_SLOT ON RMA_BOGIE_SLOT(BOGIE, POSITION);  

CREATE INDEX EQCLASS_WAGON ON RMA_WAGON(EQCLASS);    
CREATE INDEX WAGON_EQUIP_TYPE ON RMA_WAGON(EQUIP_TYPE);              
CREATE UNIQUE INDEX PK_WAGON ON RMA_WAGON(EQUIPNO);  

CREATE TEMPORARY TABLE RMA_WORKSHOP(
   	ID_COLUMN BIGINT NOT NULL,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    WORKSHOP VARCHAR(6) NOT NULL,
    WORKSHOP_DESC VARCHAR(40),
	IS_CONTRACTOR VARCHAR(1) NOT NULL DEFAULT 'N'	
);

CREATE TEMPORARY TABLE RMA_ADVICE_POSITION(
	ID_COLUMN BIGINT NOT NULL,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	ADVICE BIGINT NOT NULL,
	POSITION INTEGER NOT NULL,
	ROTABLE BIGINT,
	ROTABLE_TYPE VARCHAR(2),
	SENDING_WORKSHOP BIGINT,
	RECEIVING_WORKSHOP BIGINT,
	PLACED_BY BIGINT,
	PLACEMENT_DATE TIMESTAMP,
	IS_RECEIVED VARCHAR(1) NOT NULL,
	RECEIVED_DATE TIMESTAMP,
    REMOVE_BEARING VARCHAR(1) DEFAULT 'N'
);

CREATE TEMPORARY TABLE RMA_PERSON (
	ID_COLUMN BIGINT NOT NULL,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	PERSON_NO VARCHAR(60) NOT NULL,
	PERSON_DESC VARCHAR(250) NOT NULL,
	PASSWORD VARCHAR(60) NOT NULL
);

CREATE TEMPORARY TABLE USER_ROLE (
	_ID BIGINT NOT NULL,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(60) NOT NULL,
	DESC_ VARCHAR(250) NOT NULL
);

CREATE TEMPORARY TABLE USER_ROLE_ASSOCIATION (
	_ID BIGINT NOT NULL,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	ID_CRAFT BIGINT NOT NULL,
	ID_USER_ROLE BIGINT NOT NULL
);

CREATE TABLE SECURITY_ROLE_ASSOCIATION (
	_ID BIGINT NOT NULL,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	TOKEN VARCHAR(250) NOT NULL,
	ID_USER_ROLE BIGINT NOT NULL
);

CREATE TEMPORARY TABLE RMA_WODET(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR SYSTEM_SEQUENCE_RMA_WODET) NOT NULL NULL_TO_DEFAULT SEQUENCE SYSTEM_SEQUENCE_RMA_WODET,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    WONO VARCHAR(8) NOT NULL,
    IMPORTANT_PROPERTY VARCHAR(255),
    EQUIPNO BIGINT NOT NULL,
    WOTYPE VARCHAR(4),
    WO_DESC VARCHAR(250),
    PRTY BIGINT,
    PLANNER VARCHAR(4),
    MAINT_SUPER VARCHAR(8),
    DURATION DECIMAL(10, 2),
    AREACD VARCHAR(4),
    WONOPM VARCHAR(8),
    ROTABLE_NO VARCHAR(16),
    NO_OF_DEFECTS BIGINT,
    METER_FLAG VARCHAR(1),
    ORGUNIT VARCHAR(8),
    JOB_NO VARCHAR(10),
    PROJECT_NO VARCHAR(20),
    COST_CENTRE VARCHAR(8),
    INVOICEABLE VARCHAR(1),
    ORIGINATOR VARCHAR(8),
    SUPERVISOR VARCHAR(8),
    EARLY_START DATE,
    EARLY_START_TIME BIGINT,
    EARLY_FINISH DATE,
    EARLY_FINISH_TIME BIGINT,
    ACTUAL_START DATE,
    ACTUAL_START_TIME BIGINT,
    ACTUAL_FINISH DATE,
    ACTUAL_FINISH_TIME BIGINT,
    TRANS_DATE DATE,
    TRANS_DATE_TIME BIGINT,
    ACTIVITY_CODE VARCHAR(4),
    WO_STATUS VARCHAR(1),
    VENDORNO VARCHAR(10),
    PERMIT VARCHAR(2),
    OC_FLAG VARCHAR(1),
    MAN_COST DECIMAL(10, 2),
    BILL_COST DECIMAL(10, 2),
    SPEQ_COST DECIMAL(10, 2),
    IND_COST DECIMAL(10, 2),
    DC_COST DECIMAL(10, 2),
    CONT_COST DECIMAL(10, 2),
    MAN_EST DECIMAL(10, 2),
    BILL_EST DECIMAL(10, 2),
    SPEQ_EST DECIMAL(10, 2),
    IND_EST DECIMAL(10, 2),
    DC_EST DECIMAL(10, 2),
    CONT_EST DECIMAL(10, 2),
    OUTAGE DECIMAL(10, 2),
    MAN_COM DECIMAL(10, 2),
    BILL_COM DECIMAL(10, 2),
    DC_COM DECIMAL(10, 2),
    SPEQ_COM DECIMAL(10, 2),
    IND_COM DECIMAL(10, 2),
    MAN_BUDG DECIMAL(10, 2),
    BILL_BUDG DECIMAL(10, 2),
    SPEQ_BUDG DECIMAL(10, 2),
    IND_BUDG DECIMAL(10, 2),
    DC_BUDG DECIMAL(10, 2),
    FAILURE_TYPE VARCHAR(4),
    REPAIR_CODE VARCHAR(4),
    SHUTDOWN_CODE VARCHAR(6),
    COMPLEX_CODE VARCHAR(2),
    PROCESS_SHIFT VARCHAR(1),
    CAUSE_CODE VARCHAR(4),
    MOD_NO VARCHAR(7),
    MOD_APPROVED VARCHAR(1),
    TRADE_GROUP VARCHAR(3),
    SAFETY_JOB_FLAG VARCHAR(1),
    PRINT_FLAG VARCHAR(1),
    MOD_REQUIRED VARCHAR(1),
    GL_ACCOUNT_CODE VARCHAR(17),
    FACTORY_REP VARCHAR(4),
    TASK_EST DECIMAL(10, 2),
    TASK_COST DECIMAL(10, 2),
    TASK_COM DECIMAL(10, 2),
    TASK_BUDG DECIMAL(10, 2),
    WORKSHOP BIGINT,
    SCHED_COMMENTS VARCHAR(60),
    CLOSE_DATE DATE,
    WHSE VARCHAR(4),
    PART_NO VARCHAR(20),
    MANUF_QTY BIGINT,
    RAKE_ID VARCHAR(10),
    PONO VARCHAR(8),
    AGREED_VARIATION VARCHAR(1),
    PROJECT_SYSTEM_GENERATED VARCHAR(1)
);   

CREATE TEMPORARY TABLE RMA_BOGIE (
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR COMMON_RMA_SEQUENCE) NOT NULL NULL_TO_DEFAULT SEQUENCE COMMON_RMA_SEQUENCE,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    ROTABLE_NO VARCHAR(16) NOT NULL,
    ROTABLE_DESC VARCHAR(60) NOT NULL,
    RECLASSIFICATION_DATE DATE,
    PREVIOUS_ROTABLE_NO VARCHAR(16),
    LAST_METER_READING BIGINT,
    EQCLASS BIGINT NOT NULL,
    LAST_READING_DATE DATE,
    WHSE VARCHAR(4),
    INSTALL_DATE DATE,
    LAST_CC_ENTRY BIGINT, 
    CURRENT_LOCATION BIGINT NOT NULL,
	CURRENT_LOCATION_TYPE VARCHAR(6) NOT NULL,
    ROTABLE_STATUS CHAR(1) NOT NULL
);      

CREATE TEMPORARY TABLE RMA_WHEELSET (
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR COMMON_RMA_SEQUENCE) NOT NULL NULL_TO_DEFAULT SEQUENCE COMMON_RMA_SEQUENCE,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
    ROTABLE_NO VARCHAR(16) NOT NULL,
    ROTABLE_DESC VARCHAR(60) NOT NULL,
    RECLASSIFICATION_DATE DATE,
    PREVIOUS_ROTABLE_NO VARCHAR(16),
    LAST_METER_READING BIGINT,
    EQCLASS BIGINT NOT NULL,
    LAST_READING_DATE DATE,
    WHSE VARCHAR(4),
    INSTALL_DATE DATE,
    LAST_CC_ENTRY BIGINT,
    CURRENT_LOCATION BIGINT NOT NULL,
	CURRENT_LOCATION_TYPE VARCHAR(6) NOT NULL,
    ROTABLE_STATUS CHAR(1) NOT NULL
);      

CREATE TEMPORARY TABLE RMA_BOGIE_MOVEMENTS (
	ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR COMMON_RMA_SEQUENCE) NOT NULL NULL_TO_DEFAULT SEQUENCE COMMON_RMA_SEQUENCE,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	BOGIE BIGINT NOT NULL,
	IN_LOCATION BIGINT NOT NULL,
	IN_LOCATION_TYPE VARCHAR(6) NOT NULL,
	IN_DATE DATE NOT NULL,
	IN_TRANS_DATE DATE NOT NULL,
	IN_USER	BIGINT NOT NULL,
	OUT_LOCATION BIGINT,
	OUT_LOCATION_TYPE VARCHAR(6),
	OUT_DATE DATE,
	OUT_TRANS_DATE DATE,
	OUT_USER BIGINT
);

CREATE UNIQUE INDEX UI_BOGIE_MOVEMENTS ON RMA_BOGIE_MOVEMENTS(BOGIE, IN_LOCATION, IN_DATE, IN_TRANS_DATE, IN_USER);  

CREATE TEMPORARY TABLE RMA_WHEELSET_MOVEMENTS (
	ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR COMMON_RMA_SEQUENCE) NOT NULL NULL_TO_DEFAULT SEQUENCE COMMON_RMA_SEQUENCE,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	WHEELSET BIGINT NOT NULL,
	IN_LOCATION BIGINT NOT NULL,
	IN_LOCATION_TYPE VARCHAR(6) NOT NULL,
	IN_DATE DATE NOT NULL,
	IN_TRANS_DATE DATE NOT NULL,
	IN_USER	BIGINT NOT NULL,
	OUT_LOCATION BIGINT,
	OUT_LOCATION_TYPE VARCHAR(6),
	OUT_DATE DATE,
	OUT_TRANS_DATE DATE,
	OUT_USER BIGINT
);

CREATE UNIQUE INDEX UI_WHEELSET_MOVEMENTS ON RMA_WHEELSET_MOVEMENTS(WHEELSET, IN_LOCATION, IN_DATE, IN_TRANS_DATE, IN_USER);

CREATE TEMPORARY TABLE RMA_ADVICE(
    ID_COLUMN BIGINT DEFAULT (NEXT VALUE FOR COMMON_RMA_SEQUENCE) NOT NULL NULL_TO_DEFAULT SEQUENCE COMMON_RMA_SEQUENCE,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	ADVICE_NO BIGINT NOT NULL,
	ADVICE_DESC VARCHAR(250) NOT NULL,
	RAISED_DATE TIMESTAMP NOT NULL,
	DISPATCHED_DATE TIMESTAMP,
	IS_ROAD VARCHAR(1) NOT NULL,	
	IS_RECEIVED VARCHAR(1) NOT NULL,
	CARRIER_WAGON BIGINT,
    INIT_AT_WORKSHOP BIGINT NOT NULL,
    DISPATCH_TO_WORKSHOP BIGINT
);

CREATE TEMPORARY TABLE RMA_COMPLETION_CERTIFICATE (
	ID_COLUMN BIGINT NOT NULL IDENTITY PRIMARY KEY,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	CC_NO BIGINT NOT NULL,
	CC_DESC VARCHAR(250) NOT NULL,
    CC_STATUS CHAR(1) NOT NULL,
    RAISED_DATE TIMESTAMP NOT NULL,
    ACCEPTED_DATE TIMESTAMP,
	INITIATED_AT BIGINT NOT NULL,
	INITIATED_BY BIGINT NOT NULL,
    ACCEPTED_BY BIGINT
);

CREATE UNIQUE INDEX PUBLIC.UI_RMA_CC ON PUBLIC.RMA_COMPLETION_CERTIFICATE(CC_NO);      

CREATE TEMPORARY TABLE RMA_COMPLETION_CERTIFICATE_ENTRY (
	ID_COLUMN BIGINT NOT NULL IDENTITY PRIMARY KEY,
	VERSION_COLUMN BIGINT NOT NULL DEFAULT 0,
	CC_NO BIGINT NOT NULL,
	ROTABLE BIGINT NOT NULL,
	ROTABLE_TYPE VARCHAR(2) NOT NULL,
	CREATED_DATE TIMESTAMP NOT NULL,
	COMPLETED CHAR(1) NOT NULL,
	AMOUNT NUMERIC,
	CURRENCY VARCHAR(3)
);

CREATE TEMPORARY TABLE COMPOSITE_ENTITY (
	_ID BIGINT NOT NULL,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(60) NOT NULL,
	DESC_ VARCHAR(250) NOT NULL
);

CREATE TEMPORARY TABLE COMPOSITE_ENTITY_KEY (
	_ID BIGINT NOT NULL,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(60) NOT NULL,
	DESC_ VARCHAR(250) NOT NULL
);

CREATE TEMPORARY TABLE COMPLEX_KEY_ENTITY (
	_ID BIGINT NOT NULL,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	DESC_ VARCHAR(250) NOT NULL
);

CREATE TABLE CRAFT (
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(255),
	DESC_ VARCHAR(255),
	RATE NUMERIC(18, 2),
	OVERTIME_RATE NUMERIC(18, 2),
	CT_MAINTSUPERVISOR CHAR(1),
	CT_ORIGINATOR CHAR(1),
	CT_AUTHORISOR CHAR(1),
	CT_ISSUER CHAR(1),
	CT_BUYER CHAR(1),
	CT_PLANNER CHAR(1),
	IS_SUPER_USER CHAR(1),
	IS_SECTOR_USER CHAR(1),
	IS_WORKSHOP_USER CHAR(1),
	IS_BASE CHAR(1) DEFAULT 'N',
	ID_BASE_CRAFT INT,
	USER_NAME VARCHAR(32),
	USER_PASSWORD VARCHAR(255),
	USER_PUBLIC_KEY VARCHAR(255),
	ID_ORG_LEVEL4 INT
);

CREATE TABLE USER_ (
	_ID INT NOT NULL PRIMARY KEY,
	_VERSION INT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(255),
	PASSWORD_ VARCHAR(255) NOT NULL,
	BASE_ CHAR(1) NOT NULL,
	BASEDONUSER_ INT
);



CREATE TABLE USERSESSION_ (
   _ID int PRIMARY KEY NOT NULL,
   _VERSION int DEFAULT ((0)) NOT NULL,
   USER_ int NOT NULL,
   SERIESID_ varchar(255) NOT NULL,
   EXPIRYTIME_ datetime NOT NULL,
   TRUSTED_ char(1) DEFAULT ('N') NOT NULL,
   LASTACCESS_ datetime NOT NULL
);

CREATE TABLE MAIN_MENU
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	KEY_ VARCHAR(255),
	DESC_ VARCHAR(255),
	TITLE VARCHAR(255),
	ITEM_ORDER INT NOT NULL,
	ID_PARENT INT
);

CREATE TABLE MAIN_MENU_INVISIBLE (
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	ID_CRAFT INT NOT NULL,
	ID_MAIN_MENU INT NOT NULL
);

CREATE TABLE ENTITY_CENTRE_CONFIG
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	ID_CRAFT INT NOT NULL,
	TITLE VARCHAR(255) NOT NULL,	
	ID_MAIN_MENU INT NOT NULL,
	IS_PRINCIPAL CHAR(1) NOT NULL DEFAULT 'N',	
	BODY BLOB
);

CREATE UNIQUE INDEX UI_ENTITY_CENTRE_CONFIG ON ENTITY_CENTRE_CONFIG(ID_CRAFT, TITLE, ID_MAIN_MENU);

CREATE TABLE ENTITY_MASTER_CONFIG 
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	ID_CRAFT INT NOT NULL,
	MASTER_TYPE VARCHAR(255) NOT NULL,
	BODY BLOB NOT NULL
);

CREATE UNIQUE INDEX KUI_ENTITY_MASTER_CONFIG ON ENTITY_MASTER_CONFIG(ID_CRAFT, MASTER_TYPE);

ALTER TABLE ENTITY_MASTER_CONFIG ADD CONSTRAINT FK_ENTITY_MASTER_CONFIG_ID_CRAFT FOREIGN KEY (ID_CRAFT) REFERENCES CRAFT(_ID);

CREATE TABLE ENTITY_LOCATOR_CONFIG 
(
	_ID INT NOT NULL IDENTITY PRIMARY KEY,
	_VERSION BIGINT NOT NULL DEFAULT 0,
	ID_CRAFT INT NOT NULL,
	LOCATOR_TYPE VARCHAR(255) NOT NULL,
	BODY BLOB NOT NULL
);

CREATE UNIQUE INDEX KUI_ENTITY_LOCATOR_CONFIG ON ENTITY_LOCATOR_CONFIG(ID_CRAFT, LOCATOR_TYPE);

ALTER TABLE ENTITY_LOCATOR_CONFIG ADD CONSTRAINT FK_ENTITY_LOCATOR_CONFIG_ID_CRAFT FOREIGN KEY (ID_CRAFT) REFERENCES CRAFT(_ID);

CREATE UNIQUE INDEX PUBLIC.UI_RMA_CC_ENTRY ON PUBLIC.RMA_COMPLETION_CERTIFICATE_ENTRY(CC_NO, ROTABLE);

CREATE UNIQUE INDEX ROTABLE_NO_RMA_BOGIE ON RMA_BOGIE(ROTABLE_NO);           
CREATE UNIQUE INDEX ROTABLE_NO_RMA_WHEELSET ON RMA_WHEELSET(ROTABLE_NO);           
CREATE INDEX WOSTATUS_RMA_WODET ON RMA_WODET(WO_STATUS);             
CREATE INDEX WORKSHOP_RMA_WODET ON RMA_WODET(WORKSHOP);              
CREATE INDEX EQUIPNO_RMA_WODET ON RMA_WODET(EQUIPNO);            
CREATE UNIQUE INDEX PK_RMA_WODET ON RMA_WODET(WONO);             

ALTER TABLE RMA_WAGON_SLOT ADD CONSTRAINT PK_WAGON_SLOT PRIMARY KEY(ID_COLUMN);          
ALTER TABLE RMA_BOGIE_SLOT ADD CONSTRAINT PK_BOGIE_SLOT PRIMARY KEY(ID_COLUMN);          
ALTER TABLE RMA_BOGIE ADD CONSTRAINT CONSTRAINT_DA PRIMARY KEY(ID_COLUMN);
ALTER TABLE RMA_WHEELSET ADD CONSTRAINT CONSTRAINT_DAAA PRIMARY KEY(ID_COLUMN); 
ALTER TABLE RMA_ROTCLASS ADD CONSTRAINT PK_RMA_ROTCLASS PRIMARY KEY(ID_COLUMN);              
ALTER TABLE RMA_WODET ADD CONSTRAINT CONSTRAINT_D2 PRIMARY KEY(ID_COLUMN);            
ALTER TABLE RMA_BOGIE_WSET_CLASS_XREF ADD CONSTRAINT CONSTRAINT_D00 PRIMARY KEY(ID_COLUMN);             
ALTER TABLE RMA_WAGONCLASS ADD CONSTRAINT PK_RMA_WAGONCLASS PRIMARY KEY(ID_COLUMN);            
ALTER TABLE RMA_WAGON ADD CONSTRAINT CONSTRAINT_4E PRIMARY KEY(ID_COLUMN);
ALTER TABLE RMA_WORKSHOP ADD CONSTRAINT PK_RMA_WORKSHOP PRIMARY KEY(ID_COLUMN);          
ALTER TABLE RMA_WAGON_BOGIE_CLASS_XREF ADD CONSTRAINT CONSTRAINT_3D PRIMARY KEY(ID_COLUMN);           
ALTER TABLE RMA_ADVICE ADD CONSTRAINT CONSTRAINT_4_ADVICES PRIMARY KEY(ID_COLUMN);
ALTER TABLE RMA_PERSON ADD CONSTRAINT CONSTRAINT_4_PERSONS PRIMARY KEY(ID_COLUMN);
ALTER TABLE RMA_BOGIE_MOVEMENTS ADD CONSTRAINT PK_BOGIE_MOVEMENTS PRIMARY KEY(ID_COLUMN);  
ALTER TABLE RMA_WHEELSET_MOVEMENTS ADD CONSTRAINT PK_WHEELSET_MOVEMENTS PRIMARY KEY(ID_COLUMN);
ALTER TABLE USER_ROLE ADD CONSTRAINT CONSTRAINT_4_USER_ROLES PRIMARY KEY(_ID);
ALTER TABLE USER_ROLE_ASSOCIATION ADD CONSTRAINT CONSTRAINT_4_ASSOCIATION PRIMARY KEY(_ID);
ALTER TABLE SECURITY_ROLE_ASSOCIATION ADD CONSTRAINT CONSTRAINT_4_SECURITY_ROLE_ASSOCIATION PRIMARY KEY(_ID);
ALTER TABLE COMPOSITE_ENTITY ADD CONSTRAINT CONSTRAINT_4_COMPOSITE_ENTITY PRIMARY KEY(_ID);
ALTER TABLE COMPOSITE_ENTITY_KEY ADD CONSTRAINT CONSTRAINT_4_COMPOSITE_ENTITY_KEY PRIMARY KEY(_ID);
ALTER TABLE COMPLEX_KEY_ENTITY ADD CONSTRAINT CONSTRAINT_4_COMPLEX_KEY_ENTITY PRIMARY KEY(_ID);

CREATE TABLE TGVEHICLEMAKE_ ( _ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0,KEY_ VARCHAR(255),DESC_ VARCHAR(255));
CREATE UNIQUE INDEX KUI_VEH_MAKE ON TGVEHICLEMAKE_(KEY_);
CREATE TABLE TGVEHICLEMODEL_ ( _ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0,KEY_ VARCHAR(255),DESC_ VARCHAR(255), MAKE_ INT, CONSTRAINT FK_VEH_MODEL_ID_VEH_MAKE FOREIGN KEY(MAKE_) REFERENCES TGVEHICLEMAKE_(_ID)); 
CREATE UNIQUE INDEX KUI_VEH_MODEL ON TGVEHICLEMODEL_(KEY_);
CREATE TABLE TGVEHICLE_ ( _ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0,KEY_ VARCHAR(255), DESC_ VARCHAR(255),  INITDATE_ DATETIME NULL, PRICE_ DECIMAL(10, 2), PURCHASEPRICE_ DECIMAL(10, 2), MODEL_ INT, ACTIVE_ CHAR(1), LEASED_ CHAR(1),  
STATION_ INT, REPLACEDBY_ INT, CONSTRAINT FK_VEH_ID_VEH_MODEL FOREIGN KEY(MODEL_) REFERENCES TGVEHICLEMODEL_(_ID)); 
CREATE UNIQUE INDEX KUI_VEH ON TGVEHICLE_(KEY_);
CREATE TABLE TGWORKORDER_ ( _ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0,KEY_ VARCHAR(255), DESC_ VARCHAR(255), 
IMPORTANTPROPERTY_ VARCHAR(255), VEHICLE_ INT, ACTCOST_ NUMERIC(18, 2), ESTCOST_ NUMERIC(18, 2), YEARLYCOST_ NUMERIC(18, 2));
CREATE UNIQUE INDEX KUI_TGWORKORDER ON TGWORKORDER_(KEY_);

CREATE TABLE MIGRATION_RUN (_ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0, KEY_ VARCHAR(255) NOT NULL, STARTED DATETIME NOT NULL, FINISHED DATETIME NULL );
CREATE TABLE MIGRATION_HISTORY (_ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0, ID_MIGRATION_RUN INT NOT NULL, RETRIEVER_NAME VARCHAR(255) NOT NULL, THREAD_NAME VARCHAR(255) NOT NULL, ENTITY_TYPE_NAME VARCHAR(255) NOT NULL, RETRIEVED_COUNT INT NULL, INSERTED_COUNT INT NULL, UPDATED_COUNT INT NULL, SKIPPED_COUNT INT NULL, FAILED_COUNT INT NULL, STARTED DATETIME NOT NULL, FINISHED DATETIME NULL );
CREATE TABLE MIGRATION_ERROR (_ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0, ID_MIGRATION_HISTORY INT NOT NULL, ERROR_NUMBER INT NOT NULL, RAW_DATA VARCHAR(4096), ERROR_TYPE VARCHAR(4096), ERROR_MESSAGE VARCHAR(4096), ERROR_PRECAUSE VARCHAR(4096), ERROR_PROP_NAME VARCHAR(256), ERROR_PROP_TYPE VARCHAR(4096), ERROR_PROP_VALUE VARCHAR(4096));

CREATE TABLE WODET ( _ID INT NOT NULL PRIMARY KEY, _VERSION INT NOT NULL DEFAULT 0,KEY_ VARCHAR(255),DESC_ VARCHAR(255),ID_EQDET INT,ID_ORG_LEVEL4 INT,ID_CRAFT_ORIGINATOR INT,ID_CRAFT_MAINT_SUPER INT,ID_COST_CENTRE INT,ID_WORKSHOP INT,ID_WOTYPE INT,ID_PRTY INT,ID_WO_STATUS_CODES INT,ID_CUSTOMERS INT,ID_SERVICE_TYPE INT,ID_INCIDENT INT, ID_PM_ROUTINE INT, DURATION NUMERIC(18, 2),TRANS_DATE DATETIME,EARLY_START DATETIME,EARLY_FINISH DATETIME,ACTUAL_START DATETIME,ACTUAL_FINISH DATETIME,DOWN_TIME NUMERIC(18, 2),MAN_EST NUMERIC(18, 2),MAN_COST NUMERIC(18, 2),BILL_EST NUMERIC(18, 2),BILL_COST NUMERIC(18, 2),DC_EST NUMERIC(18, 2),DC_COST NUMERIC(18, 2),CONT_EST NUMERIC(18, 2),CONT_COST NUMERIC(18, 2),SPEQ_EST NUMERIC(18, 2),SPEQ_COST NUMERIC(18, 2),IND_EST NUMERIC(18, 2),IND_COST NUMERIC(18, 2),TASK_EST NUMERIC(18, 2),TASK_COST NUMERIC(18, 2),TOTAL_ACT_COST NUMERIC(18, 2),TOTAL_EST_COST NUMERIC(18, 2),METER_READING INT,JOB_NO VARCHAR(32)); 