# Postaround

![Postaround](/files/postaround_header.png)

Postaround is a demo app which allows users to see Instagram posts in their their area. At this time the app is in `Sandox` mode only, so user accounts will need to be approved in order to use tha app.

The app will show users 10 posts from their the area. A default radius of 500 meters around the users' current location will be used, with the option of extending the search radius from teh app's UI.

Also, the app will automatically search a wider area if there are no results found near the user.

## Preview

### Demo

You can try it out [here](https://www.juanhernandez.work/postaround).

### Screenshots

![Screenshots](/files/first_row.png)
![Screenshots](/files/second_row.png)

## Running the app

### 1. Get the source code

Clone this repo to your PC and open it in Android Studio.

### 2. Build and run!

The project is ready to be built it and run in an Android emulator or your Android phone.

## Using the app

### 1. Get a sandbox user

Since the app is only for demo purposes is in **[Sandbox Mode](https://www.instagram.com/developer/sandbox/)**. It is however a fully functional environment that allows developers to test the Instagram API.

To get a Sandbox user send me email to [jalejandro.hperez@gmail.com](mailto:jalejandro.hperez@gmail.com). After your account has been confirmed as a Sandbox user, you'll be able to start using the app.

If your account hasn't been enabled as a Sandbox user, the app will show a view similar to the image below.

![No access](/files/no_access.png)

### 2. Exploring the app

Now you're ready to explore the app! Sandbox users are by default from Miami, FL. If you are in Miami, you'll see some cool posts and your app will look like the image below.

![Feed](/files/feed.png)

If you aren't in Miami you will probably see only posts of yourself (if you tag the location in your posts). If you dosn't usually tag your location in posts, you'll likely see a screen like the one below.

![Empty screen](/files/empty.png)

#### Changing the radius setting

To set a custom radius to look for posts, click the ruler icon in the upper right corner of the screen (see image below).

![Ruler](/files/ruler.png)

## A foreword about Unit Tests in the project

Before executing the tests it is important to know that I've used the Model-View-Presenter (MVP) pattern. The MVP pattern allows for greater code readability, testability, cleaness and adaptability to changes. To know more about MVP pattern check [this article](https://android.jlelse.eu/architectural-guidelines-to-follow-for-mvp-pattern-in-android-2374848a0157).

In the images below you can see how I've applied MVP to the Feed functionality for both cases when retrieving the data: Success and Fail.

### Success

![Success case](/files/mvp_success.png)

The Success case involves the following methods:

* `subscribe()`: Used to notify FeedPresenter that FeedActivity is ready to get the data.
* `onRecentMediaStarted`: Used to notify FeedActivity that the API request will start soon.
* `getRecentMedia(...)`: FeedPresenter tells FeedDataSource to get the recent posts within a given radius around a specified location.
* `onCompleted()`: FeedDataSource has finished to get data from the server and notifies the FeedPresenter.
* `onRecentMediaCompleted()`: Notify FeedActivity that all the necessary data is fetched already.
* `onNext(...)`: FeedDataSource returns the requested data to FeedPresenter.
* `onGetRecentMediaSuccess(...)`: FeedPresenter returns the requested data to FeedActivity.

### Fail

![Fail case](/files/mvp_fail.png)

The Fail case involves the following methods that are not involved in the Success case:

* `onError(...)`: FeedDataSource notifies FeedPresenter that an error ocurred during the request.
* `onGetRecentMediaError()`: FeedPresenter notifies FeedActivity about the error.

## Unit Tests included in the project

In this project I've implemented three tests:

* `FeedPresenterTest.getRecentMediaShouldLoadIntoView`: It checks that the View calls the right methods when the Presenter has requested the recent media and the response is successful.
* `FeedPresenterTest.getRecentMediaShouldReturnErrorToView`: This test is similar to the first one. The only change is the Presenter will get an error in the response and the View should trigger the appropriate methods.
* `PrefUtilsTest.securePrefsAreSavedCorrectly`: This test checks that the data is correctly saved by an external library used to securely persist the Instagram API access token accross app sessions.

### How to run the first two tests

Switch to the *Local Unit Tests* View in the file explorer in Android Studio (see the image below). You'll see a folder named *app*. Expand the folders under it until you get to a class named `FeedPresenterTest`. Right click the class and select *Run 'FeedPresenterTest'*.

![Local Unit Test](/file/local_unit_tests.png)

### How to execute the last test

Switch to the *Android Instrumented Tests* View in the file explorer in Android Studio (see the image below). Expand the foders under the *app* folder until you get to a class named `PrefUtilsTest`. Then, right click the class and select *Run 'PrefUtilsTest'*.

![Android Instrumented Tests](/file/android_instrumented_tests.png)

### Instagram API

The main functionality of this app is supported by one endpoint of the Instagram API:

    GET /media/search

#### Parameters

Name | Description
:--- |:---
`ACCESS_TOKEN` | A valid access token
`LAT` | Latitude of the center search coordinate. If used, lng is required.
`LNG` | Longitude of the center search coordinate. If used, lat is required.
`DISTANCE` | Default is 1km (distance=1000), max distance is 5km.

*Reference: [Instagram Developer Documentation](https://www.instagram.com/developer/endpoints/media/)*