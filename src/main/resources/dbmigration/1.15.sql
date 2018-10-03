-- apply changes
create table rc_connect_player (
  id                            integer auto_increment not null,
  player                        varchar(40),
  new_server                    varchar(255),
  old_server                    varchar(255),
  cause                         varchar(255),
  args                          varchar(255),
  constraint pk_rc_connect_player primary key (id)
);

