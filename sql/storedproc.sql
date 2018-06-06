DELIMITER $$
DROP PROCEDURE IF EXISTS `get_stackchart_info_sp`$$
CREATE PROCEDURE `get_stackchart_info_sp`(IN loc_id INT(10), IN res_id INT(10), IN stack_chart_type varchar(50), OUT stackchart_info TEXT, OUT error_msg VARCHAR(2000))
BEGIN
  DECLARE sp_cur_first_avail_date varchar(20)DEFAULT '';
  DECLARE sp_cur_first_avail_past_date varchar(20) DEFAULT '';
  DECLARE sp_resource_ids text DEFAULT '';
  DECLARE sp_cur_ser_blocks TINYINT(4);	
  DECLARE sp_start_date date DEFAULT NULL;	
  DECLARE sp_end_date date DEFAULT NULL;	


  DECLARE resource_id_cur CURSOR FOR select group_concat(rc.id) from resource rc where rc.delete_flag='N' and rc.enable='Y' and rc.location_id = loc_id order by rc.id;
  DECLARE bookedApptCount_cur CURSOR FOR select count(s.id) from schedule s where s.appt_date_time between sp_start_date and sp_end_date and s.id > 0 and s.status>=11 and s.status<=19 and s.resource_id in (sp_resource_ids);
  -- DECLARE holdApptCount_cur CURSOR FOR select count(s.id) from schedule s where s.appt_date_time between sp_start_date and sp_end_date and s.id > 0 and s.status=1 and s.resource_id in (sp_resource_ids);
  DECLARE openApptCount_cur CURSOR FOR select count(rc.id) from resource_calendar rc where DATE(rc.date_time) >=sp_start_date and DATE(rc.date_time) <= sp_end_date and rc.schedule_id=0 and rc.resource_id in (sp_resource_ids) 
                                       and DATE(rc.date_time) NOT IN (select cd.date from closed_days cd where cd.location_id=loc_id and cd.date >= sp_start_date and cd.date <= sp_end_date UNION
                                       select h.date from holidays h where h.date >= sp_start_date  and h.date<=sp_end_date);


  -- stack_chart_type=normal
  set @maxDaysVerifyCountForNormalStackedChart=30;
  set @noOfPastDaysForNormalStackedChartGraph=2;
  set @noOfFutureDaysForNormalStackedChartGraph=2;
  
  -- stack_chart_type=rotated
  set @maxDaysVerifyCountForRotatedStackedChart=30;
  set @noOfPastDaysForRotatedChartGraph=0;
  set @noOfFutureDaysForRotatedChartGraph=29;
  
 
  
  set stackchart_info='stackedChartDays=11/01/16,11/02/16,11/03/16,11/04/16,11/07/16,11/08/16,11/09/16,11/10/16,11/11/16,11/14/16,11/15/16,11/16/16,11/17/16,11/18/16,11/21/16,11/22/16,11/23/16,11/28/16,11/29/16,11/30/16|noOfApptsBooked=4,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0|noOfApptsOpened=57,60,85,45,63,61,60,85,44,64,61,60,85,45,64,61,60,64,61,60|
                       noOfConfirmedNotifications=|noOfUnConfirmedNotifications=';
  
   OPEN resource_id_cur;
	  FETCH resource_id_cur INTO sp_resource_ids;
   CLOSE resource_id_cur;

   -- TODO: loop through date from now to next maxDaysVerifyCountForNormalStackedChart and append into three local variables{date, bookedAppts, openBooked}. prepare final format.
   
END$$
DELIMITER ;

call get_stackchart_info_sp(1,1,@a,@b);
select @a,@b;


