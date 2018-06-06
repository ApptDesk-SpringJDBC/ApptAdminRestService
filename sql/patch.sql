-- Dec 11 2016
-- Balaji
create table dynamic_fields_display (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `page_name` varchar(100) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `column_name` varchar(50) NOT NULL,
  `display` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

insert  into `dynamic_fields_display`(`id`,`page_name`,`table_name`,`column_name`,`display`) values (1,'location','location','locationNameOnline','Y'),(2,'location','location','address','Y'),(3,'location','location','city','Y'),(4,'location','location','state','Y'),(5,'location','location','zip','Y'),(6,'location','location','timeZone','Y'),(7,'location','location','workPhone','Y'),(8,'location','location','enable','Y'),(9,'location','location','closed','Y'),(10,'location','location','locationNameIvrTts','Y'),(11,'location','location','locationNameIvrAudio','Y'),(12,'location','location','locationGoogleMap','Y'),(13,'location','location','locationGoogleMapLink','Y'),(14,'location','location','closedMessage','Y'),(15,'location','location','closedTts','Y'),(16,'location','location','closedAudio','Y'),(17,'location','location','apptStartDate','Y'),(18,'location','location','apptEndDate','Y');

-- Dec 13 2016
-- updated by Murali
insert  into `dynamic_fields_display`(`page_name`,`table_name`,`column_name`,`display`) values 
('service','service','serviceNameOnline','Y'),
('service','service','duration','Y'),
('service','service','allowSchedulingFor','Y'),
('service','service','serviceNameIvrTts','Y'),
('service','service','serviceNameIvrAudio','Y'),
('service','service','allowDuplicateAppt','Y'),
('service','service','enable','Y'),
('service','service','closed','Y'),
('service','service','closedLocations','Y'),
('service','service','closedMessage','Y'),
('service','service','closedTts','Y'),
('service','service','closedAudio','Y'),
('service','service','apptStartDate','Y'),
('service','service','apptEndDate','Y'),
('service','service','serviceCSSColor','Y');


-- Dec 22 2016
-- updated by Murali
insert  into `dynamic_fields_display`(`page_name`,`table_name`,`column_name`,`display`) values 
('resource','resource','prefix','Y'),
('resource','resource','firstName','Y'),
('resource','resource','lastName','Y'),
('resource','resource','title','Y'),
('resource','resource','email','Y'),
('resource','resource','locationName','Y'),
('resource','resource','resourceServices','Y'),
('resource','resource','allowSelfService','Y'),
('resource','resource','enable','Y'),
('resource','resource','resourceAudio','Y');


-- Balaji
CREATE TABLE `dynamic_search_result_columns` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(200) NOT NULL,
  `table_column` varchar(200) NOT NULL DEFAULT '',
  `title` varchar(500) NOT NULL,
  `display_flag` char(1) NOT NULL DEFAULT 'Y',
  `placement` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- Balaji
create table dynamic_search_by_fields (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `title` varchar(100) NOT NULL,
  `display` char(1) NOT NULL DEFAULT 'Y',
  `placement` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



-- Dec 27 2016
-- updated by Murali
insert  into `dynamic_fields_display`(`page_name`,`table_name`,`column_name`,`display`) values 
('user','user','firstName','Y'),
('user','user','lastName','Y'),
('user','user','username','Y'),
('user','user','contactEmail','Y'),
('user','user','contactPhone','Y'),
('user','user','accessLevel','Y'),
('user','user','changePassword','Y');

-- JAN 08 2017
-- updated by Murali
insert into dynamic_search_by_fields (title,display,placement) values ('Appointent Details','Y',1);
insert into dynamic_search_by_fields (title,display,placement) values ('Customer Details','Y',2);
insert into dynamic_search_by_fields (title,display,placement) values ('Customer Activity Details','Y',3);
insert into dynamic_search_by_fields (title,display,placement) values ('Household Info Details','Y',4);
insert into dynamic_search_by_fields (title,display,placement) values ('Pledge Details','Y',5);

-- Balaji
create table dynamic_field_labels (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  page_name varchar(100) NOT NULL,
  field_name varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `display` char(1) NOT NULL DEFAULT 'Y',
  `placement` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','accountNumber','AccountNumber','Y','1');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','firstName','FirstName','Y','2');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','lastName','LastName','Y','3');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','contactPhone','ContactPhone','Y','4');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','apptDateTime','Appt Date & Time','Y','5');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','serviceName','ServiceName','Y','6');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('calendar_tooltip','attrib1','Energy Acc','Y','7');


insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','time','Time','Y','1');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','serviceName','ServiceName','Y','2');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','notificationStatus','Notify Status','Y','3');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','accountNumber','SSN','Y','4');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','firstName','FirstName','Y','5');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','lastName','LastName','Y','6');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','contactPhone','ContactPhone','Y','7');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','state','State','Y','8');
insert into `dynamic_field_labels` (`page_name`, `field_name`, `title`, `display`, `placement`) values('table_print_view','zipCode','ZipCode','Y','9');

create table itemized_report_goal (
  id int(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  row_goal int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('admin','us-en','NO_BOOKED_APPTS','There are no future appointments you have scheduled with us. Please make sure you have entered the correct SSN.');
insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('admin','us-en','DUPLICATE_APPT','You have previously scheduled appointment with us. In order to schedule a new appointment, you will need to cancel the existing appointment.');
insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('admin','us-en','HOLD_NOT_RELEASED','Your record is currently locked and unable to access now. Please try after 15 minutes.');
insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('admin','us-en','SELECTED_DATE_TIME_NOT_AVAILABLE','Selected Date and time is not available.');

insert into `itemized_report_goal` (`id`, `row_goal`) values('1','80');
insert into `itemized_report_goal` (`id`, `row_goal`) values('2','15');
insert into `itemized_report_goal` (`id`, `row_goal`) values('3','10');
insert into `itemized_report_goal` (`id`, `row_goal`) values('4','10');
insert into `itemized_report_goal` (`id`, `row_goal`) values('5','75');

alter table appointmentstatus add column denied char(1) DEFAULT 'Y';
alter table appointmentstatus add column report_display char(1) DEFAULT 'Y';



insert  into `dynamic_include_reports`(`table_name`,`table_column`,`checkbox_status`,`title`,`placement`) values 
('includeReportNew','ssn','1','Acct Number',5),
('includeReportNew','firstName','1','First Name',6),
('includeReportNew','lastName','1','Last Name',7),
('includeReportNew','contactPhone','1','Contact Phone',8),
('includeReportNew','email','1','Email',9),
('includeReportNew','apptStatus','1','Status',10),
('includeReportNew','apptDateTime','1','Appt Date/Time',1),
('includeReportNew','locationName','1','Location',2),
('includeReportNew','resourceName','1','Intake',3),
('includeReportNew','serviceName','1','Service',4),
('includeReportNew','apptMethod','1','Method',11),
('includeReportNew','walkIn','1','Walk in',13),
('includeReportNew','comments','1','Comments',14),
('includeReportNew','departmentName','1','Department Name',12),
('includeReportNew','paymentAmount','1','Pledge',16),
('includeReportNew','frontDeskApptDuration','1','Front Desk Duration',17),
('includeReportNew','resourceApptDuration','1','Doctor Duration',18),
('includeReportNew','accessed','1','Assessed',18);

ALTER TABLE customer_pledge add column location_id INT(10) NULL;
ALTER TABLE customer_pledge add column resource_id INT(10) NULL;
ALTER TABLE customer_pledge add column service_id INT(10) NULL;


CREATE TABLE `appointment_status_report` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each row',
	`column-name` VARCHAR(100) NOT NULL,
	`appointmentstatus_ids` VARCHAR(50) NOT NULL,
	`is_denied` CHAR(1) NULL DEFAULT 'N',
	report_display char(1) DEFAULT 'Y',
	place_holder_name varchar(50) NULL,
	`placement` INT(6) NULL DEFAULT '1' COMMENT 'Placement Order',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=MyISAM
AUTO_INCREMENT=1
;

INSERT INTO `appointment_status_report` values (NULL, '#Served', '5', 'N', 'Y',"SERVED",1);
INSERT INTO `appointment_status_report` values (NULL, 'IN', '9,12', 'Y','Y',"IN", 2);
INSERT INTO `appointment_status_report` values (NULL, 'SS', '8,17', 'Y','Y',"SS", 3);
INSERT INTO `appointment_status_report` values (NULL, 'ID/DL', '6,10', 'Y','Y',"ID_DL", 4);
INSERT INTO `appointment_status_report` values (NULL, 'OI', '14', 'Y','Y',"OI", 5);
INSERT INTO `appointment_status_report` values (NULL, 'PA', '13', 'Y','Y',"PA", 6);
INSERT INTO `appointment_status_report` values (NULL, 'CA-$0', '7', 'Y','Y',"CA", 7);
INSERT INTO `appointment_status_report` values (NULL, 'IP-GA', '16', 'Y','Y',"IP_GA", 8);
INSERT INTO `appointment_status_report` values (NULL, 'No Show', '1', 'N','Y',"NOSHOW", 9);
INSERT INTO `appointment_status_report` values (NULL, 'Booked', '18', 'N','Y',"NO_OF_BOOKED", 10);
commit;






