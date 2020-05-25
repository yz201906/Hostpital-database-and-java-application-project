CREATE TABLE Address (
    AddressID INTEGER NOT NULL,
    Street VARCHAR(50) NOT NULL,
    "NUMBER" INTEGER,
    City VARCHAR(20) NOT NULL,
    "STATE" VARCHAR(20) NOT NULL,
    Country VARCHAR(30) NOT NULL,
    ZipCode VARCHAR(10) NOT NULL,
    PRIMARY KEY (AddressID),
    CHECK (AddressID > 0)
);
        
 
CREATE TABLE BodyParts (
	BPCode VARCHAR(6),
	BPName VARCHAR(30) NOT NULL,
	PRIMARY KEY(BPCode)
);


CREATE TABLE Equipment(
	EName varchar(50),
	PRIMARY KEY (EName)
);


CREATE TABLE Services(
    SCode varchar(5) NOT NULL,
    SName varchar(20) NOT NULL,
    PRIMARY KEY (SCode)
);


CREATE TABLE Priority (
	PriorityId INTEGER NOT NULL,
	"TYPE" varchar(20) NOT NULL,
	PRIMARY KEY (PriorityId),
    CHECK (PriorityId > 0)
);


CREATE TABLE NegativeExperiences (
	NECode INTEGER NOT NULL,
	"NAME" varchar(20) NOT NULL,
	PRIMARY KEY (NECode),
    CHECK (NECode > 0)
);

CREATE TABLE SeverityScale (
    "NAME" VARCHAR(20),
    PRIMARY KEY ("NAME")
);

CREATE TABLE Symptoms(
    SCode VARCHAR(6), 
	SName VARCHAR(50),
	HasBodyPart NUMBER(1) NOT NULL,
    AddedByPatient NUMBER(1) NOT NULL,
    BPCode VARCHAR(6),
    ScaleName VARCHAR(20),
	PRIMARY KEY(SCode),
    FOREIGN KEY(BPCode) REFERENCES BodyParts,
    FOREIGN KEY(ScaleName) REFERENCES SeverityScale,
    CHECK (SCode LIKE 'SYM___%')
);


CREATE TABLE Severity (
    SeverityId INTEGER NOT NULL,
    "NAME" varchar(10),
    "NUMBER" INTEGER,
    ScaleName VARCHAR(20) NOT NULL,
    PRIMARY KEY (SeverityId),
    FOREIGN KEY (ScaleName) REFERENCES SeverityScale,
    CHECK (SeverityId > 0)
);

    
CREATE TABLE Facility (
    FId INTEGER NOT NULL,
    FName VARCHAR(50) NOT NULL,
    "CLASSIFICATION" VARCHAR(2) NOT NULL,
    Beds INTEGER NOT NULL,
    AddressID INTEGER NOT NULL,
    PRIMARY KEY (FId),
    FOREIGN KEY (AddressID) REFERENCES Address,
    CHECK (FId > 0),
    CHECK ("CLASSIFICATION" = '01' OR "CLASSIFICATION" = '02' OR "CLASSIFICATION" = '03')
);


CREATE TABLE Certificate (
    CCode VARCHAR(10) NOT NULL,
    CName VARCHAR(50) NOT NULL,
    CDate DATE NOT NULL,
    EDate DATE NOT NULL,
    FId INTEGER NOT NULL,
    PRIMARY KEY (CCode),
    FOREIGN KEY (FId) REFERENCES Facility,
    CHECK (CCode LIKE 'CER___%')
);
    

CREATE TABLE PriorityRuleSet (
    RuleSetId INTEGER NOT NULL,
    PriorityId INTEGER NOT NULL,
    PRIMARY KEY (RuleSetId),
    FOREIGN KEY (PriorityId) REFERENCES "PRIORITY",
    CHECK (RuleSetId > 0)
);


