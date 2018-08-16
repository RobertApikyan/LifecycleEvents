![N|Solid](https://github.com/RobertApikyan/LifecycleEvents/blob/master/Intro/intro.png?raw=true)

## API 16+

LifecycleEvents is event bus implementation using lifecycle from android architecture components and kotlin language features, also it's designed for Java language users. With LifecycleEvents you can send any object as an event.

## Simple Usage
##### Kotlin
```kotlin
...
// Sending userInfo as an event
userInfo.sendAsEvent()
...

// Receiving userInfo
disposable = observeEvent<UserInfo> { userInfo ->
            // use userInfo object here
        }
...

// Cancel event observation when you do need it anymore
disposable.dispose()
...
````
##### Java
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

// Cancel event observation when you do need it anymore
disposable.dispose()
...
````
## With Android Lifecycle
While observing events inside Activity or Fragment we do not need to cancel event observation manually, we can just pass lifecycleOwner instance as an observeEvent methods argument, and dispose() method will be called authomatically with onDestroy() lifecycle method. 
##### Kotlin
```kotlin
    observeEvent<User>(lifecycleOwner: this) { user ->
            user.printOut()
    }
```
##### Java


## Gradle
### Add to project level build.gradle
```groovy
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Add dependency to app module level build.gradle
```groovy
    dependencies {
	        compile 'com.github.RobertApikyan:SegmentedControl:release_1.0.2'
	}
```
 

	

```

## Simple usage in XML
```java

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
