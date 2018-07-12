show databases;
#drop database WORD;
create database myProject;
use myProject;
show tables;
insert into WORD value(1,"f","24");
select * from WORD;
show triggers;

#=============================
#用户表
#属性	用户ID：int USID
#		账号：char[50] USACCOUNT
#		密码：char[20] USPASSWORD
create table US
(
	USID int not null unique auto_increment,
    USACCOUNT char(50) not null unique,
    USPASSWORD char(20) not null,
    primary key(USID)
);

select * from US;
drop table US;

#=============================
#考卷表
#属性	试卷ID：int PAID
#		时间：char[11] PATIME
#		题目编号：int*10 PAQUES(n)
#		题目类型：char[3]*10 PATYPE(n)
create table PAPER
(
	PAID int not null unique,
    PATIME char(11) not null,
    PAQUES1 int not null,
    PAQUES2 int not null,
    PAQUES3 int not null,
    PAQUES4 int not null,
    PAQUES5 int not null,
    PAQUES6 int not null,
    PAQUES7 int not null,
    PAQUES8 int not null,
    PAQUES9 int not null,
    PAQUES10 int not null,
    primary key(PAID)
);

insert into PAPER 
(PAID, PATIME, PAQUES1, PAQUES2, PAQUES3, PAQUES4, PAQUES5, PAQUES6, PAQUES7, PAQUES8, PAQUES9, PAQUES10)
VALUES(1, '2017.01', 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

select * from PAPER;



#================================
#题目类型表
#属性	题目ID：int 
create table QUESTYPE
(
	QUESID int not null unique, 
    ttype char(3) not null,
    primary key(QUESID)
);

insert into QUESTYPE (QUESID, ttype) VALUES(1, '51');
insert into QUESTYPE (QUESID, ttype) VALUES(2, '51');
insert into QUESTYPE (QUESID, ttype) VALUES(3, '62');
insert into QUESTYPE (QUESID, ttype) VALUES(4, '62');
insert into QUESTYPE (QUESID, ttype) VALUES(5, '62');
insert into QUESTYPE (QUESID, ttype) VALUES(6, '93');
insert into QUESTYPE (QUESID, ttype) VALUES(7, '62');
insert into QUESTYPE (QUESID, ttype) VALUES(8, '62');
insert into QUESTYPE (QUESID, ttype) VALUES(9, '62');
insert into QUESTYPE (QUESID, ttype) VALUES(10, '62');

show tables;
select * from QUESTYPE;

#==============================
#单词表
#属性	单词ID：int WOID
#		英文：char[20] WOENG
#		中文：char[20] WOCHI
create table WORD
(
	WOID int not null unique auto_increment,
    WOENG char(20) not null unique,
    WOCHI char(20) not null,
    primary key(WOID, WOENG)
);

drop table WORD;

select * from WORD;

insert into WORD (WOENG, WOCHI) VALUES('undimensional', '肤浅的');
insert into WORD (WOENG, WOCHI) VALUES('ballyhoo', '哗众取宠的');
insert into WORD (WOENG, WOCHI) VALUES('quip', '机智幽默的评论');


#==============================
#用户错题表
#属性	用户ID：int USID
#		题目ID：int QUESID
#		题目答案 : char(4) QUESANS
create table USER_FAULT
(
	USID int not null,
    QUESID int not null,
    QUESANS char(4) not null,
    primary key(USID, QUESID)
);

select * from user_fault;
#==============================
#五选一题目表
#属性	题目编号：int QUID
#		题目内容：varchar QUCON
#		五个选项：char[50] QUOPT*5
#		一个答案: char[2] QUANS
create table QUES51
(
	QUID int not null unique,
    QUCON varchar(500) not null unique,
    QUOPTA char(50) not null,
    QUOPTB char(50) not null,
    QUOPTC char(50) not null,
    QUOPTD char(50) not null,
    QUOPTE char(50) not null,
    QUANS1 char(2) not null,
    primary key(QUID)
);

#drop table QUES51;
show tables;
select * from QUES51;

#==============================
#六选二题目表
#属性	题目编号：int QUID
#		题目内容：char[500] QUCON
#		六个选项：char[50] QUOPT*6
#		两个答案: char[2] QUANS*2
create table QUES62
(
	QUID int not null unique,
    QUCON varchar(500) not null unique,
    QUOPTA char(50) not null,
    QUOPTB char(50) not null,
    QUOPTC char(50) not null,
    QUOPTD char(50) not null,
    QUOPTE char(50) not null,
    QUOPTF char(50) not null,
    QUANS1 char(2) not null,
    QUANS2 char(2) not null,
    primary key(QUID)
);

select * from QUES62;

#==============================
#九选三题目表
#属性	题目编号：int QUID
#		题目内容：char[500] QUCON
#		九个选项：char[50] QUOPT*6
#		三个答案: char[2] QUANS*2
create table QUES93
(
	QUID int not null unique,
    QUCON varchar(500) not null unique,
    QUOPTA char(50) not null,
    QUOPTB char(50) not null,
    QUOPTC char(50) not null,
    QUOPTD char(50) not null,
    QUOPTE char(50) not null,
    QUOPTF char(50) not null,
	QUOPTG char(50) not null,
    QUOPTH char(50) not null,
    QUOPTI char(50) not null,
    QUANS1 char(2) not null,
    QUANS2 char(2) not null,
	QUANS3 char(2) not null,
    primary key(QUID)
);
select * from QUES93;

#================================
#评论表
#属性	评论编号 int COID
#		评论时间 char[11] COTIME
#		评论内容 char[50] COCON
#		点赞数量 int CONICE
#		题目编号 int COQUESID
#		发送用户编号 int COFROM
create table COMENT
(
	COID int not null unique auto_increment,
    COTIME varchar(30) not null,
    COCON varchar(300) not null,
    CONICE int not null,
    COQUESID int not null,
    COFROM int not null,
    primary key(COID)
);



select * from COMENT;

#点赞时给评论表的点赞数+1
create trigger nice_add1
after insert on NICE
for each row
update coment set CONICE = CONICE + 1 where COID=new.NICOMID;

#删除评论时同时删除点赞表的相关信息
drop trigger nice_delete;

create trigger nice_delete
after delete on COMENT
for each row
delete from NICE where NICOMID=old.COID;
#drop table coment;

#===============================
#考卷-生词表
#属性	考卷编号：int PWPAPERID
#		单词编号：int PWWORDID
create table PAPER_WORD
(
	PWPAPERID int not null,
    PWWORDID int not null,
    primary key(PWPAPERID, PWWORDID)
);

insert into PAPER_WORD (PWPAPERID, PWWORDID) values(1, 1);
insert into PAPER_WORD (PWPAPERID, PWWORDID) values(1, 2);
insert into PAPER_WORD (PWPAPERID, PWWORDID) values(1, 3);

#===============================
#用户生词表
#属性	用户ID：int USERID
#		单词英文：char[20] UWENG
#		单词中文：char[20] UWCHI
#		记录时间：char[11] UWTIME
create table USER_WORD
(
	USERID int not null,
    UWENG varchar(30) not null,
    UWCHI varchar(100) not null,
    UWTIME varchar(50) not null,
    primary key(USERID, UWENG)
);
drop table USER_WORD;
select * from USER_WORD;


#===============================
#笔记表
#属性	用户ID：int NOUSERID
#		题目编号：int NOQUESID
#		笔记内容：char(50) NOCON
#		笔记时间(精确到分钟)：char[17] NOTIME
create table NOTE
(
	NOUSERID int not null,
    NOQUESID int not null,
    NOCON char(50) not null,
    NOTIME char(17) not null,
    primary key(NOSERID, NOQUESID)
);

#================================
#点赞记录表
#属性	用户ID：int NIUSERID
#		评论ID：int NICOMID
#		点赞时间(精确到分钟)：char[17] NITIME
create table NICE
(
	NIUSERID int not null,
    NICOMID int not null,
    NITIME varchar(50) not null,
    primary key(NIUSERID, NICOMID)
);

drop table NICE;
select * from NICE;

#==================================
#做题痕迹表
#属性	用户ID：int
#		试卷ID：int
# 		答案：char[4]
create table USANS
(
	USID int not null,
    PAID int not null,
    ANS1 char(4),
    ANS2 char(4),
    ANS3 char(4),
    ANS4 char(4),
    ANS5 char(4),
    ANS6 char(4),
    ANS7 char(4),
    ANS8 char(4),
    ANS9 char(4),
    ANS10 char(4),
    primary key(USID, PAID)
);

show tables;
select * from USANS;
update USANS set ANS1='B',ANS2='D',ANS3='CD',ANS4='BC',ANS5='CD',ANS6='BDE',ANS7='C',ANS8='D',ANS9='CD',ANS10 = 'CD' where USID = 1 and PAID = 1;


