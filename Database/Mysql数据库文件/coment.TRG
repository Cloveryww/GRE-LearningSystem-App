TYPE=TRIGGERS
triggers='CREATE DEFINER=`root`@`localhost` trigger nice_delete\nafter delete on COMENT\nfor each row\ndelete from NICE where NICOMID=old.COID'
sql_modes=1344274432
definers='root@localhost'
client_cs_names='utf8'
connection_cl_names='utf8_general_ci'
db_cl_names='utf8_general_ci'
created=151447570623
