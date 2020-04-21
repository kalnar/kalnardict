
CREATE TABLE word(
   id INTEGER PRIMARY KEY NOT NULL,
   base_form TEXT NOT NULL,
   translation TEXT NOT NULL
);

insert into word (id,base_form,translation) values (1,"asztal","table");