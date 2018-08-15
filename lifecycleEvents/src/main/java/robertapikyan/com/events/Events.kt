@file:Suppress("UNCHECKED_CAST")
@file:JvmName("Events")

package robertapikyan.com.events

import android.arch.lifecycle.LifecycleOwner
import org.jetbrains.annotations.NotNull
import robertapikyan.com.events.executors.Threads
import robertapikyan.com.events.implementation.AbstractEventObserver
import robertapikyan.com.events.implementation.EventObserver
import robertapikyan.com.events.implementation.lifecycle.PendingEventsRules

/**
 * EXAMPLE
 *
 *  // here is data class
 *  data class User(val userName:String,val age:Int, ...)
 *
 *  // the sender activity
 *  class FarFarAwayFromReceiverActivity : AppCompatActivity(){
 *
 *          ...
 *          override fun onCreate(savedInstanceState: Bundle?) {
 *              super.onCreate(savedInstanceState)
 *              setContentView(R.layout....)
 *          }
 *
 *          override fun onUserFetchSuccess(user:User){
 *              user.sendAsEvent() // with kotlin extension method sendAsEvent
 *          }
 *          ...
 *
 *  }
 *
 *   // receiver activity
 *   class ReceiverActivity : AppCompatActivity() {
 *
 *          ...
 *
 *          override fun onCreate(savedInstanceState: Bundle?) {
 *              super.onCreate(savedInstanceState)
 *
 *              setContentView(R.layout....)
 *
 *              // 1. first option
 *              observeEvent<LogoutEvent>(lifecycleOwner = this) { user ->
 *                  /*do you user work*/
 *              }
 *
 *              // 2. second option
 *               observeEvent<LogoutEvent>(lifecycleOwner = this,
 *                                         rules = PendingEventsRules.ONLY_LAST) // only the last event from pendings will be received
 *               { user ->
 *                  /*do you user work*/
 *              }
 *
 *              // 3. option three
 *               observeEvent<LogoutEvent>(lifecycleOwner = this,
 *                                         receiveOn = Threads.BACKGROUND // events will be received on the background thread
 *                                         rules = PendingEventsRules.REVERSE_ORDER) // pending events will be received in reversed order
 *               { user ->
 *                  /*do you user work, but be careful your on background thread ;) */
 *              }
 *
 *              ....
 *          }
 *
 *          ...
 *  }
 *
 */

/**
 * Use this extension method in order to send events directly from event class.
 * Any not null object can be send as an event
 * Example.
 *
 * val user = User(name= "Rob",age= 14, ... )
 * // now you can send user object as an event
 * user.sendAsEvent()
 *
 * @param T is event type.
 * @param sendOn is specified as MAIN or BACKGROUND, default value is MAIN, use BACKGROUND if there
 * is a big number of observers for specified event type.
 */
fun <T : Any> T.sendAsEvent(sendOn: Threads = Threads.MAIN) =
        sendEvent(this, sendOn)

/**
 *
 * Use this method in order to send events.
 * Any not null object can be send as an event
 *
 * @param T is event type.
 * @param event, not null data class
 *
 */
fun <T : Any> sendEvent(@NotNull event: T) = sendEvent(event, Threads.MAIN)

/**
 * Use this method in order to send events.
 * Any not null object can be send as an event
 * Example.
 *
 * val user = User(name= "Rob",age= 14, ... )
 * // now you can send user object as an event
 * user.sendAsEvent()
 *
 * @param T is event type.
 * @param event, not null data class
 * @param sendOn is specified as MAIN or BACKGROUND, use BACKGROUND if there
 * is a big number of observers for specified event type.
 */
fun <T : Any> sendEvent(@NotNull event: T, sendOn: Threads) {
    @Suppress("SENSELESS_COMPARISON")
    if (event != null) {
        EventObservables.observable.sendEvent(event, sendOn)
    }
}

/**
 * Use this class to start observing for specified event type
 */
fun <T : Any> observeEvent(observer: AbstractEventObserver<T>) =
        EventObservables.observable.observe(observer as AbstractEventObserver<Any>)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param receiveOn is the thread where event will be delivered,
 * by default observations are invoking on the MAIN thread
 */
inline fun <reified T> observeEvent(receiveOn: Threads = Threads.MAIN,
                                    noinline observer: (T) -> Unit) =
        EventObservables.observable.observe(AbstractEventObserver.fromLambda(T::class.java, receiveOn, PendingEventsRules.IN_ORDER, observer)
                as AbstractEventObserver<Any>)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