DELIMITER $$
DROP PROCEDURE IF EXISTS `add_location_sp`$$
CREATE PROCEDURE `add_location_sp`(IN location_name_online varchar(100),
        IN location_name_mobile VARCHAR(100), 
        IN location_name_sms varchar(100),
        IN location_name_ivr_tts varchar(100),
		IN location_name_ivr_audio varchar(100),
		IN location_name_remind_sms varchar(20),
		IN address varchar(60),
		IN city varchar(30),
		IN state varchar(2),
		IN zip varchar(9),
		IN work_phone varchar(15),
		IN location_google_map text,
		IN location_google_map_link text,
		IN time_zone varchar(20),
		IN comments varchar(100),
		IN delete_flag CHAR(1),
		IN placement INT(6),
		IN `enable` CHAR(1),
		IN closed char(1),
		IN closed_message varchar(500),
		IN closed_audio varchar(100),
		IN closed_tts varchar(500),
		IN appt_start_date DATE,
		IN appt_end_date DATE,
		OUT response CHAR(1),
		OUT error_msg VARCHAR(2000))
	
BEGIN
  DECLARE sp_location_id INT(10) DEFAULT 0;
  DECLARE sp_default_day_start_time TIME DEFAULT '08:00';
  DECLARE sp_default_day_end_time TIME DEFAULT '11:00';
  DECLARE sp_default_is_sun_open CHAR(1) DEFAULT 'N';
  DECLARE sp_default_is_mon_open CHAR(1) DEFAULT 'N';
  DECLARE sp_default_is_tue_open CHAR(1) DEFAULT 'N';
  DECLARE sp_default_is_wed_open CHAR(1) DEFAULT 'N';
  DECLARE sp_default_is_thu_open CHAR(1) DEFAULT 'N';
  DECLARE sp_default_is_fri_open CHAR(1) DEFAULT 'N';
  DECLARE sp_default_is_sat_open CHAR(1) DEFAULT 'N';	

  insert into location 
  (location_name_online,location_name_mobile, location_name_sms, location_name_remind_sms, 
   location_name_ivr_tts, location_name_ivr_audio,
   address, city, state, zip, work_phone, location_google_map, location_google_map_link, 
   time_zone, comments,delete_flag, `enable`, closed, closed_message, closed_audio, closed_tts,appt_start_date,appt_end_date) 
   values (location_name_online, 
           IF(location_name_mobile IS NULL,'', location_name_mobile), 
           IF(location_name_sms IS NULL,'',location_name_sms),
           IF(location_name_remind_sms IS NULL ,'',location_name_remind_sms),
           IF(location_name_ivr_tts IS NULL,'',location_name_ivr_tts),
           IF(location_name_ivr_audio IS NULL,'',location_name_ivr_audio),
           address, city, state, zip,
           work_phone, 
           IF(location_google_map IS NULL ,'',location_google_map),
           IF(location_google_map_link IS NULL,'',location_google_map_link),
           time_zone,
           IF(comments IS NULL,'',comments),
           IF(delete_flag IS NULL OR delete_flag='','N','Y'),
           IF(`enable` IS NULL OR `enable`='','Y','N'),
           IF(closed IS NULL OR closed='','N','Y'),
           IF(closed_message IS NULL,'',closed_message),
           IF(closed_audio IS NULL,'',closed_audio),
           IF(closed_tts IS NULL,'',closed_tts),
           IF(appt_start_date IS NULL OR appt_start_date='',NULL,appt_start_date),
	   IF(appt_end_date IS NULL OR appt_end_date='',NULL,appt_end_date));

   SET sp_location_id = LAST_INSERT_ID();
   
   select default_day_start_time,default_day_end_time,default_is_sun_open,default_is_mon_open,default_is_tue_open,
          default_is_wed_open,default_is_thu_open,default_is_fri_open,default_is_sat_open from appt_sys_config 
          into sp_default_day_start_time,sp_default_day_end_time,sp_default_is_sun_open,sp_default_is_mon_open,sp_default_is_tue_open,sp_default_is_wed_open,sp_default_is_thu_open,sp_default_is_fri_open,sp_default_is_sat_open;

   
   insert into location_working_hrs(location_id
                ,sun_start_time, sun_end_time
                ,mon_start_time,mon_end_time
                ,tues_start_time,tues_end_time
                ,wed_start_time, wed_end_time
                ,thurs_start_time,thurs_end_time
                ,fri_start_time,fri_end_time
                ,sat_start_time,sat_end_time
                ) VALUES 
               (sp_location_id,
		IF(sp_default_is_sun_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_sun_open='Y',sp_default_day_end_time,NULL),
		IF(sp_default_is_mon_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_mon_open='Y',sp_default_day_end_time,NULL),
		IF(sp_default_is_tue_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_tue_open='Y',sp_default_day_end_time,NULL),
		IF(sp_default_is_wed_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_wed_open='Y',sp_default_day_end_time,NULL),
		IF(sp_default_is_thu_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_thu_open='Y',sp_default_day_end_time,NULL),
		IF(sp_default_is_fri_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_fri_open='Y',sp_default_day_end_time,NULL),
		IF(sp_default_is_sat_open='Y',sp_default_day_start_time,NULL),
		IF(sp_default_is_sat_open='Y',sp_default_day_end_time,NULL)
               );
   

    SET response='Y';
