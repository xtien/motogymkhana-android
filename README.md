# motogymkhana android app #
Moto Gymkhana android app for a national competition.

The app lets you select a country (currently NL or EU for European championship) and a season (2015, 2016, 2017), then shows you the planned rounds, registered riders, and results. 

If you have the admin password for your country or region, extra functions open up for registering riders, registration of time, editing rider profiles, editing settings, etc. 

The app uses a server at api.gymcomp.com, where race results are stored. You can view the race results at www.gymcomp.com/nl or /eu.

The app uses Roboguice for dependency injection, ORMlite for storing data, Robolectic for testing, Jackson for json serialization and deserialization. The Roboguice jar has been slightly modified to remove a bug that is not fixed in the official Roboguice repository, and to add a `RoboAppCompatActivity` that is not available in Roboguice (yet).

Wiki page has more info.