fun <T> observeEvent(lifecycleOwner: LifecycleOwner,
                     observer: AbstractEventObserver<T>) =
        EventObservables.observable.observe(lifecycleOwner, observer
                as AbstractEventObserver<Any>)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param receiveOn is the thread where event will be delivered,
 * by default observations are invoking on the MAIN thread
 * @param rule is the pending events handling policy, pending events are thous events that are received
 * when lifecycleOwner is not visible ( after onStop() callback ), by default all events will be delivered
 * after lifecycleOwner onStart callback, with the same ordering
 * @param observer this observer will be automatically removed when lifecycleOwner is destroyed
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
inline fun <reified T> observeEvent(lifecycleOwner: LifecycleOwner,
                                    receiveOn: Threads = Threads.MAIN,
                                    rule: PendingEventsRules = PendingEventsRules.IN_ORDER,
                                    noinline observer: (T) -> Unit) =
        EventObservables.observable.observe(lifecycleOwner,
                AbstractEventObserver.fromLambda(T::class.java, receiveOn, rule, observer)
                        as AbstractEventObserver<Any>)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param rule is the pending events handling policy, pending events are thous events that are received
 * when lifecycleOwner is not visible ( after onStop() callback ), by default all events will be delivered
 * after lifecycleOwner onStart callback, with the same ordering
 * @param observer this observer will be automatically removed when lifecycleOwner is destroyed
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
inline fun <reified T> observeEvent(lifecycleOwner: LifecycleOwner,
                                    rule: PendingEventsRules = PendingEventsRules.IN_ORDER,
                                    noinline observer: (T) -> Unit) =
        EventObservables.observable.observe(lifecycleOwner,
                AbstractEventObserver.fromLambda(T::class.java, Threads.MAIN, rule, observer)
                        as AbstractEventObserver<Any>)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param observableClass is the type of event that will be observed
 * @param receiveOn is the thread where event will be delivered,
 * by default observations are invoking on the MAIN thread
 * @param rule is the pending events handling policy, pending events are thous events that are received
 * when lifecycleOwner is not visible ( after onStop() callback ), by default all events will be delivered
 * after lifecycleOwner onStart callback, with the same ordering
 * @param observer this observer will be automatically removed when lifecycleOwner is destroyed
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
fun <T : Any> observeEvent(observableClass: Class<T>,
                           receiveOn: Threads = Threads.MAIN,
                           rule: PendingEventsRules = PendingEventsRules.IN_ORDER,
                           observer: EventObserver<T>) =
        EventObservables.observable.observe(AbstractEventObserver
                .fromLambda(observableClass, receiveOn, rule, observer::onEvent) as AbstractEventObserver<Any>)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param observableClass is the type of event that will be observed
 * @param rule is the pending events handling policy, pending events are thous events that are received
 * when lifecycleOwner is not visible ( after onStop() callback ), by default all events will be delivered
 * after lifecycleOwner onStart callback, with the same ordering
 * @param observer this observer will be automatically removed when lifecycleOwner is destroyed
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
fun <T : Any> observeEvent(observableClass: Class<T>,
                           rule: PendingEventsRules = PendingEventsRules.IN_ORDER,
                           observer: EventObserver<T>) =
        observeEvent(observableClass, Threads.MAIN, rule, observer)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param observableClass is the type of event that will be observed
 * @param receiveOn is the thread where event will be delivered,
 * by default observations are invoking on the MAIN thread
 * @param observer this observer will be automatically removed when lifecycleOwner is destroyed
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
fun <T : Any> observeEvent(observableClass: Class<T>,
                           receiveOn: Threads = Threads.MAIN,
                           observer: EventObserver<T>) =
        observeEvent(observableClass, receiveOn, PendingEventsRules.IN_ORDER, observer)

/**
 * Use this class to start observing for specified event type, this method receives lifecycleOwner
 * as an argument, end events will be delivered to observable only if lifecycleOwner's lifecycle state is
 * between onStart and onStop, events that are out off this state will be delivered later or will be
 * ignored, it depends on specified rules [PendingEventsRules]
 *
 * @param observableClass is the type of event that will be observed
 * @param observer this observer will be automatically removed when lifecycleOwner is destroyed
 * @return [robertapikyan.com.events.implementation.Disposable],
 * after calling disposable.dispose() method the observable will be removed
 */
fun <T : Any> observeEvent(observableClass: Class<T>,
                           observer: EventObserver<T>) =
        observeEvent(observableClass, Threads.MAIN, PendingEventsRules.IN_ORDER, observer)


