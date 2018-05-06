# Postaround

![Postaround](/files/postaround_header.png)

It's a demo app where people (right now only Sandbox users) can see what Instagram posts have been posted near their area. 

When the user opens the app, the user will see 10 posts from the area. The search will be default to 500 meters around the users current location but the app will allow the user to extend the search radius. 

Also, the app will automatically search a wider area if there are no results found near the user.

# Preview
## Demo
You can try it out ![here](https://www.juanhernandez.work/postaround)

## Screenshots
![Screenshots](/files/first_row.png)

![Screenshots](/files/second_row.png)

# Running the app
## 1. Cloning the project
Clone the project to your PC and open it with Android Studio.
## 2. Compile and run!
The project is ready to compile it and run it in an Android emulator or your Android phone.

# Using the app
## 1. Make you a sandbox user
Since the app is only for demo purposes is in **![Sandbox Mode](https://www.instagram.com/developer/sandbox/)**. This is a fully functional environment that allows developers to test the Instagram API.

To make you a Sandbox user just write me an email to jalejandro.hperez@gmail.com. When I confirm that you are a Sandbox user you can start using the app.

If you aren't a Sandbox user, when you try to login to the app, it will look like the image below.

![No access](/files/no_access.png)

## 2. Exploring the app
You are ready to explore the app once you log in. The Sandbox users by default are from Miami, so, if you are in Miami, you will see some cool posts and your app will look like the image below.

![Feed](/files/feed.png)

If you aren't in Miami you will probably see only posts of yourself (if you tag the location in your posts). If you are the instagramer that doesn't like to tag the location in posts, you'll see a screen like the one below.

![Empty screen](/files/empty.png)

### Selecting a distance
To pick a custom distance click the ruler icon placed in the upper right (see image below).

![Ruler](/files/ruler.png)

# Making tests!
In this project I implemented three tests
* `FeedPresenterTest.getRecentMediaShouldLoadIntoView`: With this test I check the View calls the right methods when the Presenter has requested the recent media and the response is successful.
* `FeedPresenterTest.getRecentMediaShouldReturnErrorToView`: This test is similar to the first one. The only change is the Presenter got error in the response and I want to make sure the View triggers the right methods.
* `PrefUtilsTest.securePrefsAreSavedCorrectly`: This test was implemented to check the data is correctly saved by an external library I use to save securely the access token given by Instagram.

## How to execute the first two tests
In Android Studio go to the *Project View* and select *Local Unit Test*. There you will see the *app* folder. Expand the folder until you get to `FeedPresenterTest` class. Right click the class and select *Run 'FeedPresenterTest'*. 


# Let's dive into the code!
## Instagram API
The main functionality of this app is supported by one endpoint of the Instagram API:
```
GET /media/search
```
### Parameters
* **ACCESS_TOKEN**: A valid access token
* **LAT**: Latitude of the center search coordinate. If used, lng is required.
* **LNG**: Longitude of the center search coordinate. If used, lat is required.
* **DISTANCE**: Default is 1km (distance=1000), max distance is 5km.

*Reference: ![Instagram Developer Documentation](https://www.instagram.com/developer/endpoints/media/)*

## MVP Pattern