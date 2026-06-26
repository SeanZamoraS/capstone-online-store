# API CAPSTONE READ ME

## About:

This is a project based on starter code provided by Pluralsight. 
I fixed bugs, created unit tests, and implemented the categories and shopping cart services/controllers. 

This project is meant to be used in creating a webstore with a JavaScript/html front end that connects to an API
which draws from a SQL database. 

I did not touch the front end of the project for the most part. I was focused on creating new features on the 
back end and testing with Insomnia rather than the front end. 

You will find my bug tracking .md files inside database/capstone-issues/aaa-bug-tracking.

## How to run project:

At this point in time, in order to run this project you will need at least one IDE installed and MySQL server.

First running the create_database_videogamestore_ORIGINAL.sql script on your database is necessary to set up the data.
From that point the project's ECommerceApplication can be run through the IDE to start the API.
If desired, the capstone-client-videogamestore project folder can be run in another IDE (Visual Studios using Live Server)
to view the html interface. Otherwise, the API also accepts requests from Insomnia and other tools. 

## Development notes:

I wanted to try being detailed in my documentation of bugs as much as I could while still trying to implement some features. I thought this would be a useful skill to focus on for this capstone and would incorporate some of my IT skills.

I did not realize that the .yaml for this project existed in the starter code for Insomnia and that we were supposed to be using it 
as a list of requirements for our app. I have thus updated (after turning in) my code to fix some of 
the required status codes for the tests. I have also included my own tests I wrote in Insomnia
that I went ahead with the project on when they passed rather than using the proper ones. 
You may find this in the base of the project structure called insomniacapstonetests.yaml.