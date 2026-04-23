

# About

KalnarDict is a dictionary application that can load dictionaries on the storage and thus you can use your favorite dictionary offline (e.g. in transport or in airplane). 

For now it supports only a custom SQLite scheme as external source. The goal is to support the most common formats like TEI or Stardict to load dictionaries and add support for different online dictionaries. 

# Demo

Here is a small demo of the application for the mock flavor:

https://github.com/user-attachments/assets/2474ae87-5e1e-4771-8f3a-9a31dbff5ccd


# Custom Database Schema

In your external database, you need to have a table `meta_info`. Here is the schema:

```
create TABLE meta_info(
   id INTEGER PRIMARY KEY NOT NULL,
   dictionary_name TEXT NOT NULL,
   language_from TEXT NOT NULL,
   language_to TEXT NOT NULL,
   version TEXT NOT NULL
);
```



The `dictionary_name` field should be the name of the table of a dictionary. For example `english_french_dictionary`. For that, the scehama need to look like this:



```
create TABLE english_french_dictionary(
   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
   base_form TEXT NOT NULL,
   base_form_alt TEXT NOT NULL,
   translation TEXT NOT NULL
);
```



Where `base_form_alt` is the word without accents. 

Now you can create any dictionary that you want. 



## Some features that are on the non deterministic roadmap

	- support for sign language
	- Favorites, history
	- Show dictionary from clipboard via a service
	- support for wikipedia, google translate, larousse
	- add forvo API support for prononciation
	- support for KMP - iOS, Desktop

