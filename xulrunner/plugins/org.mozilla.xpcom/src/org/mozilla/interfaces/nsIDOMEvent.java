/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/dom/interfaces/events/nsIDOMEvent.idl
 */

package org.mozilla.interfaces;

public interface nsIDOMEvent extends nsISupports {

  String NS_IDOMEVENT_IID =
    "{a66b7b80-ff46-bd97-0080-5f8ae38add32}";

  /**
 * The nsIDOMEvent interface is the primary datatype for all events in
 * the Document Object Model.
 *
 * For more information on this interface please see 
 * http://www.w3.org/TR/DOM-Level-2-Events/
 *
 * @status FROZEN
 */
/**
   * The current event phase is the capturing phase.
   */
  int CAPTURING_PHASE = 1;

  /**
   * The event is currently being evaluated at the target EventTarget.
   */
  int AT_TARGET = 2;

  /**
   * The current event phase is the bubbling phase.
   */
  int BUBBLING_PHASE = 3;

  /**
   * The name of the event (case-insensitive). The name must be an XML 
   * name.
   */
  String getType();

  /**
   * Used to indicate the EventTarget to which the event was originally 
   * dispatched.
   */
  nsIDOMEventTarget getTarget();

  /**
   * Used to indicate the EventTarget whose EventListeners are currently 
   * being processed. This is particularly useful during capturing and 
   * bubbling.
   */
  nsIDOMEventTarget getCurrentTarget();

  /**
   * Used to indicate which phase of event flow is currently being 
   * evaluated.
   */
  int getEventPhase();

  /**
   * Used to indicate whether or not an event is a bubbling event. If the 
   * event can bubble the value is true, else the value is false.
   */
  boolean getBubbles();

  /**
   * Used to indicate whether or not an event can have its default action 
   * prevented. If the default action can be prevented the value is true, 
   * else the value is false.
   */
  boolean getCancelable();

  /**
   * Used to specify the time (in milliseconds relative to the epoch) at 
   * which the event was created. Due to the fact that some systems may 
   * not provide this information the value of timeStamp may be not 
   * available for all events. When not available, a value of 0 will be 
   * returned. Examples of epoch time are the time of the system start or 
   * 0:0:0 UTC 1st January 1970.
   */
  double getTimeStamp();

  /**
   * The stopPropagation method is used prevent further propagation of an 
   * event during event flow. If this method is called by any 
   * EventListener the event will cease propagating through the tree. The 
   * event will complete dispatch to all listeners on the current 
   * EventTarget before event flow stops. This method may be used during 
   * any stage of event flow.
   */
  void stopPropagation();

  /**
   * If an event is cancelable, the preventDefault method is used to 
   * signify that the event is to be canceled, meaning any default action 
   * normally taken by the implementation as a result of the event will 
   * not occur. If, during any stage of event flow, the preventDefault 
   * method is called the event is canceled. Any default action associated 
   * with the event will not occur. Calling this method for a 
   * non-cancelable event has no effect. Once preventDefault has been 
   * called it will remain in effect throughout the remainder of the 
   * event's propagation. This method may be used during any stage of 
   * event flow.
   */
  void preventDefault();

  /**
   * The initEvent method is used to initialize the value of an Event 
   * created through the DocumentEvent interface. This method may only be 
   * called before the Event has been dispatched via the dispatchEvent 
   * method, though it may be called multiple times during that phase if 
   * necessary. If called multiple times the final invocation takes 
   * precedence. If called from a subclass of Event interface only the 
   * values specified in the initEvent method are modified, all other 
   * attributes are left unchanged.
   *
   * @param   eventTypeArg Specifies the event type. This type may be 
   *                       any event type currently defined in this 
   *                       specification or a new event type.. The string 
   *                       must be an XML name.
   *                       Any new event type must not begin with any 
   *                       upper, lower, or mixed case version of the 
   *                       string "DOM". This prefix is reserved for 
   *                       future DOM event sets. It is also strongly 
   *                       recommended that third parties adding their 
   *                       own events use their own prefix to avoid 
   *                       confusion and lessen the probability of 
   *                       conflicts with other new events.
   * @param   canBubbleArg Specifies whether or not the event can bubble.
   * @param   cancelableArg Specifies whether or not the event's default 
   *                        action can be prevented.
   */
  void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg);

}