TYPE=TRIGGERS
triggers='CREATE DEFINER=`root`@`localhost` trigger nice_add1\nafter insert on NICE\nfor each row\nupdate coment set CONICE = CONICE + 1 where COID=new.NICOMID'
sql_modes=1344274432
definers='root@localhost'
client_cs_names='utf8'
connection_cl_names='utf8_general_ci'
db_cl_names='utf8_general_ci'
created=151386785744
