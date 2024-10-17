CREATE TABLE member(
member_id varchar2(20) PRIMARY key,
member_pw varchar2(16) NOT NULL,
member_name varchar2(21) NOT NULL,
member_rank varchar2(18) DEFAULT '회원' NOT NULL,
member_email varchar2(60) NOT NULL,
member_contact char(11) NOT NULL,
member_post varchar2(6) NOT NULL,
member_address1 varchar2(300) NOT NULL,
member_address2 varchar2(300) NOT NULL,
member_join_date DEFAULT sysdate NOT NULL,
member_point NUMBER DEFAULT 0 NOT NULL,
check(regexp_like(member_id, '^[a-z][a-z0-9]{4,19}$')),
check(
    regexp_like(member_pw, '^[A-Za-z0-9!@#$]{8,16}$')
    and
    regexp_like(member_pw, '[A-Z]+')
    and
    regexp_like(member_pw, '[a-z]+')
    and
    regexp_like(member_pw, '[0-9]+')
    and
    regexp_like(member_pw, '[!@#$]+')
),
check(member_rank in ('회원', '관리자')),
check(regexp_like(member_contact, '^010[1-9][0-9]{6,7}$')),
check(regexp_like(member_post, '^[0-9]{5,6}$')),
check(member_post is not null and member_address1 is not null and member_address2 is not null),
check(member_point >= 0)
);
