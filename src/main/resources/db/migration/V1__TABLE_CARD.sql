create table if not exists cards (
   card_id int primary key auto_increment,
   card_number varchar(16) not null,
   card_password varchar(60) not null,
   available_balance decimal(12,2) not null default 0,
   dh_created_at timestamp null,
   dh_updated_at timestamp null,
   record_version int not null
);




