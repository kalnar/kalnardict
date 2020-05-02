
CREATE TABLE word(
   id INTEGER PRIMARY KEY NOT NULL,
   base_form TEXT NOT NULL,
   base_form_alt TEXT NOT NULL,
   translation TEXT NOT NULL,
   dictionary_id INTEGER NOT NULL
);

insert into word (id,base_form,base_form_alt,translation,dictionary_id) values (1,"asztal","asztal","table",1);