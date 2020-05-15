create TABLE meta_info(
   id INTEGER PRIMARY KEY NOT NULL,
   dictionary_name TEXT NOT NULL,
   language_from TEXT NOT NULL,
   language_to TEXT NOT NULL,
   version TEXT NOT NULL
);

create TABLE test_fr_dictionary(
   id INTEGER PRIMARY KEY NOT NULL,
   base_form TEXT NOT NULL,
   base_form_alt TEXT NOT NULL,
   translation TEXT NOT NULL
);

insert into
    test_fr_dictionary (id,base_form,base_form_alt,translation)
values
    (1,"konyha","konyha","cuisine");

insert into
    meta_info (id,dictionary_name,language_from,language_to,version)
values
    (1,"test_fr_dictionary","Hungarian","French","0.01");