CREATE TABLE PriorityRules (
	RuleId INTEGER NOT NULL,
	SCode VARCHAR(6) NOT NULL,
	SeverityId INTEGER,
	BPCode VARCHAR(6),
    Operator INTEGER,
    RuleSetId INTEGER NOT NULL,
	PRIMARY KEY (RuleId),
	FOREIGN KEY (SCode) REFERENCES Symptoms,
	FOREIGN KEY (SeverityId) REFERENCES Severity,
	FOREIGN KEY (BPCode) REFERENCES BodyParts,
    FOREIGN KEY (RuleSetId) REFERENCES PriorityRuleSet,
    CHECK (RuleId > 0)
);


CREATE TABLE Staff (
    SId INTEGER NOT NULL,
    AId INTEGER NOT NULL,
    IsMedical NUMBER(1) NOT NULL,
    FirstName varchar(50) NOT NULL,
    LastName varchar(50) NOT NULL,
    DOB DATE NOT NULL,
    PRIMARY KEY (SId),
    FOREIGN KEY (AId) REFERENCES Address,
    CHECK (SId > 0)
);

CREATE TABLE ServiceDepartment (
    SDCode VARCHAR(5) NOT NULL,
    Dname VARCHAR(20),
    IsMedical NUMBER(1) NOT NULL,
    FId INTEGER NOT NULL,
    SId INTEGER NOT NULL,
    PRIMARY KEY (SDCode, FId),
    FOREIGN KEY (FId) REFERENCES Facility ON DELETE CASCADE,
    FOREIGN KEY (SId) REFERENCES Staff
);


CREATE TABLE Patient (
    PId INTEGER NOT NULL,
    LastName varchar(50) NOT NULL,
    FirstName varchar(50) NOT NULL,
    PhoneNo varchar(20),
    DOB DATE NOT NULL,
    AddressId INTEGER NOT NULL,
    PRIMARY KEY(PId),
    FOREIGN KEY(AddressId) REFERENCES Address,
    CHECK (PId > 0)
);
    
    
CREATE TABLE PatientReports (
    PId INTEGER,
    TreatmentStartTime DATE NOT NULL,
    DeclineDescription VARCHAR(60),
    Treatment VARCHAR(40),
    DischargeStatus VARCHAR(15) NOT NULL, 
    ReportSubmitTime DATE NOT NULL,
    FOREIGN KEY(PId) REFERENCES Patient(PId) ON DELETE CASCADE,
    PRIMARY KEY(TreatmentStartTime, PId)
);


CREATE TABLE CheckIn (
	PId INTEGER NOT NULL,
	StartTime DATE NOT NULL,
	EndTime DATE,
	BloodPressure varchar(20),
	BodyTemperature NUMBER(38,2),
	StaffId INTEGER,
	FacilityId INTEGER NOT NULL,
	PriorityId INTEGER,
    TreatmentStartTime DATE,
	PRIMARY KEY(PId, StartTime),
	FOREIGN KEY(PId) REFERENCES Patient ON DELETE CASCADE,
	FOREIGN KEY(StaffId) REFERENCES Staff,
	FOREIGN KEY(FacilityId) REFERENCES Facility ON DELETE CASCADE,
	FOREIGN KEY (PriorityId) REFERENCES Priority
);

CREATE TABLE ReferralReasons (
    ReasonId INTEGER NOT NULL,
	RCode INTEGER NOT NULL,
	"DESCRIPTION" varchar(100),
	ServiceName varchar(30) NOT NULL,
	TreatmentStartTime DATE NOT NULL,
	PatientId NOT NULL,
	PRIMARY KEY (ReasonId),
	FOREIGN KEY (TreatmentStartTime, PatientId) REFERENCES PatientReports,
    CHECK (ReasonId > 0),
    CHECK (RCode = 1 OR RCode = 2 OR RCode = 3)
);

