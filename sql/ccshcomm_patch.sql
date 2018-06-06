DROP TABLE customer_pledge_vendor;
CREATE TABLE `customer_pledge_vendor` (                   
  `id` int(10) NOT NULL AUTO_INCREMENT,                   
  `customer_pledge_id` bigint(20) unsigned NOT NULL,      
  `vendor_id` int(10) NOT NULL,                           
  `vendor_pledge_amount` decimal(10,2) DEFAULT '0.00',    
  `account_number` varchar(250) DEFAULT NULL,             
  PRIMARY KEY (`id`)                                      
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `customer_vendor` (                        
   `id` int(10) NOT NULL AUTO_INCREMENT,                 
   `fund_id` int(10) NOT NULL,                           
   `vendor_name` varchar(100) NOT NULL,                  
   `vendor_name_tts` varchar(100) NOT NULL,              
   `vendor_name_audio` varchar(100) NOT NULL,            
   `placement` tinyint(4) DEFAULT NULL,                  
   `delete_flag` char(1) DEFAULT 'N',                    
   PRIMARY KEY (`id`)                                    
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;  


insert into `customer_vendor` (`fund_id`, `vendor_name`, `vendor_name_tts`, `vendor_name_audio`, `placement`, `delete_flag`) values('1','Utility Vendor','Utility Vendor','vendor','1','N');
insert into `customer_vendor` (`fund_id`, `vendor_name`, `vendor_name_tts`, `vendor_name_audio`, `placement`, `delete_flag`) values('1','Direct Pay to Applicant','Direct Pay to Applicant','direct_pay','2','N');

