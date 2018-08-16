![N|Solid](https://github.com/RobertApikyan/LifecycleEvents/blob/master/Intro/intro.png?raw=true)

### API 16+
LifecycleEvents library is an event bus implementation, using lifecycle from android architecture components and kotlin language features.
It is also designed for Java language.
### Simple Usage
LifecycleEvents allows us to send any object as an event,
In this example, UserInfo's instance will be send as an event.
##### kotlin
```kotlin
...
// Sending userInfo as an event
userInfo.sendAsEvent() // sendAsEvent is a kotlin extention method, for java user Events.sendEvent(userInfo) mthod
...

// Receiving userInfo
disposable = observeEvent<UserInfo> { userInfo ->
            // use userInfo object here
        }
...

// Cancel event observation
disposable.dispose()
...
````
##### java
```java
...

// Sending userInfo as an event
disposable = Events.sendEvent(userInfo);
...

// Receiving userInfo
Events.observeEvent(UserInfo.class, userInfo ->{
	// use userInfo object here
});
...

// Cancel event observation
disposable.dispose()
...
````
## With Android Lifecycle
While observing events inside Activity or Fragment we do not need to cancel event observation manually, we can just pass lifecycleOwner instance as an observeEvent method's argument, and dispose() method will be called automatically with onDestroy() lifecycle method.
##### kotlin
```kotlin
observeEvent<User>(lifecycleOwner: this) { userInfo ->
    // use userInfo object here
}
```
## Pending Events Handling
Observation with lifecycleOwner allows us to handle events only when activity or fragment are at list started but not stopped.
Events that are arrived when activity/fragment is in the stopped state will be marked as pending events, and will be delivered
with onStart() lifecycle method. By default pending events will delivered with the same receiving order,
but we can change delivery behavior by setting observeEvent method's rule attribute.
##### kotlin
```kotlin
// After onStart() Only the last event will be delivered
observeEvent<User>(this, PendingEventsRules.ONLY_LAST) { userInfo ->
    // use userInfo object here
}
```
There are five types of PendingEventsRules
 1. IGNORE
 2. IN_ORDER // default
 3. REVERSE_ORDER
 4. ONLY_LAST
 5. ONLY_FIRST
 6. IMMEDIATE // events will be delivered, even after onStop()

## Threads Handling
By default all events will be sent and received on the main thread, but we can change this.
In the above example, event observation will be done on the background worker thread (do this if you have big number of observers for specified event),
and the event will be received on the main thread.
##### kotlin
```kotlin
...
// observers will be iterated on the background worker thread
user.sendAsEvent(Threads.BACKGROUND)
...
// event will be recieved on the main thread (here you can not specify Threads.MAIN, it is the default value)
observeEvent<User>(this, Threads.MAIN) { userInfo ->
    // use userInfo object here
}

// also you can specify  Threads.BACKGROUND, in order to receive events on the background worker thread
observeEvent<User>(this, Threads.BACKGROUND) { userInfo ->
    // use userInfo object here
}
```




[![View Robert Apikyan profile on LinkedIn](https://www.linkedin.com/img/webpromo/btn_viewmy_160x33.png)](https://www.linkedin.com/in/robert-apikyan-24b915130/)

License
-------

    Copyright 2017 Robert Apikyan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
