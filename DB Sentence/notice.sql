	drop sequence notice_seq;
	create sequence notice_seq;
	drop table notice;
	create table notice (
	notice_no number primary key,
	notice_member_id references member (member_id) on delete set null,
	notice_type varchar2(30) not null,
	notice_title varchar2(300) not null,
	notice_content varchar2(4000) not null,
	notice_wtime timestamp  default systimestamp not null,
	notice_views	number default 0,
	check(notice_views > 0),
	check(notice_type in ('운영', '공지', '업데이트', '점검안내', '긴급공지'))
);
