CREATE TABLE Students
(
	student_id SERIAL Primary Key,
	first_name Text not null,
	last_name Text not null,
	email Text Not Null Unique,
	enrollment_date Date
);