END$$
DELIMITER ;


-- test info
-- call add_location_sp('balji1last','onl','sms','rsms','tts','audio','address','ci','dd','234','1231231234','','','','dd','N','Y','N','','','','',null, null, @a,@b);
-- select @a;
-- select @b;


DELIMITER $$
DROP PROCEDURE IF EXISTS `update_location_sp`$$
CREATE PROCEDURE `update_location_sp`(
        IN location_id INT(10),
        IN location_name_online varchar(100),
        IN location_name_mobile VARCHAR(100), 
        IN location_name_sms varchar(100),
        IN location_name_ivr_tts varchar(100),
		IN location_name_ivr_audio varchar(100),
		IN location_name_remind_sms varchar(20),
		IN address varchar(60),
		IN city varchar(30),
		IN state varchar(2),
		IN zip varchar(9),
		IN work_phone varchar(15),
		IN location_google_map text,
		IN location_google_map_link text,
		IN time_zone varchar(20),
		IN comments varchar(100),
		IN delete_flag CHAR(1),
		IN placement INT(6),
		IN `enable` CHAR(1),
		IN closed char(1),
		IN closed_message varchar(500),
		IN closed_audio varchar(100),
		IN closed_tts varchar(500),
		IN appt_start_date DATE,
		IN appt_end_date DATE,
		OUT response CHAR(1),
		OUT error_msg VARCHAR(2000))
	
BEGIN 
  update location set location_name_online=location_name_online,
  		   location_name_mobile=IF(location_name_mobile IS NULL,'', location_name_mobile), 
           location_name_sms=IF(location_name_sms IS NULL,'',location_name_sms),
           location_name_remind_sms=IF(location_name_remind_sms IS NULL ,'',location_name_remind_sms),
           location_name_ivr_tts=IF(location_name_ivr_tts IS NULL,'',location_name_ivr_tts),
           location_name_ivr_audio=IF(location_name_ivr_audio IS NULL,'',location_name_ivr_audio),
           address=address,
           city=city,
           state=state,
           zip=zip,
           work_phone=work_phone,
           location_google_map=IF(location_google_map IS NULL ,'',location_google_map),
           location_google_map_link=IF(location_google_map_link IS NULL,'',location_google_map_link),
           time_zone=time_zone,
           comments=IF(comments IS NULL,'',comments),
           delete_flag=IF(delete_flag IS NULL OR delete_flag='','N','Y'),
           `enable`=IF(`enable` IS NULL OR `enable`='','Y','N'),
           closed=IF(closed IS NULL OR closed='','N','Y'),
           closed_message=IF(closed_message IS NULL,'',closed_message),
           closed_audio=IF(closed_audio IS NULL,'',closed_audio),
           closed_tts=IF(closed_tts IS NULL,'',closed_tts),
           appt_start_date=IF(appt_start_date IS NULL OR appt_start_date='',NULL,appt_start_date),
	       appt_end_date=IF(appt_end_date IS NULL OR appt_end_date='',NULL,appt_end_date)
	       where id=location_id;
           
    SET response='Y';
