Project is developed using Svelte(3.44.0), Spring Boot(5.3.11) and MySql

Rozer is a single page application.
Entry Point of client App is main.js and App.svelte, App.svelte is responsible
for managing the view depending upon the app state.
● Assets directory holds all the static resources
● Const directory holds the application level constants (labels, URL)
● Store directory holds the global level store
● Util directory holds the utility methods of the application
● View directory contains all the application UI
● Widget directory contains the custom widget of the application

Server directory holds the API code and the end point can be found at the
controller directory.

Running the application locally

Setup client
Prerequisite
● Node JS
● Visual Studio code
Install dependency

● Open CMD/terminal and navigate to client folder and run

npm install

● Run application in development mode

Setup server

Prerequisite
● JDK 1.8

● MySql
● MySql workbench
● IntelliJ IDEA CE

Once the prerequisite is installed, open the app in intelliJ IDEA

npm run dev
This command will give a url to launch the UI code, open the URL in

browser

Ex: http://localhost:3000
