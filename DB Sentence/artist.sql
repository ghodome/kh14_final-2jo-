create sequence artist_seq;
drop sequence artist_seq;
create table artist(
artist_no number primary key,
artist_name varchar2(90) not null,
artist_description varchar2(900) not null,
artist_history varchar2(900) not null,
artist_birth char(10) not null,
artist_death char(10) not null,
check(regexp_like(artist_name,'^([가-힣]{2,7}|[a-zA-Z ]{1,30})$')),
check(regexp_like(artist_birth, '^([0-9]{4})-(02-(0[1-9]|1[0-9]|2[0-8])|(0[469]|11)-(0[1-9]|1[0-9]|2[0-9]|30)|(0[13578]|1[02])-(0[1-9]|1[0-9]|2[0-9]|3[01]))$')),
check(regexp_like(artist_death, '^([0-9]{4})-(02-(0[1-9]|1[0-9]|2[0-8])|(0[469]|11)-(0[1-9]|1[0-9]|2[0-9]|30)|(0[13578]|1[02])-(0[1-9]|1[0-9]|2[0-9]|3[01]))$'))
);