END$$
DELIMITER ;

-- call update_location_sp(15,'balji1lastdd','onl','sms','rsms','tts','audio','address','ci','dd','234','1231231234','','','','dd','N','Y','N','','','','',null, null, @a,@b);
-- select @a;
-- select @b;



DELIMITER $$
DROP PROCEDURE IF EXISTS `hold_appointment_sp`$$
CREATE PROCEDURE `hold_appointment_sp`(IN `appt_date_time` VARCHAR(60), IN `block_time_in_mins` INT(11), IN `loc_id` INT(10), IN `res_id` INT(10), IN proc_id INT(10), IN dept_id INT(10), IN `ser_id` INT(10), IN `cust_id` BIGINT(20), IN `trans_id` BIGINT(20), IN device VARCHAR(10), OUT `sched_id` BIGINT(20), OUT `error_msg` VARCHAR(2000), OUT `display_datetime` VARCHAR(200))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE rows_count INT DEFAULT 0;
	DECLARE no_blocks INT DEFAULT 0;
	DECLARE dups_appt CHAR(1) DEFAULT 'N'; -- no duplicates by default
	DECLARE dups_appt_count INT DEFAULT 0;
	DECLARE dups_hold_count INT DEFAULT 0;
	DECLARE sp_res_id INT(10);

	DECLARE cur_resource_list CURSOR  FOR select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where r.id=res_id and ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N' order by r.placement;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	START TRANSACTION;
	
	select allow_duplicate_appt INTO dups_appt from service where id=ser_id;
	
	select count(s.`id`) INTO dups_appt_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and s.`status` = 11;
	
	select count(s.`id`) INTO dups_hold_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and  s.`status` = 1;
	
	SET sched_id = 1;
	
	IF(dept_id = NULL or dept_id = 0) THEN
		SET dept_id = 1;
	END IF;
	IF(proc_id = NULL or proc_id = 0) THEN
		SET proc_id = 1;
	END IF;
	
	IF(dups_appt = 'N' and dups_appt_count > 0 and cust_id > 0) THEN
		SET error_msg = 'DUPLICATE_APPT';
	ELSE
		 IF(dups_appt = 'N' and dups_hold_count > 0 and cust_id > 0) THEN
		 	SET error_msg = 'HOLD_NOT_RELEASED';
		 ELSE
			SET done = 0;
			OPEN cur_resource_list;
			REPEAT
				FETCH cur_resource_list INTO sp_res_id;
				IF (done=0) THEN
					select `blocks` INTO no_blocks from service where id=ser_id;
			
					-- find out timeslots are still available for that date/time and resourceId
					select count(1) INTO rows_count from resource_calendar where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=sp_res_id and schedule_id=0;
			
					IF(rows_count = no_blocks) THEN
						INSERT INTO schedule (timestamp,status,appt_date_time,blocks,trans_id,procedure_id,location_id,department_id,resource_id,service_id,customer_id,updated_by) values (CONVERT_TZ(now(),'US/Central','US/Pacific'),1,appt_date_time,no_blocks,trans_id,proc_id,loc_id,dept_id,sp_res_id,ser_id,cust_id,concat('book:',lower(device),'@',DATE_FORMAT(CONVERT_TZ(now(),'US/Central','US/Pacific'),'%m/%d/%Y %h:%i %p')));
						SELECT LAST_INSERT_ID() INTO sched_id;
						update resource_calendar set schedule_id=sched_id where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=sp_res_id;
						COMMIT;
			
						select DATE_FORMAT(appt_date_time, online_datetime_display) INTO display_datetime from appt_sys_config;
						SET done = 1;
					END IF;
				END IF;
			UNTIL done END REPEAT;
			close cur_resource_list;
			
			IF(sched_id = 1) THEN
				SET error_msg = 'SELECTED_DATE_TIME_NOT_AVAILABLE';
			END IF;
					
	  END IF;
   END IF;
END$$
DELIMITER ;