CREATE TABLE Ptnt_Describes_Symp (
    PatientId INTEGER NOT NULL,
    CheckInStartTime DATE NOT NULL,
    SeverityId INTEGER NOT NULL,
    SymptomCode VARCHAR(6) NOT NULL,
    BPCode VARCHAR(6),
    Duration INTEGER,
    DurationType INTEGER,
    Occurrence NUMBER(1),
    Cause varchar(50),
    PRIMARY KEY (PatientId, CheckInStartTime, SeverityId, SymptomCode),
    FOREIGN KEY (PatientId, CheckInStartTime) REFERENCES CheckIn,	
    FOREIGN KEY (SeverityId) REFERENCES Severity,
    FOREIGN KEY (SymptomCode) REFERENCES Symptoms,
    FOREIGN KEY (BPCode) REFERENCES BodyParts
);


CREATE TABLE Dpt_SpecializesIn_Srv (
    FacilityId INTEGER NOT NULL,
    ServiceDepartmentCode VARCHAR(5) NOT NULL,
    BodyPartCode VARCHAR(10),
    PRIMARY KEY (FacilityId, ServiceDepartmentCode, BodyPartCode),
    FOREIGN KEY (FacilityId, ServiceDepartmentCode) REFERENCES ServiceDepartment(FId, SDCode),
    FOREIGN KEY (BodyPartCode) REFERENCES BodyParts
);

CREATE TABLE Ptnt_Reports_Rpt (
    PatientId INTEGER NOT NULL,
    TreatmentStartTime DATE NOT NULL,
    NegativeExperienceCode INTEGER NOT NULL,
    Description varchar(100),
    PRIMARY KEY (PatientId, TreatmentStartTime, NegativeExperienceCode),
    FOREIGN KEY (PatientId, TreatmentStartTime) REFERENCES PatientReports(PId, TreatmentStartTime),
    FOREIGN KEY (NegativeExperienceCode) REFERENCES NegativeExperiences,
    CHECK (NegativeExperienceCode = 1 OR NegativeExperienceCode = 2)
);


CREATE TABLE Srv_Uses_Eqp (
    EName VARCHAR(50) NOT NULL,
    SCode VARCHAR(5) NOT NULL,
    PRIMARY KEY (EName, SCode),
    FOREIGN KEY (EName) REFERENCES Equipment,
    FOREIGN KEY (SCode) REFERENCES Services
);

CREATE TABLE Dpt_Provides_Srv (
    SCode VARCHAR(5) NOT NULL,
    SDCode VARCHAR(5) NOT NULL,
    FId INTEGER NOT NULL,
    PRIMARY KEY (SCode, SDCode),
    FOREIGN KEY (SCode) REFERENCES Services,
    FOREIGN KEY (SDCode, FId) REFERENCES ServiceDepartment
);

CREATE TABLE Ptnt_ReferredByTo_Fclt (
    PId INTEGER,
    TreatmentStartTime DATE,
    Sid INTEGER NOT NULL,
    FId INTEGER,
    FOREIGN KEY (TreatmentStartTime, PId) REFERENCES PatientReports,
    FOREIGN KEY (FId) REFERENCES Facility,
    FOREIGN KEY (Sid) REFERENCES Staff,
    PRIMARY KEY (PId, TreatmentStartTime)
);


CREATE TABLE Stf_WorksAt_Dpt (
    FId INTEGER NOT NULL,
    SDCode VARCHAR(5) NOT NULL,
    Sid INTEGER NOT NULL,
    HireDate DATE NOT NULL,
    IsPrimary NUMBER(1) NOT NULL,
    PRIMARY KEY (FId, Sid, SDCode),
    FOREIGN KEY (SDCode, FId) REFERENCES ServiceDepartment,
    FOREIGN KEY (Sid) REFERENCES Staff
);



CREATE TRIGGER ReasonCount
BEFORE INSERT ON ReferralReasons
FOR EACH ROW
DECLARE RCount INTEGER;
BEGIN
    SELECT COUNT(*) INTO RCount FROM ReferralReasons WHERE PatientId = :new.PatientId AND TreatmentStartTime = :new.TreatmentStartTime;
    
    IF(RCount = 4) THEN
        RAISE_APPLICATION_ERROR(-20000
                , 'Patient Reports cannot contain more than 4 reasons.');
    END IF;
END;