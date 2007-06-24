// VpeResizerMouseListener.h: interface for the VpeResizerMouseListener class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_VPERESIZERMOUSELISTENER_H__C4220897_A1EA_447F_B551_647E5A75D95B__INCLUDED_)
#define AFX_VPERESIZERMOUSELISTENER_H__C4220897_A1EA_447F_B551_647E5A75D95B__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "nsWeakReference.h"
#include "nsIDOMMouseListener.h"
#include "VpeResizer.h"


class VpeResizerMouseListener : public nsIDOMMouseListener  
{
public:
	VpeResizerMouseListener(VpeResizer *resizer);
	virtual ~VpeResizerMouseListener();

	NS_DECL_ISUPPORTS

	/**
	* Processes a mouse down event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD MouseDown(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a mouse up event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD MouseUp(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a mouse click event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*
	*/
	NS_IMETHOD MouseClick(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a mouse click event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*
	*/
	NS_IMETHOD MouseDblClick(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a mouse enter event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD MouseOver(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a mouse leave event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD MouseOut(nsIDOMEvent* aMouseEvent);

	/* void handleEvent (in nsIDOMEvent event); */
	NS_IMETHOD HandleEvent(nsIDOMEvent *event);

private:
	VpeResizer *mResizer;
};

nsresult
NS_NewVpeResizerMouseListener(nsIDOMEventListener ** aInstancePtrResult, VpeResizer *resizer);

#endif // !defined(AFX_VPERESIZERMOUSELISTENER_H__C4220897_A1EA_447F_B551_647E5A75D95B__INCLUDED_)
