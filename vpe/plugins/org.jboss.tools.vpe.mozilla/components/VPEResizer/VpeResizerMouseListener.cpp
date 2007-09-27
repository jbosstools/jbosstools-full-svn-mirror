// VpeResizerMouseListener.cpp: implementation of the VpeResizerMouseListener class.
//
//////////////////////////////////////////////////////////////////////

#include "VpeResizerMouseListener.h"

#include "IVpeResizer.h"

#include "nsIDOMMouseEvent.h"
#include "nsIDOMNSEvent.h"
#include "nsIDOMEventTarget.h"
#include "nsIDOMElement.h"

#ifdef _DEBUG
#include <iostream>
using namespace std;
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

NS_IMPL_ISUPPORTS2(VpeResizerMouseListener, nsIDOMEventListener, nsIDOMMouseListener)

VpeResizerMouseListener::VpeResizerMouseListener(VpeResizer *resizer)
:mResizer(resizer)
{

}

VpeResizerMouseListener::~VpeResizerMouseListener()
{

}

/**
* Processes a mouse down event
* @param aMouseEvent @see nsIDOMEvent.h 
* @returns whether the event was consumed or ignored. @see nsresult
*/
NS_IMETHODIMP
VpeResizerMouseListener::MouseDown(nsIDOMEvent* aMouseEvent)
{
#ifdef _DEBUG
	cout << "C:VpeResizerMouseListener::MouseDown| Start resizing listener" << endl;
#endif
	nsCOMPtr<nsIDOMMouseEvent> mouseEvent ( do_QueryInterface(aMouseEvent) );
	if (!mouseEvent) {
		//non-ui event passed in.  bad things.
		return NS_OK;
	}

	nsCOMPtr<IVpeResizer> objectResizer = do_QueryInterface(mResizer);
	if (objectResizer)
	{
		PRUint16 buttonNumber;
		nsresult res = mouseEvent->GetButton(&buttonNumber);
		if (NS_FAILED(res)) return res;

		PRBool isContextClick;
		
#if defined(XP_MAC) || defined(XP_MACOSX)
		// Ctrl+Click for context menu
		res = mouseEvent->GetCtrlKey(&isContextClick);
		if (NS_FAILED(res)) return res;
#else
		// Right mouse button for Windows, UNIX
		isContextClick = buttonNumber == 2;
#endif
		
		PRInt32 clickCount;
		res = mouseEvent->GetDetail(&clickCount);
		if (NS_FAILED(res)) return res;

		if (!isContextClick && buttonNumber == 0 && clickCount == 1)
		{
			nsCOMPtr<nsIDOMEventTarget> target;
			nsCOMPtr<nsIDOMNSEvent> internalEvent = do_QueryInterface(aMouseEvent);
			res = internalEvent->GetExplicitOriginalTarget(getter_AddRefs(target));
			if (NS_FAILED(res)) return res;
			if (!target) return NS_ERROR_NULL_POINTER;
			nsCOMPtr<nsIDOMElement> element = do_QueryInterface(target);
			if (!element) return NS_ERROR_NULL_POINTER;

			// if the target element is an image, we have to display resizers
			PRInt32 clientX, clientY;
			mouseEvent->GetClientX(&clientX);
			mouseEvent->GetClientY(&clientY);
			return objectResizer->MouseDown(clientX, clientY, element);
		}
	}
	
	return NS_OK;
}

/**
* Processes a mouse up event
* @param aMouseEvent @see nsIDOMEvent.h 
* @returns whether the event was consumed or ignored. @see nsresult
*/
NS_IMETHODIMP
VpeResizerMouseListener::MouseUp(nsIDOMEvent* aMouseEvent)
{
#ifdef _DEBUG
	cout << "C:VpeResizerMouseListener::MouseUp| Stop resizing listener" << endl;
#endif

	nsCOMPtr<nsIDOMMouseEvent> mouseEvent ( do_QueryInterface(aMouseEvent) );
	if (!mouseEvent) {
		//non-ui event passed in.  bad things.
		return NS_OK;
	}
	
	// Don't do anything special if not an HTML editor
	nsCOMPtr<IVpeResizer> objectResizer = do_QueryInterface(mResizer);
	if (objectResizer)
	{
		nsCOMPtr<nsIDOMEventTarget> target;
		nsresult res = aMouseEvent->GetTarget(getter_AddRefs(target));
		if (NS_FAILED(res)) return res;
		if (!target) return NS_ERROR_NULL_POINTER;
		nsCOMPtr<nsIDOMElement> element = do_QueryInterface(target);
		
		PRInt32 clientX, clientY;
		mouseEvent->GetClientX(&clientX);
		mouseEvent->GetClientY(&clientY);
		return objectResizer->MouseUp(clientX, clientY, element);
	}
	
	return NS_OK;
}

/**
* Processes a mouse click event
* @param aMouseEvent @see nsIDOMEvent.h 
* @returns whether the event was consumed or ignored. @see nsresult
*
*/
NS_IMETHODIMP
VpeResizerMouseListener::MouseClick(nsIDOMEvent* aMouseEvent)
{
	return NS_OK;
}

/**
* Processes a mouse click event
* @param aMouseEvent @see nsIDOMEvent.h 
* @returns whether the event was consumed or ignored. @see nsresult
*
*/
NS_IMETHODIMP
VpeResizerMouseListener::MouseDblClick(nsIDOMEvent* aMouseEvent)
{
	return NS_OK;
}

/**
* Processes a mouse enter event
* @param aMouseEvent @see nsIDOMEvent.h 
* @returns whether the event was consumed or ignored. @see nsresult
*/
NS_IMETHODIMP
VpeResizerMouseListener::MouseOver(nsIDOMEvent* aMouseEvent)
{
	return NS_OK;
}

/**
* Processes a mouse leave event
* @param aMouseEvent @see nsIDOMEvent.h 
* @returns whether the event was consumed or ignored. @see nsresult
*/
NS_IMETHODIMP
VpeResizerMouseListener::MouseOut(nsIDOMEvent* aMouseEvent)
{
	return NS_OK;
}


/* void handleEvent (in nsIDOMEvent event); */
NS_IMETHODIMP
VpeResizerMouseListener::HandleEvent(nsIDOMEvent *event)
{
	return NS_OK;
}

nsresult
NS_NewVpeResizerMouseListener(nsIDOMEventListener ** aInstancePtrResult, VpeResizer *resizer)
{
	VpeResizerMouseListener* listener = new VpeResizerMouseListener(resizer);
	if (!listener)
		return NS_ERROR_OUT_OF_MEMORY;
	
	return listener->QueryInterface(NS_GET_IID(nsIDOMEventListener), (void **) aInstancePtrResult);   
}
